package com.tangledbytes.j2controller.music;

import static com.tangledbytes.j2controller.utils.Constants.speaker;

import android.media.MediaPlayer;
import android.util.Log;

import com.tangledbytes.j2controller.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

//TODO: Add restart button
public class MusicPlayer {
    private static final String TAG = "MusicPlayer";
    List<File> songs;
    MediaPlayer mediaPlayer;
    private boolean shuffle = true;
    private boolean repeat;
    private int currentSongIndex = 0;
    private int pausePosition = 0;

    public MusicPlayer() throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            if (isRepeat())
                mp.start();
            else {
                try {
                    playNextSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void playNextSong() throws IOException {
        currentSongIndex++;
        if (currentSongIndex >= songs.size()) {
            if (isShuffle()) {
                restartPlayer();
                return;
            } else {
                speaker.say("Starting playlist from beginning");
                currentSongIndex = 0;
            }
        }
        playSong(currentSongIndex);
    }

    public void playSong(int songIndex) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(songs.get(songIndex).toString());
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    public void resume() {
        if (!isPlaying()) {
            mediaPlayer.seekTo(pausePosition);
            mediaPlayer.start();
        }
    }

    public void seekTo(int to) throws IOException {
        if (to >= mediaPlayer.getDuration())
            playNextSong();
        else
            mediaPlayer.seekTo(to);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
            pausePosition = mediaPlayer.getCurrentPosition();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void restartPlayer() throws IOException {
        restartPlayer(false);
    }

    public void restartPlayer(boolean remainPaused) throws IOException {
        File songsDir = new File(Constants.EXT_SONGS_DIR);
        songs = new ArrayList<>();
        for (File file : Objects.requireNonNull(songsDir.listFiles()))
            if (file.getName().endsWith(".mp3"))
                songs.add(file);
        if (isShuffle())
            Collections.shuffle(songs);
        else
            Collections.sort(songs);
        currentSongIndex = 0;
        seekTo(0);
        if (!remainPaused)
            playSong(currentSongIndex);
        else
            mediaPlayer.reset();
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void playPrevSong() throws IOException {
        if (currentSongIndex != 0)
            currentSongIndex--;
        playSong(currentSongIndex);
    }

    public void destroyPlayer() {
        mediaPlayer.reset();
    }
}
