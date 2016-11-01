package pe.mplescano.mobile.myapp.poc03.ioc.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import pe.mplescano.mobile.myapp.poc03.dao.DatabaseHandler;
import pe.mplescano.mobile.myapp.poc03.dao.DbPointLocationDao;
import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;

/**
 * Created by mplescano on 04/10/2016.
 */
@Module
public class BackendModule {

    @Provides
    @Singleton
    DatabaseHandler getDatabaseHandler(Context mContext) {
        return new DatabaseHandler(mContext);
    }

    @Provides
    @Singleton
    PointLocationDao getPointLocationDao(DatabaseHandler databaseHandler) {
        return new DbPointLocationDao(databaseHandler);
    }
}
