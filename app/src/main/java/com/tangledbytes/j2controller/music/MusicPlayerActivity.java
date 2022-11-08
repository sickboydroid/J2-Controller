package com.tangledbytes.j2controller.music;

import static com.tangledbytes.j2controller.utils.AppState.speaker;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tangledbytes.j2controller.MainActivity;
import com.tangledbytes.j2controller.R;
import com.tangledbytes.j2controller.utils.AppState;

import java.io.File;
import java.io.IOException;

public class MusicPlayerActivity extends Activity {
    private static final String TAG = "MusicPlayerActivity";
    private MediaPlayer mMediaPlayer;
    private SongsManager mSongsManager;
    private boolean mShuffle = true;
    private boolean mRepeat;
    private int mPausePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        AppState.preferredActivity = MusicPlayerActivity.class;
        speaker.say("Music Player");
        setUpButtons();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this::onSongCompletion);
        mSongsManager = SongsManager.initializeSongManager(true);
        playCurrentSong();
    }

    private void setUpButtons() {
        Button btnPlayToggle = (Button) findViewById(R.id.btnPlayToggle);
        Button btnSeek = (Button) findViewById(R.id.btnSeek);
        Button btnPrevSong = (Button) findViewById(R.id.btnPrevSong);
        Button btnNextSong = (Button) findViewById(R.id.btnNextSong);
        Button btnShuffleToggle = (Button) findViewById(R.id.btnShuffleToggle);
        Button btnRepeatToggle = (Button) findViewById(R.id.btnRepeatToggle);
        btnPlayToggle.setOnClickListener((View view) -> {
            if (mMediaPlayer.isPlaying())
                pauseSong();
            else
                resumeSong();
        });
        btnSeek.setOnClickListener((View view) -> seek());
        btnNextSong.setOnClickListener((View view) -> playSong(mSongsManager.getNextSong()));
        btnPrevSong.setOnClickListener((View view) -> playSong(mSongsManager.getPreviousSong()));
        btnShuffleToggle.setOnClickListener((View view) -> {
            mShuffle = !mShuffle;
            if (mShuffle)
                speaker.say("Shuffle turned ON");
            else
                speaker.say("Shuffle turned OFF");
            mSongsManager.reloadSongs(mShuffle);
            playCurrentSong();
        });
        btnRepeatToggle.setOnClickListener((View view) -> {
            mRepeat = !mRepeat;
            if (mRepeat)
                speaker.say("Repeat turned ON");
            else
                speaker.say("Repeat turned OFF");
        });
    }

    @Override
    protected void onDestroy() {
        mMediaPlayer.release();
        AppState.preferredActivity = MainActivity.class;
        super.onDestroy();
    }

    private void playCurrentSong() {
        playSong(mSongsManager.getCurrentSong());
    }

    private void playSong(File song) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(song.toString());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseSong() {
        mMediaPlayer.pause();
        mPausePosition = mMediaPlayer.getCurrentPosition();
    }

    public void resumeSong() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(mPausePosition);
            mMediaPlayer.start();
        }
    }

    public void seek() {
        int seekTo = mMediaPlayer.getCurrentPosition() + 15_000;
        if (seekTo < mMediaPlayer.getDuration()) {
            mMediaPlayer.seekTo(seekTo);
            return;
        }
        if (mRepeat)
            playCurrentSong();
        else
            playSong(mSongsManager.getNextSong());
    }

    private void onSongCompletion(MediaPlayer mp) {
        if (mRepeat)
            playCurrentSong();
        else
            playSong(mSongsManager.getNextSong());
    }
}