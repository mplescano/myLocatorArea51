package pe.mplescano.mobile.myapp.poc03.frontend.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;
import pe.mplescano.mobile.myapp.poc03.ioc.BaseApplication;
import pe.mplescano.mobile.myapp.poc03.support.LocationTask;
import pe.mplescano.mobile.myapp.poc03.support.LocationTimerTask;
import pe.mplescano.mobile.myapp.poc03.support.RepetitiveTask;
import pe.mplescano.mobile.myapp.poc03.support.listener.LocationListener;

/**
 * Created by mplescano on 04/10/2016.
 *
 * Los timer no son reusables una vez cancelados
 * @see //stackoverflow.com/questions/15680642/android-relaunching-timers-after-they-are-canceled
 *
 */
public class LocationHandlerService extends Service {

    @Inject
    PointLocationDao pointLocationDao;

    LocationManager locationManager;

    LocationListener locationListener;

    static final int NORMAL_FRECUENCY = 120;

    static final int LOW_FRECUENCY = 360;

    int frecuency = NORMAL_FRECUENCY;

    Runnable tasker;

    RepetitiveTask repetitiveTask;

    Handler handler;

    @Override
    public void onCreate() {
        ((BaseApplication) getApplication()).getMainComponent().inject(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener(locationManager, pointLocationDao);
        tasker = new LocationTask(locationManager, locationListener);
        handler = new Handler();
        repetitiveTask = new RepetitiveTask(tasker, handler);
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
        if (intent != null) {
            String status = intent.getStringExtra("status");
            if ("low".equals(status)) {
                frecuency = LOW_FRECUENCY;
            }
            else {
                frecuency = NORMAL_FRECUENCY;
            }
        }
        if (!locationListener.isRunning()) {
            repetitiveTask.stop();
            repetitiveTask.start(frecuency * 1000);
        }
        else {
            locationListener = new LocationListener(locationManager, pointLocationDao);
            tasker = new LocationTimerTask(locationManager, locationListener);
            repetitiveTask.stop();
            repetitiveTask = new RepetitiveTask(tasker, handler);
            repetitiveTask.start(frecuency * 1000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        repetitiveTask.stop();
    }
}
