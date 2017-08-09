package com.nac.mediademo;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Thanh Nguyen on 3/28/2017.
 */
public class MainAct extends Activity implements AdapterView.OnItemClickListener {
    private MediaManager mediaMgr;
    private ListView mLvSong;
    private SongAdapter mSongAdapter;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initViews();
    }

    private void initViews() {
        mediaMgr = new MediaManager(getApplicationContext());
        mLvSong = (ListView) findViewById(R.id.lv_song);
        mSongAdapter = new SongAdapter(mediaMgr.getListSong(),this);
        mLvSong.setAdapter(mSongAdapter);
        mLvSong.setOnItemClickListener(this);
        //startMediaPlayer1();
    }

    private void startMediaPlayer1() {
        MediaPlayer player1 = mediaMgr.initMediaPlayerByRawId(
                R.raw.chuc_mung_vuot_moc_01_0,
                false);
        MediaPlayer player2 = mediaMgr.initMediaPlayerByRawId(
                R.raw.background_music,
                true);
        player2.start();
        player1.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mPlayer!=null){
            mPlayer.reset();
        }
        Song song = mediaMgr.getListSong().get(position);
        mPlayer = MediaPlayer.create(this, Uri.parse(song.getPath()));
        mPlayer.start();
    }
}
