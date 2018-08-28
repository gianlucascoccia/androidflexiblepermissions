package gssi.aq.it.afplibrary;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.util.Log;

import java.util.List;

/**
 * Created by gianlucascoccia on 16/08/16.
 */
public class AFPLocationManager {

    private static LocationListener l = null;

    public static boolean addGpsStatusListener(LocationManager lm, GpsStatus.Listener listener) throws SecurityException {
        return lm.addGpsStatusListener(listener);
    }

//  public static boolean addNmeaListener (LocationManager lm, OnNmeaMessageListener listener, Handler handler)

//  public static boolean addNmeaListener (LocationManager lm, OnNmeaMessageListener listener)

    public static boolean addNmeaListener (LocationManager lm, GpsStatus.NmeaListener listener) throws SecurityException {
        return lm.addNmeaListener(listener);
    }

    public static void addProximityAlert (LocationManager lm, double latitude, double longitude, float radius, long expiration, PendingIntent intent) throws SecurityException {
        lm.addProximityAlert(latitude, longitude, radius, expiration, intent);
    }

    public static void addTestProvider (LocationManager lm, String name, boolean requiresNetwork, boolean requiresSatellite, boolean requiresCell, boolean hasMonetaryCost,
                                        boolean supportsAltitude, boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy) {
        lm.addTestProvider(name, requiresNetwork, requiresSatellite, requiresCell, hasMonetaryCost, supportsAltitude, supportsSpeed, supportsBearing, powerRequirement, accuracy);
    }

    public static void clearTestProviderEnabled (LocationManager lm, String provider){
        lm.clearTestProviderEnabled(provider);
    }

    public static void clearTestProviderLocation (LocationManager lm, String provider){
        lm.clearTestProviderEnabled(provider);
    }

    public static void clearTestProviderStatus (LocationManager lm, String provider){
        lm.clearTestProviderStatus(provider);
    }

    public static List<String> getAllProviders (LocationManager lm){
        return lm.getAllProviders();
    }

    public static String getBestProvider (LocationManager lm, Criteria criteria, boolean enabledOnly){
        return lm.getBestProvider(criteria, enabledOnly);
    }

    public static GpsStatus getGpsStatus (LocationManager lm, GpsStatus status){
        return lm.getGpsStatus(status);
    }

    public static Location getLastKnownLocation (LocationManager lm, String provider, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER GET LAST KNOWN LOCATION ***");
        Location loc =  lm.getLastKnownLocation(provider);
        if(loc != null){
            AFPModel.proxyLocation(loc, AFPModel.getLocationAccessLevel(_featureNames));
            return loc;
        }
        return null;
    }

    public static LocationProvider getProvider (LocationManager lm, String name){
        return lm.getProvider(name);
    }

    public static List<String> getProviders (LocationManager lm, Criteria criteria, boolean enabledOnly){
        return lm.getProviders(criteria, enabledOnly);
    }

    public static List<String> getProviders (LocationManager lm, boolean enabledOnly){
        return lm.getProviders(enabledOnly);
    }

    public static boolean isProviderEnabled (LocationManager lm, String provider){
        return lm.isProviderEnabled(provider);
    }

//    public static boolean registerGnssMeasurementsCallback (GnssMeasurementsEvent.Callback callback, Handler handler)

//    public static boolean registerGnssMeasurementsCallback (GnssMeasurementsEvent.Callback callback)

//    public static boolean registerGnssNavigationMessageCallback (GnssNavigationMessage.Callback callback, Handler handler)

//    public static boolean registerGnssNavigationMessageCallback (GnssNavigationMessage.Callback callback)

//    public static boolean registerGnssStatusCallback (GnssStatus.Callback callback)

//    public static boolean registerGnssStatusCallback (GnssStatus.Callback callback, Handler handler)

    public static void removeGpsStatusListener (LocationManager lm, GpsStatus.Listener listener){
        lm.removeGpsStatusListener(listener);
    }

//    public static void void removeNmeaListener (OnNmeaMessageListener listener)

    public static void removeNmeaListener (LocationManager lm, GpsStatus.NmeaListener listener){
        lm.removeNmeaListener(listener);
    }

    public static void removeProximityAlert (LocationManager lm, PendingIntent intent) throws SecurityException {
        lm.removeProximityAlert(intent);
    }

    public static void removeTestProvider (LocationManager lm, String provider){
        lm.removeTestProvider(provider);
    }

    public static void removeUpdates (LocationManager lm, LocationListener listener) throws SecurityException {
        lm.removeUpdates(listener);
    }

    public static void removeUpdates (LocationManager lm, PendingIntent intent) {
        lm.removeUpdates(intent);
    }

