package com.tangledbytes.j2controller;

import static com.tangledbytes.j2controller.utils.AppState.speaker;

import android.app.Application;
import android.util.Log;

import com.tangledbytes.j2controller.utils.Constants;
import com.tangledbytes.j2controller.utils.Speaker;
import com.tangledbytes.j2controller.utils.Utils;

import java.io.File;

public class BaseApplication extends Application {
    private static final String TAG = BaseApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        if (speaker == null)
            speaker = new Speaker(this);
        setUpExceptionHandler();
        createRequiredDirectories();
    }

    private void setUpExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {
            speaker.say("Exception occurred " + paramThrowable.getMessage());
            Log.wtf(TAG, "Unhandled exception (handled in BaseApplication)", paramThrowable);
            Utils.startAppStarterService(BaseApplication.this);
            System.exit(2);
        });
    }

    private void createRequiredDirectories() {
        File songsDir = new File(Constants.EXT_SONGS_DIR);
        if (!songsDir.exists())
            if (!songsDir.mkdirs())
                Log.d(TAG, "Songs directory cannot be create");
    }
}
