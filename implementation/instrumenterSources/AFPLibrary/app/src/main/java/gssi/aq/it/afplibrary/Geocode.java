package gssi.aq.it.afplibrary;

import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gianlucascoccia on 15/08/16.
 */
public class Geocode extends AsyncTask{

    @Override
    protected Location doInBackground(Object[] params) {
        JSONObject response = null;
        Location r = new Location("");

        try {
            response = (JSONObject) new JSONObject(HttpRequest.get("https://maps.googleapis.com/maps/api/geocode/json?address=" +
                    params[0] +
                    "&key=AIzaSyAdCKGdX3hFgu1GIXQoW1VujtWRAlkV8CA").body());

            response = ((JSONObject) response.getJSONArray("results").get(0)).getJSONObject("geometry").getJSONObject("location");

            r.setLatitude(response.getDouble("lat"));
            r.setLongitude(response.getDouble("lng"));
            return r;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return r;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected Location onPostExecute(Location result) {
        return result;
    }


}
