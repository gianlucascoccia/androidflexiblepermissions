import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;
import org.fusesource.jansi.AnsiConsole;


public class AFPInstrumenter {

	public static Map<String, ArrayList<String>> FeatureClassMap = new HashMap<String, ArrayList<String>>();
	public static Map<String, ArrayList<String>> ClassFeatureMap = new HashMap<String, ArrayList<String>>();
	public static Map<String, ArrayList<String>> ClassResourceMap = new HashMap<String, ArrayList<String>>();
	public static Map<String, ArrayList<String>> FeatureResourceMap = new HashMap<String, ArrayList<String>>();

	private static Map<String, InstructionHandle> positions = new HashMap<>();

	
	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		if (args.length < 3) {
			System.out.println(ansi().fg(RED).a("Usage: Instrumenter ClassFilesFolder FeatureList MainActivityFileName").reset());
			return;
		}

        File f = new File(args[1]);
		Boolean hasFeatureFile = f.exists() && !f.isDirectory();
        System.out.println("Feature file exists: " + hasFeatureFile);

		try {
			ArrayList<File> classFiles = new ArrayList<File>();
			String[] mainActivityPieces = args[2].split("\\.");
			String mainActivityName = mainActivityPieces[mainActivityPieces.length -1] + ".class";
			AFPUtils.listClassFiles(args[0], classFiles);
			Preprocess(args[1], classFiles, hasFeatureFile);
			Iterator<File> i = classFiles.iterator();
            String appName =  args[0].split("-")[0];

            while (i.hasNext()) {
				File item = i.next();
				// We need to add some things to the main activity
                if(item.getName().equalsIgnoreCase(mainActivityName)){
					instrumentMainActivity(item, appName);
				}
				
				// instrument relevant classes
				String className = item.getName().split("\\.")[0];
				if(className.contains("$")){
					className = className.split("\\$")[0];
				}
				ParseClass(item.getAbsolutePath());
			}
		} catch (ClassFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TargetLostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AnsiConsole.systemUninstall();
	}

	// Buil all the hashmaps
	protected static void Preprocess(String FeatureFilePath, ArrayList<File> classFiles, boolean hasFeatureFile) throws IOException {
		
		if(!hasFeatureFile){
			//We don't have the feature file so let's generate the features on the fly!
			AFPUtils.buildFakeFeatureClassMap(FeatureClassMap, classFiles);
			AFPUtils.buildFakeClassFeatureMap(ClassFeatureMap, classFiles);
		} else {
			//Feature file exists so we can use that
			AFPUtils.buildFeatureClassMap(FeatureClassMap, FeatureFilePath);
			AFPUtils.buildClassFeatureMap(ClassFeatureMap, FeatureFilePath);
		}
		Iterator<File> i = classFiles.iterator();
		while(i.hasNext()){
			File item = i.next();
			String classFileName = item.getName().split("\\.")[0];
			org.apache.bcel.classfile.ClassParser p = new org.apache.bcel.classfile.ClassParser(item.getAbsolutePath());
			JavaClass jc = p.parse();
			ConstantPoolGen cpg = new org.apache.bcel.generic.ConstantPoolGen(jc.getConstantPool());
			AFPUtils.buildClassResourceMap(ClassResourceMap, classFileName, cpg);
		}
	
		AFPUtils.buildFeatureResourceMap(FeatureResourceMap ,FeatureClassMap, ClassResourceMap);

//        System.out.println("### Feat -> Class ###");
//        for (String s : FeatureClassMap.keySet()){
//            if (FeatureClassMap.get(s).size() > 0){
//			    System.out.println(s + " --> " + FeatureClassMap.get(s));
//            }
//        }
//
//        System.out.println("### Class -> Feat ###");
//        for (String s : ClassFeatureMap.keySet()){
//            if (ClassFeatureMap.get(s).size() > 0){
//                System.out.println(s + " --> " + ClassFeatureMap.get(s));
//            }
//        }
//
//        System.out.println("### Class -> Res ###");
//        for (String s : ClassResourceMap.keySet()){
//            if (ClassResourceMap.get(s).size() > 0){
//                System.out.println(s + " --> " + ClassResourceMap.get(s));
//            }
//        }
//
//        System.out.println("### Feat -> Res ###");
//        for (String s : FeatureResourceMap.keySet()){
//            if (FeatureResourceMap.get(s).size() > 0){
//                System.out.println(s + " --> " + FeatureResourceMap.get(s));
//            }
//        }
		
	}
	
