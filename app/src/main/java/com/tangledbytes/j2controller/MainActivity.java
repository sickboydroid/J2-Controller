package com.tangledbytes.j2controller;

import static com.tangledbytes.j2controller.utils.Constants.speaker;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tangledbytes.j2controller.music.MusicPlayer;
import com.tangledbytes.j2controller.utils.Utils;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speaker.say("Home");
        Button btnMusicPlayer = findViewById(R.id.btnMusicPlayer);
        Button btnToggleFlash = findViewById(R.id.btnToggleFlash);
        btnMusicPlayer.setOnClickListener(this);
        btnToggleFlash.setOnClickListener(this);
        Utils.startAppStarterService(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "Clicked: " + ((Button) view).getText().toString());
        if (view.getId() == R.id.btnMusicPlayer) {
            startActivity(new Intent(this, MusicPlayerActivity.class));
        } else if (view.getId() == R.id.btnToggleFlash) {
            toggleFlash();
        }
    }

    private void toggleFlash() {
        Camera camera = Camera.open();
        Camera.Parameters p = camera.getParameters();
        String flashMode = p.getFlashMode();
        if (flashMode.equals(Camera.Parameters.FLASH_MODE_OFF)) {
            Log.d(TAG, "Turning flash ON");
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            camera.startPreview();
        } else if (p.getFlashMode().equals(android.hardware.Camera.Parameters.FLASH_MODE_ON)) {
            Log.d(TAG, "Turning flash OFF");
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
        }
    }
}