package gssi.aq.it.afplibrary;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class AFPFusedLocationProviderApi {

    private static String[] _featureNames;

    public static Location getLastLocation (FusedLocationProviderApi f, GoogleApiClient client){
        Log.d("AFPLib", " *** AFPLOCATION GET LAST LOCATION ***");
        Location loc = LocationServices.FusedLocationApi.getLastLocation(client);
        Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
        loc = AFPModel.proxyLocation( loc, AFPModel.getLocationAccessLevel(_featureNames));
        if (loc != null){
            Log.d("AFPLib", "FINAL VALUE = " + loc.getLongitude() +  loc.getLatitude());
        } else {
            Log.d("AFPLib", "FINAL VALUE = Location is null");
        }
        return loc;
    }

    public static PendingResult<Status> requestLocationUpdates(FusedLocationProviderApi f, GoogleApiClient client, LocationRequest request, final com.google.android.gms.location.LocationListener listener) {
        Log.d("AFPLib", " *** AFPLOCATION REQUEST UPDATES 1 ***");
        if (AFPModel.getLocationAccessLevel(_featureNames) == 3) {
            return f.requestLocationUpdates(client, request, listener);
        } else {
            com.google.android.gms.location.LocationListener l = new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
                    location = AFPModel.proxyLocation(location, AFPModel.getLocationAccessLevel(_featureNames));
                    listener.onLocationChanged(location);
                }

            };
            return f.requestLocationUpdates(client, request, (com.google.android.gms.location.LocationListener) l);
        }
    }

    public static PendingResult<Status> removeLocationUpdates(FusedLocationProviderApi f, GoogleApiClient client, LocationListener l){
        return f.removeLocationUpdates(client, (com.google.android.gms.location.LocationListener) l);
    }
}


