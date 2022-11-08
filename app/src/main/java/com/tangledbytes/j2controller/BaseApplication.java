package com.tangledbytes.j2controller;

import static com.tangledbytes.j2controller.utils.Constants.speaker;

import android.app.Application;
import android.util.Log;

import com.tangledbytes.j2controller.utils.Constants;
import com.tangledbytes.j2controller.utils.Speaker;
import com.tangledbytes.j2controller.utils.Utils;

import java.io.File;

public class BaseApplication extends Application {
    private static String TAG = "BaseApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        if(speaker == null)
        speaker = new Speaker(this);
        createRequiredDirectories();
        setUpExceptionHandler();
    }

    private void setUpExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {
            paramThrowable.printStackTrace();
            speaker.say("Exception");
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
