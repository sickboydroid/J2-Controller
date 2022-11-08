package com.tangledbytes.j2controller;

import static com.tangledbytes.j2controller.utils.Constants.speaker;

import static java.lang.Thread.sleep;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class AppStarterService extends Service {
    public static final String TAG = AppStarterService.class.getSimpleName();
    private static final int NOTIF_ID = 21;

    public AppStarterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "AppStarterService has been started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notif = new Notification.Builder(this).setContentTitle("App Starter").setContentText("This service is for starting j2controller").build();
        startForeground(NOTIF_ID, notif);
        goBackground();
        return START_STICKY;
    }

    private void goBackground() {
        Handler handler = new Handler();
        Thread thread = new Thread(() -> {
           while(true) {
               try {
                   ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
               ComponentName componentInfo = manager.getRunningTasks(1).get(0).topActivity;
               if(!componentInfo.getPackageName().equals(getPackageName())) {
                   handler.post(()-> {
                      Intent intent = new Intent(AppStarterService.this, MainActivity.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(intent);
                      speaker.say("Activity started");
                      Log.d(TAG, "MainActivity has been brought to foreground");
                   });
                   sleep(5_000);
               }
               Log.d(TAG, "CHECKING");
               sleep(1_000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        thread.start();
    }
}