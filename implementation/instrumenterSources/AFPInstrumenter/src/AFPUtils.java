import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

public class AFPUtils {
	
	protected static void listClassFiles(String directoryName, ArrayList<File> files) {
	    File directory = new File(directoryName);

	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	        	if(file.getName().endsWith(".class")){
	        		files.add(file);
	        	}
	        } else if (file.isDirectory()) {
	        	if(!file.getName().endsWith("android") && !file.getName().endsWith("google") 
	        			&& !file.getName().endsWith("afplibrary") && !file.getName().endsWith("apache")){
	        		listClassFiles(file.getAbsolutePath(), files);
	        	}
	        }
	    }
	}

	// build a map with key = feature, value = class
	protected static void buildFeatureClassMap(Map<String, ArrayList<String>> map, String fileName) throws IOException {
	    File featureFile = new File(fileName);
	    FileInputStream fin = new FileInputStream(featureFile);
	    
	    //Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] splitres = line.split(":");
			ArrayList<String> implementationFiles = new ArrayList<String>();
			for(String item :  splitres[1].split(",")){
				implementationFiles.add(item.trim());
			}
			map.put(splitres[0], implementationFiles);
		}
	    fin.close();

	}
	
	// build a map with key = class, value = feature
		protected static void buildClassFeatureMap(Map<String, ArrayList<String>> map, String fileName) throws IOException {
		    File featureFile = new File(fileName);
		    FileInputStream fin = new FileInputStream(featureFile);
		    
		    //Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] splitres = line.split(":");
				for(String item :  splitres[1].split(",")){
					String className = item.trim(); 
					ArrayList<String> features = map.get(className);
					if (features == null) {
						features = new ArrayList<String>();
						}
					features.add(splitres[0].trim());
		            map.put(className, features);
				}
			}
		    fin.close();

		}
		
	// update a map with key = class, value = resource
	protected static void buildClassResourceMap(Map<String, ArrayList<String>> map, String fileName, ConstantPoolGen cpg){
		ArrayList<String> usedAPI = new ArrayList<String>();
		File classFile = new File(fileName);


		if( cpg.lookupUtf8("Landroid/media/MediaRecorder;") != -1 ){
			usedAPI.add("Microphone");
		}
		if( cpg.lookupUtf8("Landroid/hardware/Camera;") != -1 ){
			usedAPI.add("Camera");
		}
		if( cpg.lookupUtf8("Landroid/location/Location;") != -1 ){
			usedAPI.add("Location");
		}
		if( cpg.lookupUtf8("Landroid/location/LocationManager;") != -1 ){
            usedAPI.add("Location");
		}
        if( cpg.lookupUtf8("android/location/LocationManager") != -1 ){
            usedAPI.add("Location");
        }
		if( cpg.lookupUtf8("Lcom/google/android/gms/location/FusedLocationProviderApi;") != -1 ){
			usedAPI.add("Location");
        }
		// cpg.lookupUtf8("Landroid/provider/ContactsContract;") // Check if this lookup works
		map.put(classFile.getName().split("\\.")[0], usedAPI);
	}
		
		
	// build a map with key = feature, value = resource
	protected static void buildFeatureResourceMap(Map<String, ArrayList<String>> map, Map<String, ArrayList<String>> FeatureMap, Map<String, ArrayList<String>> ClassMap){
		for( String feature : FeatureMap.keySet() ){
			ArrayList<String> featureResources = new ArrayList<String>();
            if ( feature != null ){
				for(String _class : FeatureMap.get(feature)) {
					if(_class != null ){
                        if(ClassMap.get(_class) != null){
                            for (String classResources : ClassMap.get(_class)){
                                if(classResources != null) {
                                    if (!featureResources.contains(classResources)) {
                                        featureResources.add(classResources);
                                    }
                                }
                            }
                        }
					}
				}	
			}
			map.put(feature, featureResources);
		}

	}
	
	// Print the code of all class methods
	protected static void printMethodsCode(ConstantPool c, ConstantPoolGen cpg, ClassGen cg) {
		for (Method method : cg.getMethods()) {
			String methodName = method.getName();
			System.out.println("Method: " + methodName);

			MethodGen mg = new org.apache.bcel.generic.MethodGen(method, methodName, cpg);
			InstructionList il = mg.getInstructionList();
			InstructionHandle[] ihs = il.getInstructionHandles();
			System.out.println("---------------------------------------------");
			for (int i = 0; i < ihs.length; i++) {
				InstructionHandle ih = ihs[i];
				Instruction instr = ih.getInstruction();
				String pr = instr.toString(c);
				System.out.println(pr);
			}
			System.out.println("---------------------------------------------");
		}
	}

	// Print some high level class information
	protected static void printClassInfo(JavaClass jc) {

		System.out.println("Class: " + jc.getClassName());
		System.out.println("  Fields:");
		for (Field field : jc.getFields()) {
			System.out.println("    " + field);
		}
		System.out.println("  Methods:");
		for (Method method : jc.getMethods()) {
			System.out.println("    " + method);
		}
	}
	
	// Print the constant pool
	protected static void printConstantPool(ConstantPool c) {
		System.out.println("*********************************************************************************************");
		System.out.println("Constant Pool:");
		System.out.println(c.toString());
		System.out.println("*********************************************************************************************");
	}
	
	protected static String replaceIfEqual(String original, String toReplace, String substitution){
		int lastFoundIndex = 0;
		while(lastFoundIndex != -1){
			lastFoundIndex = original.indexOf(toReplace, lastFoundIndex + 1);
			if(lastFoundIndex != -1){
				if(original.charAt(lastFoundIndex + toReplace.length()) != '$'){
					original = original.substring(0, lastFoundIndex) + substitution + original.substring(lastFoundIndex + toReplace.length());
				}
			}
		}
		return original;
	}

	public static void buildFakeClassFeatureMap(Map<String, ArrayList<String>> classFeatureMap, ArrayList<File> classfiles) {
		Iterator<File> I = classfiles.iterator();
		while(I.hasNext()){
			File currentFile = I.next();
			String currentFileName =  currentFile.getName().split("\\.")[0].split("\\$")[0];
			if(!classFeatureMap.containsKey(currentFileName)){
				ArrayList<String> f = new ArrayList<String>();
				f.add(currentFileName);
				classFeatureMap.put(currentFileName, f);
			}
		}
	}
	
	public static void buildFakeFeatureClassMap(Map<String, ArrayList<String>> classFeatureMap, ArrayList<File> classfiles) {
		Iterator<File> I = classfiles.iterator();
		while(I.hasNext()){
			File currentFile = I.next();
			String currentFileName =  currentFile.getName().split("\\.")[0].split("\\$")[0];
			if(!classFeatureMap.containsKey(currentFileName)){
				ArrayList<String> f = new ArrayList<String>();
				f.add(currentFileName);
				classFeatureMap.put(currentFileName, f);
			}
		}
	}

}
