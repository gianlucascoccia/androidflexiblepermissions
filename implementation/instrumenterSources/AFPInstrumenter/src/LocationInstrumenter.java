import java.util.ArrayList;

import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

public class LocationInstrumenter {
	
	
	private static final String LOCATIONMANAGER_PATH = "android/location/LocationManager";
	private static final String LOCATIONMANAGER_URI = "android.location.LocationManager";

	private static final String AFPLOCATIONMANAGER_PATH = "gssi/aq/it/afplibrary/AFPLocationManager";
	private static final String AFPLOCATIONMANAGER_URI = "gssi.aq.it.afplibrary.AFPLocationManager";

	private static final String LOCATIONPROVIDER_PATH = "com/google/android/gms/location/FusedLocationProviderApi";
	private static final String LOCATIONPROVIDER_URI = "com.google.android.gms.location.FusedLocationProviderApi";

    private static final String AFPLOCATIONPROVIDER_PATH = "gssi/aq/it/afplibrary/AFPFusedLocationProviderApi";
    private static final String AFPLOCATIONPROVIDER_URI = "gssi.aq.it.afplibrary.AFPFusedLocationProviderApi";

    private static final String LOCATIONMANAGER_GETLASTKNOWNLOCATION_PATH = "android/location/LocationManager/getLastKnownLocation";
	private static final String LOCATIONMANAGER_REQUESTLOCATIONUPDATES_PATH = "android/location/LocationManager/requestLocationUpdates";
	private static final String LOCATIONMANAGER_REQUESTSINGLEUPDATE_PATH = "android/location/LocationManager/requestSingleUpdate";

	private static final String LOCATIONPROVIDER_GETLASTKNOWNLOCATION_PATH = "com/google/android/gms/location/FusedLocationApi/getLastKnownLocation";
	private static final String LOCATIONPROVIDER_REQUESTLOCATIONUPDATES_PATH = "com/google/android/gms/location/FusedLocationApirequestLocationUpdates";
	private static final String LOCATIONPROVIDER_REQUESTSINGLEUPDATE_PATH = "com/google/android/gms/location/FusedLocationApirequestSingleUpdate";


	public static boolean isLocationInvoke(InvokeInstruction invoke, ConstantPoolGen cpg) {														
		if(invoke.getLoadClassType(cpg).toString().equals(LOCATIONMANAGER_URI)){
			return true;
		}
		if(invoke.getLoadClassType(cpg).toString().equals(LOCATIONPROVIDER_URI)){
			return true;
		}
		return false;
	}
	
	public static boolean isSpecialReplacement(Instruction instr, ConstantPoolGen cpg){
		if(instr.toString(cpg.getConstantPool()).substring(14).startsWith(LOCATIONMANAGER_GETLASTKNOWNLOCATION_PATH)){
			return true;
		}
		if(instr.toString(cpg.getConstantPool()).substring(14).startsWith(LOCATIONMANAGER_REQUESTLOCATIONUPDATES_PATH)){
			return true;
		}
		if(instr.toString(cpg.getConstantPool()).substring(14).startsWith(LOCATIONMANAGER_REQUESTSINGLEUPDATE_PATH)){
			return true;
		}
		if(instr.toString(cpg.getConstantPool()).substring(14).startsWith(LOCATIONPROVIDER_GETLASTKNOWNLOCATION_PATH)){
			return true;
		}
		if(instr.toString(cpg.getConstantPool()).substring(14).startsWith(LOCATIONPROVIDER_REQUESTLOCATIONUPDATES_PATH)){
			return true;
		}
		if(instr.toString(cpg.getConstantPool()).substring(14).startsWith(LOCATIONPROVIDER_REQUESTSINGLEUPDATE_PATH)){
			return true;
		}
		return false;
	}
	
	protected static InvokeInstruction replaceLocationInvoke(InstructionFactory inf, InvokeInstruction instruction, ConstantPoolGen cpg, InstructionList il, String classname){
		InvokeInstruction newInstr = null;
		String methodClassName = instruction.getClassName(cpg);
		String sig = instruction.getSignature(cpg);
		
		if(methodClassName.contains(LOCATIONMANAGER_URI)){
			methodClassName = methodClassName.replace(LOCATIONMANAGER_URI, AFPLOCATIONMANAGER_URI);
		}
		if(methodClassName.contains(LOCATIONPROVIDER_URI)){
			methodClassName = methodClassName.replace(LOCATIONPROVIDER_URI, AFPLOCATIONPROVIDER_URI);
		}

		sig = AFPUtils.replaceIfEqual(sig, LOCATIONMANAGER_PATH, AFPLOCATIONMANAGER_PATH);		

		sig = AFPUtils.replaceIfEqual(sig, LOCATIONPROVIDER_PATH, AFPLOCATIONPROVIDER_URI);

		if( instruction instanceof INVOKESTATIC ){
			newInstr = new INVOKESTATIC(cpg.addMethodref(methodClassName, instruction.getMethodName(cpg), sig));
		}
		if( instruction instanceof INVOKEVIRTUAL ){
			if (isSpecialReplacement(instruction, cpg)){
				insertSpecial(inf, instruction, il, cpg, classname);
				String [] sigPieces =  instruction.getSignature(cpg).split("\\)");
				sig = sigPieces[0] + "[Ljava/lang/String;)" + sigPieces[1];
			}
			
			newInstr = new INVOKESTATIC(cpg.addMethodref(AFPLOCATIONMANAGER_URI, instruction.getMethodName(cpg), "(Landroid/location/LocationManager;" + sig.substring(1)));						
		}
		if( instruction instanceof INVOKESPECIAL ){
			newInstr = new INVOKESPECIAL(cpg.addMethodref(methodClassName, instruction.getMethodName(cpg), sig)); 
		}
		if( instruction instanceof INVOKEINTERFACE){
			// We call directly the AFP Lib, so we replace invokeinterface with invokestatic
			newInstr = new INVOKESTATIC(cpg.addMethodref(AFPLOCATIONPROVIDER_URI, instruction.getMethodName(cpg), "(Lcom/google/android/gms/location/FusedLocationProviderApi;" + sig.substring(1)));
		}
		
		return newInstr;
	}
	
	public static void insertSpecial(InstructionFactory inf, Instruction instruction, InstructionList il, ConstantPoolGen cpg, String classname){
		classname = classname.split("\\$")[0];

        ArrayList<String> params = AFPInstrumenter.ClassFeatureMap.get(classname);
        short paramSize = (short) params.size();
		
		il.insert(instruction, new PUSH(cpg, paramSize));
		il.insert(instruction, new ANEWARRAY(cpg.addClass(Type.STRING)));
		for(int i = 0; i< paramSize; i++){
			il.insert(instruction, InstructionConstants.DUP);
			il.insert(instruction, new PUSH(cpg, i));		
			il.insert(instruction, new PUSH(cpg, params.get(i))); // Feature name
			il.insert(instruction, InstructionConstants.AASTORE);
		}	
	}

}
