package com.tangledbytes.j2controller.receivers;

import static com.tangledbytes.j2controller.utils.AppState.speaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tangledbytes.j2controller.utils.Utils;

public class LifeLineBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = LifeLineBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "Broadcast Received: " + action);
        String actionSimplified = action.substring(action.lastIndexOf("."), action.length() - 1)
                .replaceAll("_", "")
                .toLowerCase();
        speaker.say("Broadcast " + actionSimplified);
        Utils.startAppStarterService(context);
    }
}
