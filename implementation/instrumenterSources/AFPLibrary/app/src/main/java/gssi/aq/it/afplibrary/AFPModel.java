package gssi.aq.it.afplibrary;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import org.apache.commons.collections15.map.MultiKeyMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by gianlucascoccia on 26/07/16.
 */
public class AFPModel {

    public static MultiKeyMap<String, Integer> mFRLMap = new MultiKeyMap<String, Integer>();
    public static  Location cachedCity = null;
    public static  Location cachedRegion = null;


    public static boolean canAccessMicrophone(String [] featureNames){
        int canAccess = 0;
        Log.d("AFPLib", mFRLMap.toString());
        for(String s : featureNames){
            Log.d("AFPLIb", "Checking " + s + " Microphone level");
            Integer featureLevel = mFRLMap.get(s, "Microphone");
            if(featureLevel != null){
                canAccess += featureLevel;
            }
        }
        if( canAccess == featureNames.length ){
            return true;
        }
        return false;
    }



    public static boolean canAccessCamera(String [] featureNames){
        int canAccess = 0;
        Log.d("AFPLib", mFRLMap.toString());
        for(String s : featureNames){
            Log.d("AFPLIb", "Checking " + s + " Camera level");
            Integer featureLevel = mFRLMap.get(s, "Camera");
            if(featureLevel != null){
                canAccess += featureLevel;
            }
        }
        if( canAccess == featureNames.length ){
            return true;
        }
        return false;
    }


    public static int getLocationAccessLevel(String [] featureNames){
        int accessLevel = 3;
        if (featureNames != null) {
            for (String s : featureNames) {
                Log.d("AFPLIb", "Checking " + s + " Location level");
                Integer featureLevel = mFRLMap.get(s, "Location");
                if (featureLevel != null) {
                    accessLevel = Math.min(accessLevel, featureLevel);
                    Log.d("AFPLIb", "Access level " + accessLevel);
                }
            }
        }
        return accessLevel;
    }

    public static Location proxyLocation(Location loc, int accessLevel){
        JSONArray addresses = null;


        switch (accessLevel){
            case 3:
                if (loc != null) {
                    Log.d("AFPLib", "FULL ACCESS -> " + loc.toString());
                } else {
                    Log.d("AFPLib", "FULL ACCESS -> Location is Null");
                }
                return loc;

            case 2:
                if(cachedCity == null){
                    try {
                        addresses = reverseGeocode(loc);
                        loc = geocode(findFirst(addresses, "administrative_area_level_1").getString("long_name"));
                        cachedCity = loc;
                        } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    loc = cachedCity;
                }

                Log.d("AFPLib", "Response was: " + addresses);
                if(loc != null) {
                    Log.d("AFPLib", "CITY LEVEL ACCESS -> " + loc.toString());
                } else {
                    Log.d("AFPLib", "CITY LEVEL ACCESS -> Location is Null");
                }
                return loc;

            case 1:
                if (cachedRegion == null){
                    try {
                        addresses = reverseGeocode(loc);
                        loc = geocode(findFirst(addresses, "administrative_area_level_3").getString("long_name"));
                        cachedRegion = loc;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    loc = cachedRegion;
                }

                Log.d("AFPLib", "Response was: " + addresses);
                if(loc != null) {
                    Log.d("AFPLib", "REGION LEVEL ACCESS -> " + loc.toString());
                } else {
                    Log.d("AFPLib", "REGION LEVEL ACCESS -> Location is Null");
                }
                return loc;
            default:
                loc.setLatitude(0);
                loc.setLongitude(0);
                Log.d("AFPLib", "NO ACCESS -> " + loc.toString());
                return loc;
        }
    }

    public static LocationResult proxyLocation(LocationResult loc, int accessLevel){
        JSONArray addresses = null;
        ArrayList<Location> locations = new ArrayList<Location>();

        switch (accessLevel){
            case 3:
                Log.d("AFPLib", "FULL ACCESS -> " + loc.toString());
                return loc;

            case 2:
                try {
                    for(Location l : loc.getLocations()){
                        addresses = reverseGeocode(l);
                        l = geocode(findFirst(addresses, "administrative_area_level_1").getString("long_name"));
                        locations.add(l);
                    }
                    loc = LocationResult.create(locations);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d("AFPLib", "Response was: " + addresses);
                Log.d("AFPLib", "CITY LEVEL ACCESS -> " + loc.toString());
                return loc;

            case 1:
                try {
                for(Location l : loc.getLocations()){
                    addresses = reverseGeocode(l);
                    l = geocode(findFirst(addresses, "administrative_area_level_3").getString("long_name"));
                    locations.add(l);
                }
                loc = LocationResult.create(locations);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

                Log.d("AFPLib", "Response was: " + addresses);
                Log.d("AFPLib", "REGION LEVEL ACCESS -> " + loc.toString());
                return loc;
            default:
                loc.getLastLocation().setLatitude(0);
                loc.getLastLocation().setLongitude(0);
                Log.d("AFPLib", "NO ACCESS -> " + loc.toString());
                return loc;
        }
    }

    public static JSONArray reverseGeocode (Location loc) throws HttpRequest.HttpRequestException, JSONException, ExecutionException, InterruptedException {
        return (JSONArray) new ReverseGeocode().execute(loc).get();
    }

    public static Location geocode (final String address) throws HttpRequest.HttpRequestException, JSONException, ExecutionException, InterruptedException {
        return (Location) new Geocode().execute(address).get();
    }

    public static JSONObject findFirst(JSONArray array, String type) throws JSONException {
        JSONObject result = null;
        for(int i = 0; i < array.length(); i++){
            String types =  ((JSONObject) array.get(i)).get("types").toString();
            if(types.contains(type)){
                return (JSONObject) array.get(i);
            }
        };
        return  result;
    }
}
