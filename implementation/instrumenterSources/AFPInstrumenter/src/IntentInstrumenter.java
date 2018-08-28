import java.util.ArrayList;
import java.util.Map;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.*;

public class IntentInstrumenter {

	private static final String AFP_LIB_NAME = "AFPLib";
	private static final String INTENT_SENT_MESSAGE = "Firing permission config request intent!";
	
	private static final String INTENT_HANDLER_URI = "gssi.aq.it.afplibrary.AFPIntentHandler";
	
	private static final String INTENT_GENERATOR_NAME = "GetConfigIntent";
	private static final String INTENT_GENERATOR_SIG = "(Ljava/lang/String;)Landroid/content/Intent;";
	
	private static final String AFP_REQUEST_CODE = "AFP_REQUEST_CODE";
	private static final String AFP_REQUEST_TYPE = "I";
	
	private static final String INTENT_TRIGGER_NAME = "startActivityForResult";
	private static final String INTENT_TRIGGER_SIG = "(Landroid/content/Intent;I)V";
	
	private static final String INTENT_RECEIVER_NAME = "HandleIntentResponse";
	private static final String INTENT_RECEIVER_SIG = "(Landroid/content/Intent;)V";
	
	private static final String LOG_WRITER_URI = "android.util.Log";
	private static final String LOG_WRITER_NAME = "d";
	private static final String LOG_WRITER_SIG = "(Ljava/lang/String;Ljava/lang/String;)I";


    public static InstructionList addIntentTrigger(String mainActivityName, Map<String, ArrayList<String>> FeatureResourceMap, ClassGen _cg, ConstantPoolGen _cp){
        InstructionList il = new InstructionList();
        InstructionFactory _factory = new InstructionFactory(_cg, _cp);

        //We need a string representation of the Feature->Resource map
        String intent_extra = "";
        for( String key : FeatureResourceMap.keySet()){
            intent_extra += key.trim();
            intent_extra += ":";
            for (String val : FeatureResourceMap.get(key)){
                intent_extra += val;
                intent_extra += ",";
            }
            intent_extra += ";";
        }

        ObjectType ActivityType = new ObjectType("android.app.Activity");
        InstructionHandle ih_0 = il.append(_factory.createLoad(ActivityType, 0));
        il.append(new PUSH(_cp, intent_extra));
        il.append(_factory.createInvoke("gssi.aq.it.afplibrary.AFPIntentHandler", "launchConfigIntent", Type.VOID, new Type[] { ActivityType, Type.STRING }, Constants.INVOKESTATIC));
//        InstructionHandle ih_6 = il.append(_factory.createReturn(Type.VOID));

        return il;
    }


	
	public static InstructionList addIntentResponse(ConstantPoolGen cpg, InstructionList il){
		InstructionList newInstructions = new InstructionList();
		newInstructions.append(new ILOAD(1));
		newInstructions.append(new GETSTATIC(cpg.addFieldref(INTENT_HANDLER_URI, AFP_REQUEST_CODE, AFP_REQUEST_TYPE)));
		BranchHandle if1 = newInstructions.append(new IF_ICMPNE(null));
		newInstructions.append(new ILOAD(2));
		newInstructions.append(new PUSH(cpg, -1));
		BranchHandle if2 = newInstructions.append(new IF_ICMPNE(null));
		newInstructions.append(new ALOAD(3));
		newInstructions.append(new INVOKESTATIC(cpg.addMethodref(INTENT_HANDLER_URI, INTENT_RECEIVER_NAME, INTENT_RECEIVER_SIG)));
		InstructionHandle ih = il.getStart();
		if1.setTarget(ih);
		if2.setTarget(ih);
		return newInstructions;
	}
	
	public static InstructionList newIntentResponse(ClassGen _cg, ConstantPoolGen _cp){
		InstructionList il = new InstructionList();
		InstructionFactory _factory = new InstructionFactory(_cg, _cp);

        il.append(new PUSH(_cp, "DEBUG_AFP"));
        il.append(new PUSH(_cp, "Debug message"));
        il.append(new INVOKESTATIC(_cp.addMethodref(LOG_WRITER_URI, LOG_WRITER_NAME, LOG_WRITER_SIG)));
        il.append( InstructionConstants.POP);

		InstructionHandle ih_0 = il.append(_factory.createLoad(Type.INT, 1));
        il.append(new GETSTATIC(_cp.addFieldref(INTENT_HANDLER_URI, AFP_REQUEST_CODE, AFP_REQUEST_TYPE))); //	il.append(new PUSH(_cp, 1000));
		BranchInstruction if_icmpne_2 = _factory.createBranchInstruction(Constants.IF_ICMPNE, null);
		il.append(if_icmpne_2);
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(new PUSH(_cp, -1));
		BranchInstruction if_icmpne_7 = _factory.createBranchInstruction(Constants.IF_ICMPNE, null);
		il.append(if_icmpne_7);
		InstructionHandle ih_10 = il.append(_factory.createLoad(Type.OBJECT, 3));
        il.append(_factory.createInvoke(INTENT_HANDLER_URI, "HandleIntentResponse", Type.VOID, new Type[] { new ObjectType("android.content.Intent") }, Constants.INVOKESTATIC));
		InstructionHandle ih_20 = il.append(_factory.createReturn(Type.VOID));
		if_icmpne_2.setTarget(ih_20);
		if_icmpne_7.setTarget(ih_20);

		return il;
	}


	public static InstructionList newFakeOnCreate(ClassGen _cg, ConstantPoolGen _cp){
		InstructionList il = new InstructionList();
		InstructionFactory _factory = new InstructionFactory(_cg, _cp);

		il.append(new PUSH(_cp, "DEBUG_AFP"));
		il.append(new PUSH(_cp, "Debug message"));
		il.append(new INVOKESTATIC(_cp.addMethodref(LOG_WRITER_URI, LOG_WRITER_NAME, LOG_WRITER_SIG)));
		il.append( InstructionConstants.POP);

        InstructionHandle ih_20 = il.append(_factory.createReturn(Type.VOID));

		return il;
	}



}
