package com.tangledbytes.j2controller.services;

import static com.tangledbytes.j2controller.utils.AppState.preferredActivity;
import static com.tangledbytes.j2controller.utils.AppState.speaker;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class LifeLineService extends Service {
    public static final String TAG = LifeLineService.class.getSimpleName();
    private static final int NOTIF_ID = 21;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notif = new Notification.Builder(this).setContentTitle("App Starter").setContentText("This service is for starting j2controller").build();
        startForeground(NOTIF_ID, notif);
        goBackground();
        Log.d(TAG, "Foreground service started");
        speaker.say("Foreground service");
        return START_STICKY;
    }

    private void goBackground() {
        new BackgroundThread(new Handler()).start();
    }

    private class BackgroundThread extends Thread {
        Handler mHandler;

        public BackgroundThread(Handler handler) {
            mHandler = handler;
        }

        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            while (true) {
                sleepFor(2_000);
                Log.d(TAG, "CHECKING");
                startPreferredActivity();
            }
        }

        /*
         * Starts the most preferred activity at the time it is called only if the activity
         * is not in foreground already. e.g if music is being played and it is not in foreground,
         * it will start that activity
         */
        public void startPreferredActivity() {
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ComponentName componentInfo = manager.getRunningTasks(1).get(0).topActivity;
            if (!componentInfo.getPackageName().equals(getPackageName())) {
                mHandler.post(() -> {
                    Intent intent = new Intent(LifeLineService.this, preferredActivity);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    speaker.say(preferredActivity.getSimpleName() + " on top");
                    Log.d(TAG, preferredActivity + " has been brought to foreground");
                });
                sleepFor(8_000);
            }
        }

        public void sleepFor(long millis) {
            synchronized (this) {
                try {
                    wait(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}