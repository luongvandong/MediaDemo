package com.nac.mediademo;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Thanh Nguyen on 3/28/2017.
 */
public class MediaManager {
    private static final String TAG = MediaManager.class.getName();
    private List<Song> mListSong;
    private Context mContext;

    public static final int IDE = 1;
    public static final int PLAYING = 2;
    public static final int STOPPED = 3;
    public static final int PAUSED = 4;
    private int mState = IDE;
    private MediaPlayer mPlayer;
    private int mIndex = 0;
    private boolean isShuffle;

    public MediaManager(Context mContext) {
        this.mContext = mContext;
        initData();
        mPlayer = new MediaPlayer();
    }

    private void initData() {
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String projections[] = new String[]{
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.DURATION
        };
        String where = MediaStore.Audio.AudioColumns.DISPLAY_NAME + " LIKE '%.mp3'";
        Cursor c = mContext.getContentResolver().query(audioUri, projections,
                where, null, null);

        if (c == null) {
            Log.e(TAG, "Error: Could not get audio list...");
            return;
        }
        c.moveToFirst();
        int indexTitle = c.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
        int indexData = c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        int indexAlbum = c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
        int indexArtist = c.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
        int indexDuration = c.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);

        String name, path, album, artist;
        int duration;
        mListSong = new ArrayList<>();
        while (c.isAfterLast() == false) {
            name = c.getString(indexTitle);
            path = c.getString(indexData);
            album = c.getString(indexAlbum);
            artist = c.getString(indexArtist);
            duration = c.getInt(indexDuration);
            mListSong.add(new Song(name, path, album, artist, duration));
            c.moveToNext();
        }
        c.close();
        Log.i(TAG, mListSong.toString());
    }

    public MediaPlayer initMediaPlayerByRawId(int soundId, boolean isLooping) {
        MediaPlayer player = MediaPlayer.create(mContext, soundId);
        player.setLooping(isLooping);
        return player;
    }

    public List<Song> getListSong() {
        return mListSong;
    }

    public boolean play() {
        try {
            if (mState == IDE || mState == STOPPED) {
                Song song = mListSong.get(mIndex);
                mPlayer.setDataSource(song.getPath());
                mPlayer.prepare();
                mPlayer.start();
                mState = PLAYING;
                return true;
            } else if (mState == PLAYING) {
                pause();
                return false;
            } else if (mState == PAUSED) {
                mPlayer.start();
                mState = PLAYING;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void pause() {
        mPlayer.pause();
        mState = PAUSED;
    }

    public boolean next() {
        if (isShuffle) {
            mIndex = new Random().nextInt(mListSong.size());
        } else {
            mIndex = (mIndex + 1) % mListSong.size();
        }
        stop();
        return play();
    }

    public boolean back() {
        if (mIndex == 0) {
            mIndex = mListSong.size();
        }
        mIndex--;
        stop();
        return play();
    }

    public void stop() {
        if (mState == PLAYING || mState == PAUSED) {
            mPlayer.stop();
            mPlayer.reset();
            mState = STOPPED;
        }
    }

    public void setShuffle(boolean stateShuffle) {
        isShuffle = stateShuffle;
    }

    public Song getCurrentSong() {
        return mListSong.get(mIndex);
    }

    public String getIndexText() {
        return (mIndex + 1) + "/" + mListSong.size();
    }

    public String getDuration(int duration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(duration));
    }

    public boolean play(int position) {
        stop();
        mIndex = position;
        return play();
    }

    public int getIndex() {
        return mIndex;
    }

    public boolean isStarted() {
        return mState == PLAYING || mState == PAUSED;
    }

    public String getCurrentTimeText() {
        int currentTime = mPlayer.getCurrentPosition();
        int totalTime = mListSong.get(mIndex).getDuration();

        return getDuration(currentTime) + "/" + getDuration(totalTime);
    }

    public int getCurrentTime() {
        return mPlayer.getCurrentPosition();
    }

    public void seek(int progress){
        mPlayer.seekTo(progress);
    }
}
