package pe.mplescano.mobile.myapp.poc03.frontend.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;
import pe.mplescano.mobile.myapp.poc03.support.LocationTimerTask;
import pe.mplescano.mobile.myapp.poc03.support.listener.LocationListener;

/**
 * Created by mplescano on 04/10/2016.
 *
 * Los timer no son reusables una vez cancelados
 * @see //stackoverflow.com/questions/15680642/android-relaunching-timers-after-they-are-canceled
 *
 */
public class LocationTimerService extends Service {

    PointLocationDao pointLocationDao;

    LocationManager locationManager;

    LocationListener locationListener;

    static final int NORMAL_FRECUENCY = 120;

    static final int LOW_FRECUENCY = 360;

    int frecuency = 120;

    TimerTask tasker;

    Timer timer = new Timer();

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener(locationManager, pointLocationDao);
        tasker = new LocationTimerTask(locationManager, locationListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //boton de gui: por ver..
        //broadcast de baja bateria cambiar frecuencia
        //broadcast de boot_completed
        String status = intent.getStringExtra("status");
        if ("low".equals(status)) {
            frecuency = LOW_FRECUENCY;
        }
        else {
            frecuency = NORMAL_FRECUENCY;
        }
        if (!locationListener.isRunning()) {
            timer.cancel();
            timer = new Timer();
            timer.schedule(tasker, 0, frecuency * 1000);
        }
        else {
            locationListener = new LocationListener(locationManager, pointLocationDao);
            tasker = new LocationTimerTask(locationManager, locationListener);
            timer.cancel();
            timer.purge();
            timer = new Timer();
            timer.schedule(tasker, 0, frecuency * 1000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer.purge();
        timer = null;
    }
}
