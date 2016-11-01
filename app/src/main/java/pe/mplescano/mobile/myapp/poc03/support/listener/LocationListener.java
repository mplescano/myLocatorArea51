package pe.mplescano.mobile.myapp.poc03.support.listener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Date;

import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;
import pe.mplescano.mobile.myapp.poc03.domain.PointLocation;

/**
 * Created by mplescano on 04/10/2016.
 */
public class LocationListener implements android.location.LocationListener {

    private static final String TAG = "LocationListener";

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private boolean running;

    /*private String provider;*/

    private LocationManager locationManager;

    private PointLocationDao pointLocationDao;

    private Location previousLocation;

    public LocationListener(LocationManager locationManager, PointLocationDao pointLocationDao) {
        this.locationManager = locationManager;
        this.pointLocationDao = pointLocationDao;
        this.running = false;
        Log.v(TAG, "instancing");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "onLocationChanged");
        if (previousLocation != null) {
            Location tmpLocation;
            if (isBetterLocation(location, previousLocation)) {
                tmpLocation = location;
            }
            else {
                tmpLocation = previousLocation;
            }
            try {
                PointLocation pointLocation = new PointLocation();
                pointLocation.setLatitude(tmpLocation.getLatitude());
                pointLocation.setLongitude(tmpLocation.getLongitude());
                pointLocation.setDateCreation(new Date());
                pointLocationDao.insertPointLocation(pointLocation);
                Log.v(TAG, "pointLocationDao.insertPointLocation");
            }
            catch (Exception e) {
                //LOG
                Log.e(TAG, "", e);
            }

            try {
                locationManager.removeUpdates(this);
                this.running = false;
            }
            catch (SecurityException e) {
                //LOG
                this.running = false;
            }
            previousLocation = null;
        }
        else {
            previousLocation = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param newLocation  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location newLocation, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
