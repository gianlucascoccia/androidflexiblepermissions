import java.io.IOException;
import java.io.OutputStream;

import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

public class BCELifier {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		org.apache.bcel.classfile.ClassParser p = new org.apache.bcel.classfile.ClassParser("/Users/gianlucascoccia/Desktop/AFP/instrumenterSources/MyApplication/app/build/intermediates/classes/debug/gssi/aq/it/myapplication/MainActivity.class");
		JavaClass jc = p.parse();
		ClassGen cg = new org.apache.bcel.generic.ClassGen(jc);
		ConstantPool c = jc.getConstantPool();
		ConstantPoolGen cpg = new org.apache.bcel.generic.ConstantPoolGen(jc.getConstantPool());
		
		for(Method m : cg.getMethods()){
			if(m.getName().endsWith("startRecording")){
				System.out.println(m.getName().toString());
				MethodGen newMethod = new MethodGen(m, m.getName(), cpg);
				InstructionList il = newMethod.getInstructionList();
				for (Instruction i : il.getInstructions()){
					System.out.println(i.toString(c));
				}
				
			}
		}
		
		org.apache.bcel.util.BCELifier bc = new org.apache.bcel.util.BCELifier(jc, (OutputStream) System.out);
		bc.start();
		
	}

}
