import java.util.ArrayList;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.AllocationInstruction;
import org.apache.bcel.generic.ArrayType;
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
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.Type;

public class RecorderInstrumenter {
	
	public static final String MEDIARECORDER_URI  = "android.media.MediaRecorder";
	public static final String MEDIARECORDER_PATH = "android/media/MediaRecorder";
													
	public static final String AFPRECORDER_URI    = "gssi.aq.it.afplibrary.AFPRecorder";
	public static final String AFPRECORDER_PATH    = "Lgssi/aq/it/afplibrary/AFPRecorder;";
	
	public static final String MEDIARECORDER_START_SIG = "android/media/MediaRecorder/start";
	

	public static boolean isMediaRecorderInvoke(InvokeInstruction invoke, ConstantPoolGen cpg) {
		if(invoke.getLoadClassType(cpg).toString().equals(MEDIARECORDER_URI)){
			return true;
		}
		return false;
	}
	
	public static boolean isSpecialReplacement(Instruction instr, ConstantPoolGen cpg){
		if(instr.toString(cpg.getConstantPool()).substring(14).startsWith(MEDIARECORDER_START_SIG)){
			return true;
		}
		return false;
	}
	
	protected static InvokeInstruction replaceMediaRecorderInvoke(InstructionFactory inf, InvokeInstruction instruction, ConstantPoolGen cpg, InstructionList il, String classname){
		InvokeInstruction newInstr = null;
		String methodClassName = instruction.getClassName(cpg);
		String sig = instruction.getSignature(cpg);

		sig = AFPUtils.replaceIfEqual(sig, MEDIARECORDER_PATH, AFPRECORDER_PATH);
		
		if(methodClassName.contains(MEDIARECORDER_URI)){
			methodClassName = methodClassName.replace(MEDIARECORDER_URI, AFPRECORDER_URI);
		}
		
		if( instruction instanceof INVOKESTATIC ){
			newInstr = new INVOKESTATIC(cpg.addMethodref(methodClassName, instruction.getMethodName(cpg), instruction.getSignature(cpg))); 
		}
		if( instruction instanceof INVOKEVIRTUAL ){
			if (isSpecialReplacement(instruction, cpg)){
				insertSpecial(inf, instruction, il, cpg, classname);
				String [] sigPieces =  instruction.getSignature(cpg).split("\\)");
				sig = sigPieces[0] + "[Ljava/lang/String;)" + sigPieces[1];
			} 
			newInstr = new INVOKESTATIC(cpg.addMethodref(AFPRECORDER_URI, instruction.getMethodName(cpg), "(Landroid/media/MediaRecorder;" + sig.substring(1))); 	
		}
		if( instruction instanceof INVOKESPECIAL ){
			newInstr = new INVOKESPECIAL(cpg.addMethodref(instruction.getClassName(cpg), instruction.getMethodName(cpg), instruction.getSignature(cpg))); 
		}
		if( instruction instanceof INVOKEINTERFACE){
			newInstr = new INVOKEINTERFACE(cpg.addMethodref(instruction.getClassName(cpg), instruction.getMethodName(cpg), sig), instruction.getArgumentTypes(cpg).length); 
		}
		
//		DEBUG
//		System.out.println("OLD ->" + instruction.toString(cpg.getConstantPool()));
//		System.out.println("NEW ->" + newInstr.toString(cpg.getConstantPool()));
		
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
