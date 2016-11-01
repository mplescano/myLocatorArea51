package pe.mplescano.mobile.myapp.poc03.ioc.component;


import javax.inject.Singleton;

import dagger.Component;
import pe.mplescano.mobile.myapp.poc03.frontend.activity.MainActivity;
import pe.mplescano.mobile.myapp.poc03.frontend.fragment.FormPointLocationFragment;
import pe.mplescano.mobile.myapp.poc03.frontend.fragment.ListPointLocationFragment;
import pe.mplescano.mobile.myapp.poc03.frontend.service.LocationHandlerService;
import pe.mplescano.mobile.myapp.poc03.ioc.module.AppContextModule;
import pe.mplescano.mobile.myapp.poc03.ioc.module.BackendModule;
import pe.mplescano.mobile.myapp.poc03.ioc.module.FrontendModule;

/**
 * Created by mplescano on 25/03/2016.
 */
@Singleton
@Component(modules = {AppContextModule.class,
        BackendModule.class, FrontendModule.class})
public interface MainComponent {

    void inject(MainActivity mainActivity);

    void inject(ListPointLocationFragment listPointLocationFragment);

    void inject(FormPointLocationFragment formPointLocationFragment);

    void inject(LocationHandlerService locationHandlerService);

}
