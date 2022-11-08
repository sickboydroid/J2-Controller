package com.tangledbytes.j2controller.music;

import static com.tangledbytes.j2controller.utils.AppState.speaker;

import com.tangledbytes.j2controller.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SongsManager {
    public static final String TAG = SongsManager.class.getSimpleName();
    // Index starts from zero and goes up to mSongs.size()
    private int mCurrentSongIndex;
    private List<File> mSongs;

    private SongsManager() {
    }

    public static SongsManager initializeSongManager(boolean shuffle) {
        SongsManager songsManager = new SongsManager();
        songsManager.reloadSongs(shuffle);
        return songsManager;
    }

    public void reloadSongs(boolean shuffle) {
        File songsDir = new File(Constants.EXT_SONGS_DIR);
        mSongs = new ArrayList<>();
        for (File file : Objects.requireNonNull(songsDir.listFiles()))
            if (file.getName().endsWith(".mp3"))
                mSongs.add(file);
        if (shuffle)
            Collections.shuffle(mSongs);
        else
            Collections.sort(mSongs);
        mCurrentSongIndex = 0;
    }

    public File getCurrentSong() {
        return mSongs.get(mCurrentSongIndex);
    }

    public File getNextSong() {
        mCurrentSongIndex++;
        if (mCurrentSongIndex >= mSongs.size()) {
            speaker.say("Replaying songs");
            mCurrentSongIndex = 0;
        }
        return mSongs.get(mCurrentSongIndex);
    }

    public File getPreviousSong() {
        if (mCurrentSongIndex == 0)
            return mSongs.get(0);
        return mSongs.get(--mCurrentSongIndex);
    }

    public int getTotalSongs() {
        return mSongs.size();
    }

    public boolean isReady() {
        return mSongs != null && mSongs.size() > 0;
    }
}
