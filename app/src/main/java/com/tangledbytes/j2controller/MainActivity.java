package com.tangledbytes.j2controller;

import static com.tangledbytes.j2controller.utils.AppState.speaker;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tangledbytes.j2controller.music.MusicPlayerActivity;
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
        btnMusicPlayer.setOnClickListener((View view) -> startActivity(new Intent(this, MusicPlayerActivity.class)));
        btnToggleFlash.setOnClickListener((View view) -> toggleFlash());
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