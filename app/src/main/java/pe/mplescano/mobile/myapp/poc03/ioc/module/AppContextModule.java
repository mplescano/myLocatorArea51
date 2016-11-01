package pe.mplescano.mobile.myapp.poc03.ioc.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mplescano on 24/03/2016.
 */
@Module
public class AppContextModule {

    private Application application;

    public AppContextModule(Application mApplication) {
        this.application = mApplication;
    }

    @Provides
    @Singleton
    public Application getApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context getApplicationContext() {
        return application.getApplicationContext();
    }

}