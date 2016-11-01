package pe.mplescano.mobile.myapp.poc03.support;

import android.location.LocationManager;
import android.util.Log;

import java.util.TimerTask;

import pe.mplescano.mobile.myapp.poc03.support.listener.LocationListener;

/**
 * Created by mplescano on 06/10/2016.
 */
public class LocationTask implements Runnable {

    private static final String TAG = "LocationTask";

    private LocationManager locationManager;

    private LocationListener locationListener;

    public LocationTask(final LocationManager locationManager,
                        final LocationListener locationListener) {
        this.locationManager = locationManager;
        this.locationListener = locationListener;
    }

    @Override
    public void run() {
        try {
            if (!locationListener.isRunning()) {
                locationListener.setRunning(true);
                for (String provider : locationManager.getProviders(true)) {
                    locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
                    Log.v(TAG, "provider:" + provider);
                }
            }
        }
        catch (SecurityException e) {
            locationListener.setRunning(false);
        }
    }
}
