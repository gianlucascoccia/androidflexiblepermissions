package gssi.aq.it.afplibrary;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

/**
 * Created by gianlucascoccia on 16/06/16.
 */

public class AFPLocationService {

        private static LocationListener l;
        private static String[] _featureNames;

    public static Location getLastLocation(Object FakeArg, GoogleApiClient googleApiClient, final String[] _featureNames) {
        Log.d("AFPLib", " *** AFPLOCATION GET LAST LOCATION ***");
        Location loc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
        loc = AFPModel.proxyLocation( loc, AFPModel.getLocationAccessLevel(_featureNames));
        Log.d("AFPLib", "FINAL VALUE = " + loc.getLongitude() +  loc.getLatitude());
        return loc;
    }

    public static LocationAvailability getLocationAvailability(Object FakeArg, GoogleApiClient client) {
        Log.d("AFPLib", " *** AFPLOCATION GET LOCATION AVAILABILITY ***");
        return LocationServices.FusedLocationApi.getLocationAvailability(client);
    }

    public static final  PendingResult<Status> removeLocationUpdates (Object FakeArg, GoogleApiClient client, PendingIntent callbackIntent) {
        Log.d("AFPLib", " *** AFPLOCATION REMOVE UPDATES ***");
        return LocationServices.FusedLocationApi.removeLocationUpdates(client, callbackIntent);
    }

    public static final PendingResult<Status> removeLocationUpdates (Object FakeArg, GoogleApiClient client, LocationListener listener){
        Log.d("AFPLib", " *** AFPLOCATION REMOVE UPDATES ***");
        return LocationServices.FusedLocationApi.removeLocationUpdates(client, l);
    }

    public static final PendingResult<Status> removeLocationUpdates (Object FakeArg, GoogleApiClient client, LocationCallback callback){
        Log.d("AFPLib", " *** AFPLOCATION REMOVE UPDATES ***");
        return LocationServices.FusedLocationApi.removeLocationUpdates(client, callback);
    }

    //TODO Check afp model here!
    public static final PendingResult<Status> requestLocationUpdates (Object FakeArg, GoogleApiClient client, LocationRequest request, final LocationListener listener, final String[] _featureNames) throws IllegalStateException, SecurityException {
        Log.d("AFPLib", " *** AFPLOCATION REQUEST UPDATES 1 ***");
        l = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
                location = AFPModel.proxyLocation( location, AFPModel.getLocationAccessLevel(_featureNames));
                listener.onLocationChanged(location);
            }
        };
        return LocationServices.FusedLocationApi.requestLocationUpdates(client, request, l);
    }

    //TODO Check afp model here!
    public static final PendingResult<Status> requestLocationUpdates (Object FakeArg, GoogleApiClient client, LocationRequest request, final LocationCallback callback, Looper looper, final String[] _featureNames) throws IllegalStateException, SecurityException {
        Log.d("AFPLib", " *** AFPLOCATION REQUEST UPDATES 2 ***");
        LocationCallback l = new LocationCallback(){
                @Override
                public void onLocationResult (LocationResult result){
                    AFPModel.proxyLocation(result, AFPModel.getLocationAccessLevel(_featureNames));
                    callback.onLocationResult(result);
                }
            };
        return LocationServices.FusedLocationApi.requestLocationUpdates(client, request, l, looper);
    }

    //TODO Check afp model here!
    public static final PendingResult<Status> requestLocationUpdates (Object FakeArg, GoogleApiClient client, LocationRequest request, final LocationListener listener, Looper looper, final String[] _featureNames) throws IllegalStateException, SecurityException {
        Log.d("AFPLib", " *** AFPLOCATION REQUEST UPDATES 3 ***");
        l = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
                    location = AFPModel.proxyLocation( location, AFPModel.getLocationAccessLevel(_featureNames));
                    listener.onLocationChanged(location);
                }
            };
        return LocationServices.FusedLocationApi.requestLocationUpdates(client, request, listener, looper);
    }

    public static final PendingResult<Status> requestLocationUpdates (Object FakeArg, GoogleApiClient client, LocationRequest request, PendingIntent callbackIntent, final String[] _featureNames) throws IllegalStateException, SecurityException {
        Log.d("AFPLib", " *** AFPLOCATION REQUEST UPDATES 4# ***");
        if(AFPModel.getLocationAccessLevel(_featureNames) == 3){
            return LocationServices.FusedLocationApi.requestLocationUpdates(client, request, callbackIntent);
        } else {
            return new PendingResult<Status>() {
                @Override
                public Status await() {
                    return null;
                }
                @Override
                public Status await(long l, TimeUnit timeUnit) {
                    return null;
                }
                @Override
                public void cancel() {
                }
                @Override
                public boolean isCanceled() {
                    return false;
                }
                @Override
                public void setResultCallback(ResultCallback<? super Status> resultCallback) {
                }
                @Override
                public void setResultCallback(ResultCallback<? super Status> resultCallback, long l, TimeUnit timeUnit) {
                }
            };
        }
    }

    public static final PendingResult<Status> setMockLocation (Object FakeArg, GoogleApiClient client, Location mockLocation, String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATION SET MOCK ***");
        return LocationServices.FusedLocationApi.setMockLocation(client, mockLocation);
    }

    public static final PendingResult<Status> setMockMode (Object FakeArg, GoogleApiClient client, boolean isMockMode){
        Log.d("AFPLib", " *** AFPLOCATION SET MOCK ***");
        return LocationServices.FusedLocationApi.setMockMode(client, isMockMode);
    }

//  public static PendingResult<Status> flushLocations(GoogleApiClient client) {}


}
