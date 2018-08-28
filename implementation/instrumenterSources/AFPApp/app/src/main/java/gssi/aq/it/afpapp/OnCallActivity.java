package gssi.aq.it.afpapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.collections15.keyvalue.MultiKey;
import org.apache.commons.collections15.map.MultiKeyMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class OnCallActivity extends PreferenceActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private MultiKeyMap<String, Integer> FRIMap = new MultiKeyMap<String, Integer>();
    private Long startupTimestamp = null;
    private Long returnTimestamp = null;
    private static Map<String, ArrayList<String>> featureResourceMap = null;
    private static MultiKeyMap<String, ListPreference> FRLMap = new MultiKeyMap<String, ListPreference>();
    private static SharedPreferences preferences = null;
    private static String defaultModel = null;
    private static String senderName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startupTimestamp = System.currentTimeMillis();
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new OnCallPreferencesFragment()).commit();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static class OnCallPreferencesFragment extends PreferenceFragment
    {

        @Override
        @TargetApi(Build.VERSION_CODES.M)
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_empty);

            // Get default shared preferences
            Context hostActivity = getActivity();
            preferences = PreferenceManager.getDefaultSharedPreferences(hostActivity);

            Log.d("AFPTEST", "Deafult model: " + preferences.getAll().toString());
            defaultModel = preferences.getAll().toString();

            // Add default
            PreferenceScreen screen = this.getPreferenceScreen();

            Log.d("AFPApp", "Received Intent");

            //Riprendo le features dall'intento
            Intent intent = getActivity().getIntent();
            featureResourceMap = new HashMap<String, ArrayList<String>>();

            //De-serialize
            senderName = intent.getStringExtra("Sender");
            createFeatureResourceMap(intent.getStringExtra("Features"));
            Log.d("AFPApp", featureResourceMap.toString());

            //Create and store the config dialogs for each feature
            createDialogs(screen);

            //Create the submit button
            Preference button = new Preference(getContext());
            button.setWidgetLayoutResource(R.layout.submit_button);
            screen.addPreference(button);
        }

        private void createFeatureResourceMap(String serializedData) {
            Log.d("AFPApp", serializedData);
            serializedData = serializedData.substring(0, serializedData.length() - 1);
            for (String res : serializedData.split(";")) {

                String[] partials = res.split(":");
                String featureName = partials[0];
                ArrayList<String> usedResources = new ArrayList<String>();
                if (partials.length > 1) {
                    usedResources = new ArrayList<String>(Arrays.asList(partials[1].substring(0, partials[1].length()).split(",")));
                }
                featureResourceMap.put(featureName, usedResources);
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        private void createDialogs(PreferenceScreen screen){
            for(String feature : featureResourceMap.keySet()){
                if(!feature.equalsIgnoreCase("R") && !feature.equalsIgnoreCase("BuildConfig")) {
                    if(featureResourceMap.get(feature).size() >0 ) {
                        PreferenceCategory category = new PreferenceCategory(getContext());
                        category.setTitle("Permissions for feature \"" + feature + "\"");
                        screen.addPreference(category);
//                    category.addPreference(createSubtitle());
//                    category.setLayoutResource(R.layout.custom_layout);
                        for (String resource : featureResourceMap.get(feature)) {
                            ListPreference l = createListPreference(resource, feature);
                            category.addPreference(l);
                            FRLMap.put(new MultiKey<String>(feature, resource), l);
                        }
                        setPreferenceScreen(screen);
                    }
                }
            }
        }


        @TargetApi(Build.VERSION_CODES.M)
        private ListPreference createSubtitle() {
            ListPreference listPref = new ListPreference(getContext());
            listPref.setSummary("Set preferences below");
            return listPref;
        }

        @TargetApi(Build.VERSION_CODES.M)
        private ListPreference createListPreference(String resource, String feature) {
            ListPreference listPref = new ListPreference(getContext());
            listPref.setTitle("Preferences for " + resource + " permission");
            if (resource.endsWith("Microphone")) {
                listPref.setDialogTitle("Choose whether to allow/deny access");
                listPref.setEntries(new CharSequence[]{"Allow", "Deny"});
                listPref.setEntryValues(new CharSequence[]{"1", "0"});
                listPref.setValue(preferences.getString("MICROPHONE", "0"));
            }
            if (resource.endsWith("Camera")) {
                listPref.setDialogTitle("Choose whether to allow/deny access");
                listPref.setEntries(new CharSequence[]{"Allow", "Deny"});
                listPref.setEntryValues(new CharSequence[]{"1", "0"});
                listPref.setValue(preferences.getString("CAMERA", "0"));
            }
            if (resource.endsWith("Location")) {
                listPref.setDialogTitle("Choose the level of precision");
                listPref.setEntries(new CharSequence[]{"Complete Location", "Region Only", "City Only", "Deny"});
                listPref.setEntryValues(new CharSequence[]{"3", "2", "1", "0"});
                listPref.setValue(preferences.getString("LOCATION", "0"));
            }
            if (resource.endsWith("Contacts")) {
                listPref.setDialogTitle("Choose whether to allow/deny access");
                listPref.setEntries(new CharSequence[]{"Modify Existing Access", "Read Only Access", "Add New Access", "Deny Access"});
                listPref.setEntryValues(new CharSequence[]{"3", "2", "1", "0"});
                listPref.setValue(preferences.getString("CONTACTS", "0"));
            }
            return listPref;
        }
    }

    @Override
    public void onBackPressed() {
        returnTimestamp = System.currentTimeMillis();

        //Create the model
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        for(String f : featureResourceMap.keySet()){
            for(ArrayList<String> rl : featureResourceMap.values()){
                for(String r : rl){
                    ListPreference listPref = FRLMap.get(f, r);
                    if(listPref != null){
                        FRIMap.put(f, r, Integer.valueOf(listPref.getValue()));
                    }
                }
            }
        }


        Log.d("AFPApp", "Model creation completed, sending it back");

        //Serialize it
        String serializedModel = FRIMap.toString();
        Log.d("AFPApp", serializedModel);

        //Send the model to the server too
        Log.d("AFPApp", "Caller " + senderName);
        new UploadModelTask().execute(AFPUtils.modelToString(startupTimestamp, returnTimestamp, defaultModel, FRIMap), senderName);

        Intent mIntent = new Intent();
        mIntent.putExtra(Intent.EXTRA_TEXT, serializedModel);
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }

    public void onSubmitClick(View view) {
        onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "OnCall Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://gssi.aq.it.afpapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "OnCall Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://gssi.aq.it.afpapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
