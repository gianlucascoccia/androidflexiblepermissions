package gssi.aq.it.afplibrary;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by gianlucascoccia on 12/07/16.
 */
public class AFPIntentHandler {

    public static int AFP_REQUEST_CODE = 1000;


    public static void launchConfigIntent(Activity originatingActivity, String mapping) {
            //Launch the intent in background
             new IntentLaunchTask().execute(originatingActivity, mapping);
    }


    public static Intent GetConfigIntent(Activity originatingActivity, String Features ){
        Intent _AFPInt = new Intent();
        _AFPInt.setAction("SET_PERMISSIONS");
        _AFPInt.putExtra("Features", Features);
        _AFPInt.putExtra("Sender", originatingActivity.getClass().getPackage().getName());
        _AFPInt.setType("text/plain");
        return _AFPInt;

    }

    public static void HandleIntentResponse( Intent intent){
        //Somehow store the model that we get as response
        String received = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.d("AFPLib", "Received model");
        Log.d("AFPLib", received);

        Matcher m = Pattern.compile("\\[[A-Za-z\\s]*,\\s[A-Za-z\\s]*\\]=[0-9]+")
                .matcher(received);
        while (m.find()) {
            String[] pieces = m.group().split(",");
            String feature = pieces[0].substring(1, pieces[0].length()).trim();
            String resource = pieces[1].substring(0,pieces[1].length() - 3).trim();
            int level = Integer.valueOf(pieces[1].substring(pieces[1].length() - 1, pieces[1].length()).trim());

            Log.d("AFPLib", "Putting " + feature + " - " + resource + " - " + level);
            AFPModel.mFRLMap.put(feature, resource, level);
        }
        Log.d("AFPLib", AFPModel.mFRLMap.toString());
    }

}
