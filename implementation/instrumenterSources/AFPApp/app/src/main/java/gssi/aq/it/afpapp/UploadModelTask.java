package gssi.aq.it.afpapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UploadModelTask extends AsyncTask<String, Void, Integer> {

    String baseUrl="http://fragoel2.pythonanywhere.com/store-mapping/";

//    <appname>/<mapping>

    protected Integer doInBackground(String... params) {
        String appname = params[0];
        String mapping = params[1];

        try {
            URL url = new URL(baseUrl + "/" + mapping + "/" + appname);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            Log.d("AFPApp", "Response from server " + conn.getResponseCode());

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Integer(0);
    }

}