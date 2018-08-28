package gssi.aq.it.afplibrary;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gianlucascoccia on 15/08/16.
 */
public class ReverseGeocode extends AsyncTask {

    @Override
    protected JSONArray doInBackground(Object[] params) {
        JSONObject response = null;
        JSONArray result = null;

        try {

            response = new JSONObject(HttpRequest.get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                    ((Location) params[0]).getLatitude() +
                    "," + ((Location) params[0]).getLongitude() +
                    "&key=AIzaSyAdCKGdX3hFgu1GIXQoW1VujtWRAlkV8CA").body());

            result = response.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
        }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected JSONArray onPostExecute(JSONArray result) {
        return result;
    }
}

