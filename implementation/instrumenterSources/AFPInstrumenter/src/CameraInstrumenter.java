import org.apache.bcel.classfile.Attribute;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;

import javax.lang.model.element.TypeParameterElement;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
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
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.Type;

public class CameraInstrumenter {
	
	final static String CAMERA_URI = "android.hardware.Camera";
	final static String CAMERA_PATH = "android/hardware/Camera";
	
	final static String AFPCAMERA_URI = "gssi.aq.it.afplibrary.AFPCamera";
	final static String AFPCAMERA_PATH = "gssi/aq/it/afplibrary/AFPCamera";
	
	final static String CAMERA_TAKE_PICTURE_SIG = "android/hardware/Camera/takePicture";
	
	public static boolean isCameraInvoke(InvokeInstruction invoke, ConstantPoolGen cpg) {	
		if(invoke.getLoadClassType(cpg).toString().equals(CAMERA_URI)){
			return true;
		}
		return false;
	}
	
	public static boolean isSpecialReplacement(Instruction instr, ConstantPoolGen cpg){
		if(instr.toString(cpg.getConstantPool()).substring(14).startsWith(CAMERA_TAKE_PICTURE_SIG)){
			return true;
		}
		return false;
	}
	
	protected static InvokeInstruction replaceCameraInvoke(InstructionFactory inf, InvokeInstruction instruction, ConstantPoolGen cpg, InstructionList il, String classname){
		InvokeInstruction newInstr = null;
		String methodClassName = instruction.getClassName(cpg);
		String sig = instruction.getSignature(cpg);
		
		sig = AFPUtils.replaceIfEqual(sig, CAMERA_PATH, AFPCAMERA_PATH);
		
		if(methodClassName.contains(CAMERA_URI)){
			methodClassName = methodClassName.replace(CAMERA_URI, AFPCAMERA_URI);
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
			
			newInstr = new INVOKESTATIC(cpg.addMethodref(AFPCAMERA_URI, instruction.getMethodName(cpg), "(Landroid/hardware/Camera;" + sig.substring(1)));	
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
