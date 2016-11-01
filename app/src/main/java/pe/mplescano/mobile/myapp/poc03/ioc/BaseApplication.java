package pe.mplescano.mobile.myapp.poc03.ioc;

import android.app.Application;
import android.content.Intent;

import pe.mplescano.mobile.myapp.poc03.frontend.service.LocationHandlerService;
import pe.mplescano.mobile.myapp.poc03.ioc.component.DaggerMainComponent;
import pe.mplescano.mobile.myapp.poc03.ioc.component.MainComponent;
import pe.mplescano.mobile.myapp.poc03.ioc.module.AppContextModule;

/**
 * Created by mplescano on 25/03/2016.
 */
public class BaseApplication extends Application {

    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mainComponent = DaggerMainComponent.builder()
                .appContextModule(new AppContextModule(this))
                .build();

        Intent intentService = new Intent(this, LocationHandlerService.class);
        startService(intentService);
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }

}
