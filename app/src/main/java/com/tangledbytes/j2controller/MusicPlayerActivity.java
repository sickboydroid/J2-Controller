package com.tangledbytes.j2controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tangledbytes.j2controller.utils.Constants;
import com.tangledbytes.j2controller.utils.Speaker;
import com.tangledbytes.j2controller.music.MusicPlayer;

import java.io.IOException;

public class MusicPlayerActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MusicPlayerActivity";
    Speaker speaker = Constants.speaker;
    MusicPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        speaker.say("Music Player");
        Button btnPlayToggle = (Button) findViewById(R.id.btnPlayToggle);
        Button btnSeek = (Button) findViewById(R.id.btnSeek);
        Button btnPrevSong = (Button) findViewById(R.id.btnPrevSong);
        Button btnNextSong = (Button) findViewById(R.id.btnNextSong);
        Button btnShuffleToggle = (Button) findViewById(R.id.btnShuffleToggle);
        Button btnRepeatToggle = (Button) findViewById(R.id.btnRepeatToggle);
        btnPlayToggle.setOnClickListener(this);
        btnSeek.setOnClickListener(this);
        btnPrevSong.setOnClickListener(this);
        btnNextSong.setOnClickListener(this);
        btnShuffleToggle.setOnClickListener(this);
        btnRepeatToggle.setOnClickListener(this);
//        try {
        try {
            player = new MusicPlayer();
            player.restartPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        } catch (IOException e) {
//            speaker.say("Exception in MainActivity while playing songs");
//            speaker.say(e.toString());
//            Log.e(TAG, "Exception occurred while playing songs",e);
//        }
    }

    @Override
    public void onClick(View view) {

        Log.d(TAG, ((Button) view).getText().toString());
        try {
            int id = view.getId();
            if (id == R.id.btnPlayToggle) {
                if (player.isPlaying())
                    player.pause();
                else
                    player.resume();
            } else if (id == R.id.btnSeek) {
                player.seekTo(player.getCurrentPosition() + 10_000);
            } else if (id == R.id.btnPrevSong) {
                player.playPrevSong();
            } else if (id == R.id.btnNextSong) {
                player.playNextSong();
            } else if (id == R.id.btnShuffleToggle) {
                player.setShuffle(!player.isShuffle());
                player.restartPlayer();
                if (player.isShuffle())
                    speaker.say("Shuffle turned ON");
                else
                    speaker.say("Shuffle turned OFF");
            } else if (id == R.id.btnRepeatToggle) {
                player.setRepeat(!player.isRepeat());
                if (player.isRepeat())
                    speaker.say("Repeat turned ON");
                else
                    speaker.say("Repeat turned OFF");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        player.destroyPlayer();
        super.onDestroy();
    }
}