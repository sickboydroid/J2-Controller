package com.tangledbytes.j2controller.utils;

import static com.tangledbytes.j2controller.utils.AppState.speaker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.tangledbytes.j2controller.music.SongsManager;
import com.tangledbytes.j2controller.services.LifeLineService;

public class Utils {
    public static final String TAG = Utils.class.getSimpleName();

    public static void startAppStarterService(Context context) {
        boolean isRunning = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LifeLineService.class.getName().equals(service.service.getClassName())) {
                isRunning = true;
                break;
            }
        }
        if (!isRunning) {
            speaker.say("AppStarterService was not running. Now started");
            context.startService(new Intent(context, LifeLineService.class));
        }
    }

    public static int getBatteryLevel(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // convert to percentage before returning
        return (int) ((level / (float) scale) * 100);
    }
}
