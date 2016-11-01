package pe.mplescano.mobile.myapp.poc03.support;

import android.location.LocationManager;

import java.util.TimerTask;

import pe.mplescano.mobile.myapp.poc03.support.listener.LocationListener;

/**
 * Created by mplescano on 06/10/2016.
 */
public class LocationTimerTask extends TimerTask {

    private LocationManager locationManager;

    private LocationListener locationListener;

    public LocationTimerTask(final LocationManager locationManager,
                             final LocationListener locationListener) {
        this.locationManager = locationManager;
        this.locationListener = locationListener;
    }

    @Override
    public void run() {
        try {
            if (!locationListener.isRunning()) {
                locationListener.setRunning(true);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        }
        catch (SecurityException e) {
            locationListener.setRunning(false);
        }
    }
}
