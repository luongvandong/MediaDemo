package com.nac.mediademo;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thanh Nguyen on 3/28/2017.
 */
public class MediaManager {
    private static final String TAG = MediaManager.class.getName();
    private List<Song> mListSong;
    private Context mContext;

    public MediaManager(Context mContext) {
        this.mContext = mContext;
        initData();
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
        String where = MediaStore.Audio.AudioColumns.DISPLAY_NAME +" LIKE '%.mp3'";
        Cursor c = mContext.getContentResolver().query(audioUri, projections,
                where, null, null);

        if(c == null){
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
        while (c.isAfterLast()==false){
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

    public MediaPlayer initMediaPlayerByRawId(int soundId, boolean isLooping){
        MediaPlayer player = MediaPlayer.create(mContext, soundId);
        player.setLooping(isLooping);
        return player;
    }

    public List<Song> getListSong() {
        return mListSong;
    }
}
