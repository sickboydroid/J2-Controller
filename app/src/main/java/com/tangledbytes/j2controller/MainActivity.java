package com.tangledbytes.j2controller;

import static com.tangledbytes.j2controller.utils.AppState.speaker;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tangledbytes.j2controller.music.MusicPlayerActivity;
import com.tangledbytes.j2controller.music.SongsManager;
import com.tangledbytes.j2controller.utils.Utils;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speaker.say("Home");
        Utils.startAppStarterService(this);
        Button btnMusicPlayer = findViewById(R.id.btnMusicPlayer);
        Button btnToggleFlash = findViewById(R.id.btnToggleFlash);
        Button btnStatus = findViewById(R.id.btnStatus);
        Button btnMisc = findViewById(R.id.btnMisc);
        btnMusicPlayer.setOnClickListener((View view) -> startActivity(new Intent(this, MusicPlayerActivity.class)));
        btnToggleFlash.setOnClickListener((View view) -> toggleFlash());
        btnStatus.setOnClickListener((View view) -> showStatus());
        btnMisc.setOnClickListener((View view) -> misc());
    }

    private void misc() {
    }

    private void showStatus() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int batteryPercentage = (int) ((level / (float) scale) * 100);
        int songsAvailable = SongsManager.initializeSongManager(false).getTotalSongs();
        speaker.say("Battery is " + batteryPercentage + " percent charge");
        speaker.say("You have " + songsAvailable + " songs", false);
    }

    private void toggleFlash() {
        if (mCamera == null)
            mCamera = Camera.open();
        Camera.Parameters p = mCamera.getParameters();
        String flashMode = p.getFlashMode();
        if (flashMode.equals(Camera.Parameters.FLASH_MODE_OFF)) {
            Log.d(TAG, "Turning flash ON");
            speaker.say("Flash ON");
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(p);
            mCamera.startPreview();
        } else {
            Log.d(TAG, "Turning flash OFF");
            speaker.say("Flash OFF", true);
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(p);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}