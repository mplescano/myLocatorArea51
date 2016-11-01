package pe.mplescano.mobile.myapp.poc03.support;


import android.os.Handler;

/**
 * Created by mplescano on 06/10/2016.
 */
public class RepetitiveTask {

    private Runnable taskWrapper;
    private final Runnable taskHandler;
    private final Handler handler;
    private boolean running;

    public RepetitiveTask(final Runnable taskHandler, final Handler handler) {
        this.handler = handler;
        this.taskHandler = taskHandler;
        this.running = false;
    }

    public void start(final long delayInMillis) {
        if (taskWrapper == null) {
            taskWrapper = new Runnable() {
                @Override
                public void run() {
                    taskHandler.run();
                    handler.postDelayed(this, delayInMillis);
                }
            };
        }

        if (!running) {
            if (handler.getLooper().getThread().getId() ==
                    Thread.currentThread().getId()) {
                taskWrapper.run();
            }
            else {
                handler.post(taskWrapper);
            }
            running = true;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        if (running) {
            if (taskWrapper != null) {
                handler.removeCallbacks(taskWrapper);
                taskWrapper = null;
            }
            running = false;
        }
    }
}
