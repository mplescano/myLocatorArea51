package pe.mplescano.mobile.myapp.poc03.frontend.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pe.mplescano.mobile.myapp.poc03.frontend.service.LocationHandlerService;

/**
 * Created by mplescano on 06/10/2016.
 */
public class AppBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            Intent intentService = new Intent(context, LocationHandlerService.class);
            switch (intent.getAction()) {
                case Intent.ACTION_DEVICE_STORAGE_LOW:
                case Intent.ACTION_BATTERY_LOW:
                    intentService.putExtra("status", "low");
                    context.startService(intentService);
                    break;

                case Intent.ACTION_DEVICE_STORAGE_OK:
                case Intent.ACTION_BATTERY_OKAY:
                    intentService.putExtra("status", "ok");
                    context.startService(intentService);
                    break;

                case Intent.ACTION_BOOT_COMPLETED:
                    intentService.putExtra("status", "ok");
                    context.startService(intentService);
                    break;
            }
        }
    }
}
