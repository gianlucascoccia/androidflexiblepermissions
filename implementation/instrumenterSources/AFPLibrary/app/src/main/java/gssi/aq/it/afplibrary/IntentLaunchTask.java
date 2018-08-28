package gssi.aq.it.afplibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import static gssi.aq.it.afplibrary.AFPIntentHandler.AFP_REQUEST_CODE;
import static gssi.aq.it.afplibrary.AFPIntentHandler.GetConfigIntent;

public class IntentLaunchTask extends AsyncTask<Object, Void, Integer> {

    protected Integer doInBackground(Object... params) {
        Activity originatingActivity = (Activity) params[0];
        String mapping = (String) params[1];

        Log.d("AFPLib", "Firing permission config Intent!");
        originatingActivity.startActivityForResult(GetConfigIntent(originatingActivity, mapping), AFP_REQUEST_CODE);

        return new Integer(0);
    }

}
