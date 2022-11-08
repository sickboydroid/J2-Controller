package com.tangledbytes.j2controller.utils;

import static com.tangledbytes.j2controller.utils.Constants.speaker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.tangledbytes.j2controller.AppStarterService;

public class Utils {
    public static final String TAG = Utils.class.getSimpleName();

    public static void startAppStarterService(Context context) {
        boolean isRunning = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AppStarterService.class.getName().equals(service.service.getClassName())) {
                isRunning = true;
                break;
            }
        }
        if (!isRunning) {
            speaker.say("AppStarterService was not running. Now started");
            context.startService(new Intent(context, AppStarterService.class));
        }
    }

    public static void startRequiredActivity(Context context) {
    }
}
