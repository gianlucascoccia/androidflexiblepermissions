import java.util.ArrayList;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.AllocationInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.Type;

public class LocationServiceInstrumenter {
	
public static final String LOCATIONPROVIDER_URI = "com.google.android.gms.location.FusedLocationProviderApi";
public static final String LOCATIONPROVIDER_PATH = "com/google/android/gms/location/FusedLocationProviderApi";

public static final String AFPLOCATION_URI    = "gssi.aq.it.afplibrary.AFPLocationService";
public static final String AFPLOCATION_PATH    = "Lgssi/aq/it/afplibrary/AFPLocationService;";

public static final String LOCATIONSERVICE_GETLASTLOCATION_PATH    		 = "com/google/android/gms/location/FusedLocationProviderApi/getLastLocation";
public static final String LOCATIONSERVICE_REQUESTLOCATIONUPDATES_PATH    = "com/google/android/gms/location/FusedLocationProviderApi/requestLocationUpdates";

protected static InvokeInstruction replaceLocationInvoke(InstructionFactory inf, InvokeInstruction instruction, ConstantPoolGen cpg, InstructionList il, String classname){
		InvokeInstruction newInstr = null;
		
		if( instruction instanceof INVOKESTATIC ){
			newInstr = new INVOKESTATIC(cpg.addMethodref(AFPLOCATION_URI, instruction.getMethodName(cpg), instruction.getSignature(cpg)));
		}
		if( instruction instanceof INVOKEVIRTUAL ){
			newInstr = new INVOKEVIRTUAL(cpg.addMethodref(AFPLOCATION_URI, instruction.getMethodName(cpg), instruction.getSignature(cpg))); 	
		}
		if( instruction instanceof INVOKESPECIAL ){
			newInstr = new INVOKESPECIAL(cpg.addMethodref(AFPLOCATION_URI, instruction.getMethodName(cpg), instruction.getSignature(cpg))); 
		}
		if( instruction instanceof INVOKEINTERFACE){	
			
			
			if (isSpecialReplacement(instruction, cpg)){		
				insertSpecial(inf, instruction, il, cpg, classname);
				String [] sigPieces =  instruction.getSignature(cpg).split("\\)");
				String newSig = "(Ljava/lang/Object;" + sigPieces[0].substring(1) + "[Ljava/lang/String;)" + sigPieces[1];
				newInstr = new INVOKESTATIC(cpg.addMethodref(AFPLOCATION_URI, instruction.getMethodName(cpg), newSig));
				
			} else {
				
			newInstr = new INVOKESTATIC(cpg.addMethodref(AFPLOCATION_URI, instruction.getMethodName(cpg), "(Ljava/lang/Object;" + instruction.getSignature(cpg).substring(1)));
			}
		}
		
		return newInstr;
	}
	
	public static boolean isLocationNew(Instruction instr, ConstantPoolGen cpg) {
		if(instr.toString(cpg.getConstantPool()).endsWith(LOCATIONPROVIDER_PATH)){
			return true;
		}
		return false;
	}
	
	public static boolean isLocationInvoke(InvokeInstruction invoke, ConstantPoolGen cpg) {
		if(invoke.getLoadClassType(cpg).toString().equals(LOCATIONPROVIDER_URI)){
			return true;
		}
		return false;
	}
	
	public static boolean isLocationFieldInstruction(FieldInstruction instr, ConstantPoolGen cpg) {
		if(instr.getFieldType(cpg).toString().equals(LOCATIONPROVIDER_URI)){
			return true;
		}
		return false;
	}
	
	public static boolean isSpecialReplacement(Instruction instr, ConstantPoolGen cpg){
		if(instr.toString(cpg.getConstantPool()).substring(16).startsWith(LOCATIONSERVICE_GETLASTLOCATION_PATH)){
			return true;
		}
		if(instr.toString(cpg.getConstantPool()).substring(16).startsWith(LOCATIONSERVICE_REQUESTLOCATIONUPDATES_PATH)){
			return true;
		}
		return false;
	}
	
	public static void insertSpecial(InstructionFactory inf, Instruction instruction, InstructionList il, ConstantPoolGen cpg, String classname){
		classname = classname.split("\\$")[0];
		
		System.out.println(classname);
		
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