	protected static void instrumentMainActivity(File mainActivity, String appName) throws IOException, TargetLostException {
        org.apache.bcel.classfile.ClassParser p = new org.apache.bcel.classfile.ClassParser(mainActivity.getAbsolutePath());
        JavaClass jc = p.parse();
        ClassGen cg = new org.apache.bcel.generic.ClassGen(jc);
        ConstantPoolGen cpg = new org.apache.bcel.generic.ConstantPoolGen(jc.getConstantPool());
        System.out.println(ansi().fg(GREEN).a("** INSTRUMENTING MAIN ACTIVITY **").reset());


        for (Method m : cg.getMethods()) {
            if (m.getName().endsWith("onCreate")) {
                System.out.println("- Modifying onCreate method");
                MethodGen newMethod = new MethodGen(m, m.getName(), cpg);
                InstructionList il = newMethod.getInstructionList();
                initPositions(il);


				// Append the intent launch
				InstructionList newHandles = IntentInstrumenter.addIntentTrigger(jc.getClassName(), FeatureResourceMap, cg, cpg);
				InstructionHandle nih = il.insert(positions.get(appName), newHandles);
				il.redirectBranches(il.getEnd(), nih);


				il.setPositions();
                newMethod.setInstructionList(il);
                newMethod.removeLocalVariables();
//                newMethod.removeLineNumbers();
                newMethod.setMaxLocals();
                newMethod.setMaxStack();
                cg.replaceMethod(m, newMethod.getMethod());
                cg.update();
                il.dispose();
                JavaClass newClass = cg.getJavaClass();
                newClass.setConstantPool(cpg.getFinalConstantPool());

                try {
                    newClass.dump(mainActivity.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // Check if a onActivityResult method exists and modify it
        boolean hasActivityResult = false;
        for (Method m : cg.getMethods()) {
            if (m.getName().endsWith("onActivityResult")) {
                System.out.println("- Modifying existing onActivityResult");
                hasActivityResult = true;
                MethodGen newMethod = new MethodGen(m, m.getName(), cpg);
                InstructionList il = newMethod.getInstructionList();
                il.insert(il.getStart(), IntentInstrumenter.addIntentResponse(cpg, il));
                il.setPositions();
                newMethod.setInstructionList(il);
                newMethod.removeLocalVariables();
                newMethod.removeLineNumbers();
                newMethod.setMaxLocals();
                newMethod.setMaxStack();
                cg.replaceMethod(m, newMethod.getMethod());
                cg.update();
                il.dispose();
                JavaClass newClass = cg.getJavaClass();
                newClass.setConstantPool(cpg.getFinalConstantPool());

                try {
                    newClass.dump(mainActivity.getAbsolutePath());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        // OnActivityResult does not exist, add it!
		if(!hasActivityResult){
			System.out.println("- Adding OnActivityResult");
			Type[] argumentsType = {Type.INT, Type.INT, Type.getType("Landroid/content/Intent;")};
			String[] argumentsNames = {"requestCode", "resultCode", "data"};
			MethodGen newMethod = new MethodGen((int) Constants.ACC_PROTECTED, (Type) Type.VOID, argumentsType, argumentsNames, "onActivityResult", mainActivity.getName(), 
					IntentInstrumenter.newIntentResponse(cg, cpg), cpg);
			newMethod.removeLocalVariables();
			newMethod.removeLineNumbers();
		    newMethod.setMaxLocals();
		    newMethod.setMaxStack();
		    cg.addMethod(newMethod.getMethod());
			cg.update();
		    JavaClass newClass = cg.getJavaClass();
			newClass.setConstantPool(cpg.getFinalConstantPool());
			
			try {
				newClass.dump(mainActivity.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Add fake onCreate
        System.out.println("- Adding fakeOnCreate");
        Type[] argumentsType = {Type.getType("Landroid/os/Bundle;")};
        String[] argumentsNames = {"paramBundle"};
        MethodGen newMethod = new MethodGen((int) Constants.ACC_PROTECTED, (Type) Type.VOID, argumentsType, argumentsNames, "fakeOnCreate", mainActivity.getName(),
                IntentInstrumenter.newFakeOnCreate(cg, cpg), cpg);
        newMethod.removeLocalVariables();
        newMethod.removeLineNumbers();
        newMethod.setMaxLocals();
        newMethod.setMaxStack();
        cg.addMethod(newMethod.getMethod());
        cg.update();
        JavaClass newClass = cg.getJavaClass();
        newClass.setConstantPool(cpg.getFinalConstantPool());

        try {
            newClass.dump(mainActivity.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


	protected static void ParseClass(String classFilePath) throws ClassFormatException, IOException {
		org.apache.bcel.classfile.ClassParser p = new org.apache.bcel.classfile.ClassParser(classFilePath);
		JavaClass jc = p.parse();
		ClassGen cg = new org.apache.bcel.generic.ClassGen(jc);
		ConstantPool c = jc.getConstantPool();
		ConstantPoolGen cpg = new org.apache.bcel.generic.ConstantPoolGen(jc.getConstantPool());
		File classFile = new File(classFilePath);
		String classFileName = classFile.getName().split("\\.")[0];

		insertInstrumentation(cpg, cg, c);
		JavaClass newClass = cg.getJavaClass();
		newClass.setConstantPool(cpg.getFinalConstantPool());
		try {
			newClass.dump(classFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// Method that inserts the instrumentation into the code
	protected static void insertInstrumentation(ConstantPoolGen cpg, ClassGen cg, ConstantPool c) {
		InstructionFactory inf = new InstructionFactory(cg);

		for (Method method : cg.getMethods()) {
			if(!method.isAbstract()){
				String methodName = method.getName();
                MethodGen mg = new org.apache.bcel.generic.MethodGen(method, methodName, cpg);
				String fullyQualifiedName = cg.getClassName();
				String[] classPieces = cg.getClassName().split("\\.");
				String classname = classPieces[classPieces.length - 1 ];
				InstructionList il = mg.getInstructionList();
				if(il != null){
					InstructionHandle[] ihs = il.getInstructionHandles();
					
					for (int i = 0; i < ihs.length; i++) {
						InstructionHandle ih = ihs[i];
						Instruction instr = ih.getInstruction();
						
						// Replace method calls
						if (instr instanceof InvokeInstruction) {
							replaceInvoke(inf, (InvokeInstruction) instr, ih, cpg, il, classname, fullyQualifiedName);
						}
					}
					
					il.setPositions();
					mg.setInstructionList(il);
					mg.removeLocalVariables();
					mg.removeLineNumbers();
					mg.setMaxLocals();
					mg.setMaxStack();
					cg.replaceMethod(method, mg.getMethod());
					cg.update();
					
					il.dispose();
				}
			}
		}
	}

	protected static void replaceInvoke(InstructionFactory inf, InvokeInstruction instruction, InstructionHandle ih,
										ConstantPoolGen cpg, InstructionList il, String classname, String fullyQualifiedName) {
		
		InvokeInstruction newInvoke = null;
		if (RecorderInstrumenter.isMediaRecorderInvoke(instruction, cpg)) {
			newInvoke = RecorderInstrumenter.replaceMediaRecorderInvoke(inf, instruction, cpg, il, classname);
		}
		if (LocationInstrumenter.isLocationInvoke(instruction, cpg)){
			newInvoke = LocationInstrumenter.replaceLocationInvoke(inf, instruction, cpg, il, classname);
		}
//		if (LocationServiceInstrumenter.isLocationInvoke(instruction, cpg)){
//			newInvoke = LocationServiceInstrumenter.replaceLocationInvoke(inf, instruction, cpg, il, classname);
//		}
		if(CameraInstrumenter.isCameraInvoke(instruction, cpg)) {
			newInvoke = CameraInstrumenter.replaceCameraInvoke(inf, instruction, cpg, il, classname);
		}
		
		if (newInvoke != null){
			Instruction i = (Instruction) newInvoke;
			ih.setInstruction(newInvoke);			
			System.out.println(ansi()
					.fgDefault().a("\n - Replaced  ").fgYellow().a(instruction.toString(cpg.getConstantPool()) + " \n")
					.fgDefault().a(" with ").fgYellow().a(newInvoke.toString(cpg.getConstantPool()) + " \n")
					.fgDefault().a(" in ").fgYellow().a(fullyQualifiedName + "\n").reset()
			);
		}
	}

	protected static void initPositions(InstructionList il){
	    //Todo detect the position for the intent automatically
		positions.put("fr.inria.es.electrosmart", il.getStart());
		positions.put("SplashActivity.class", il.getEnd());
		positions.put("WelcomeActivity.class", il.getEnd());
        positions.put("com.yopapp.yop", il.getStart());
	}

	
//	#==========================================================================#
//	+	Code for debugging, prints stack size of a method on every instruction +
//	#==========================================================================#
	
//	int stackSize = 0;
//	if(jc.getClassName().contains("LocationActivity")){
//		for(Method m : jc.getMethods()){
//			if(m.getName().contains("onConnected")){
//				MethodGen newMethod = new MethodGen(m, m.getName(), cpg);
//				InstructionList il = newMethod.getInstructionList();
//				for (Instruction i : il.getInstructions()){
//					System.out.println( i.toString(cpg.getConstantPool()) + " " + i.produceStack(cpg) + " - " + i.consumeStack(cpg)  );
//					stackSize = stackSize + i.produceStack(cpg) - i.consumeStack(cpg);
//				}
//			}
//		}
//	}
//	System.out.println("EndStack = " + stackSize);

}