    public static void requestLocationUpdates (LocationManager lm, String provider, long minTime, float minDistance, final LocationListener listener, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 1 ***");
        l = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
                location = AFPModel.proxyLocation( location, AFPModel.getLocationAccessLevel(_featureNames));
                listener.onLocationChanged(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                listener.onStatusChanged(provider, status, extras);
            }
            @Override
            public void onProviderEnabled(String provider) {
                listener.onProviderEnabled(provider);
            }
            @Override
            public void onProviderDisabled(String provider) {
                listener.onProviderDisabled(provider);
            }
        };
        lm.requestLocationUpdates(provider, minTime, minDistance, l);
    }

    public static void requestLocationUpdates (LocationManager lm, long minTime, float minDistance, Criteria criteria, final LocationListener listener, Looper looper, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 2 ***");
        l = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
                location = AFPModel.proxyLocation( location, AFPModel.getLocationAccessLevel(_featureNames));
                listener.onLocationChanged(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                listener.onStatusChanged(provider, status, extras);
            }
            @Override
            public void onProviderEnabled(String provider) {
                listener.onProviderEnabled(provider);
            }
            @Override
            public void onProviderDisabled(String provider) {
                listener.onProviderDisabled(provider);
            }
        };
        lm.requestLocationUpdates(minTime, minDistance, criteria, l, looper);
    }

    public static void requestLocationUpdates (LocationManager lm, long minTime, float minDistance, Criteria criteria, PendingIntent intent, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 3# ***");
        if(AFPModel.getLocationAccessLevel(_featureNames) == 3) {
            lm.requestLocationUpdates(minTime,minDistance, criteria, intent);
        }
//        Maybe we can get the intent data from a parcel?
//        Parcel p  = Parcel.obtain();
//        intent.writeToParcel(p, 0);
    }

    public static void requestLocationUpdates (LocationManager lm, String provider, long minTime, float minDistance, final LocationListener listener, Looper looper, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 4 ***");
        l = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
                location = AFPModel.proxyLocation( location, AFPModel.getLocationAccessLevel(_featureNames));
                listener.onLocationChanged(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                listener.onStatusChanged(provider, status, extras);
            }
            @Override
            public void onProviderEnabled(String provider) {
                listener.onProviderEnabled(provider);
            }
            @Override
            public void onProviderDisabled(String provider) {
                listener.onProviderDisabled(provider);
            }
        };
        lm.requestLocationUpdates(provider, minTime, minDistance, l, looper);
    }

    public static void requestLocationUpdates (LocationManager lm, String provider, long minTime, float minDistance, PendingIntent intent, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 5# ***");
        if(AFPModel.getLocationAccessLevel(_featureNames) == 3) {
            lm.requestLocationUpdates(provider, minTime, minDistance, intent);
        }
    }

    public static void requestSingleUpdate (LocationManager lm, String provider, PendingIntent intent, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 6# ***");
        if(AFPModel.getLocationAccessLevel(_featureNames) == 3) {
            lm.requestSingleUpdate(provider, intent);
        }
    }

    public static void requestSingleUpdate (LocationManager lm, String provider, final LocationListener listener, Looper looper, final String[] _featureNames) throws SecurityException  {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 7 ***");
        l = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
                location = AFPModel.proxyLocation( location, AFPModel.getLocationAccessLevel(_featureNames));
                listener.onLocationChanged(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                listener.onStatusChanged(provider, status, extras);
            }
            @Override
            public void onProviderEnabled(String provider) {
                listener.onProviderEnabled(provider);
            }
            @Override
            public void onProviderDisabled(String provider) {
                listener.onProviderDisabled(provider);
            }
        };
        lm.requestSingleUpdate(provider,l,looper);
    }

    public static void requestSingleUpdate (LocationManager lm, Criteria criteria, final LocationListener listener, Looper looper, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 8 ***");
        l = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("AFPLib", "ACCESS LEVEL -> " + AFPModel.getLocationAccessLevel(_featureNames));
                location = AFPModel.proxyLocation( location, AFPModel.getLocationAccessLevel(_featureNames));
                listener.onLocationChanged(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                listener.onStatusChanged(provider, status, extras);
            }
            @Override
            public void onProviderEnabled(String provider) {
                listener.onProviderEnabled(provider);
            }
            @Override
            public void onProviderDisabled(String provider) {
                listener.onProviderDisabled(provider);
            }
        };
        lm.requestSingleUpdate(criteria, l, looper);
    }

    public static void requestSingleUpdate (LocationManager lm, Criteria criteria, PendingIntent intent, final String[] _featureNames) throws SecurityException {
        Log.d("AFPLib", " *** AFPLOCATIONMANAGER REQUEST LOCATION UPDATES 9# ***");
        if(AFPModel.getLocationAccessLevel(_featureNames) == 3){
            lm.requestSingleUpdate(criteria, intent);
        }
    }

    public static boolean sendExtraCommand (LocationManager lm, String provider, String command, Bundle extras){
        return lm.sendExtraCommand(provider, command, extras);
    }

    public static void setTestProviderEnabled (LocationManager lm, String provider, boolean enabled){
        lm.setTestProviderEnabled(provider, enabled);
    }

    public static void setTestProviderLocation (LocationManager lm, String provider, Location loc){
        lm.setTestProviderLocation(provider, loc);
    }

    public static void setTestProviderStatus (LocationManager lm, String provider, int status, Bundle extras, long updateTime){
        lm.setTestProviderStatus(provider, status, extras, updateTime);
    }

//    void unregisterGnssMeasurementsCallback (GnssMeasurementsEvent.Callback callback)

//    void unregisterGnssNavigationMessageCallback (GnssNavigationMessage.Callback callback)

//    void unregisterGnssStatusCallback (GnssStatus.Callback callback)


}
