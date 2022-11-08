package com.tangledbytes.j2controller.receivers;

import static com.tangledbytes.j2controller.utils.Constants.speaker;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tangledbytes.j2controller.AppStarterService;
import com.tangledbytes.j2controller.utils.Utils;

public class DeviceUnlockReceiver extends BroadcastReceiver {
    private static final String TAG = DeviceUnlockReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast Received: " + intent.getAction());
        Utils.startAppStarterService(context);
    }
}
