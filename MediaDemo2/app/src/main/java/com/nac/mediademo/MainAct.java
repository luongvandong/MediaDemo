package com.nac.mediademo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Thanh Nguyen on 3/28/2017.
 */
public class MainAct extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final int LEVEL_PAUSE = 1;
    private static final int LEVEL_PLAY = 0;
    private static final int ID = 873264238;

    private static final int REQUEST_DELETE = 102;

    private MediaManager mediaMgr;
    private ListView mLvSong;
    private SongAdapter mSongAdapter;
    //private MediaPlayer mPlayer;
    private ImageView ivBack, ivPlay, ivNext, ivStop;
    private TextView tvName, tvArtist, tvIndex, tvTime;
    private View oldView;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initViews();
    }

    private void initViews() {
        mediaMgr = new MediaManager(getApplicationContext());
        mLvSong = (ListView) findViewById(R.id.lv_song);
        ivBack = (ImageView) findViewById(R.id.iv_previous);
        ivNext = (ImageView) findViewById(R.id.iv_next);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivStop = (ImageView) findViewById(R.id.iv_stop);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvIndex = (TextView) findViewById(R.id.tv_index);
        tvTime = (TextView) findViewById(R.id.tv_time);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        mSongAdapter = new SongAdapter(mediaMgr.getListSong(),this);
        mLvSong.setAdapter(mSongAdapter);
        mLvSong.setOnItemClickListener(this);
        ivStop.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivPlay.setOnClickListener(this);

        updateInfoSong();
        ivPlay.setImageLevel(LEVEL_PLAY);
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
        if(mediaMgr.play(position)) {
            if(oldView !=null){
                oldView.setBackgroundColor(Color.parseColor("#00000000"));
            }
            view.setBackgroundColor(Color.parseColor("#FFF9C4"));
            oldView = view;
            updateInfoSong();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play:
                if(mediaMgr.play() == false){
                    ivPlay.setImageLevel(LEVEL_PLAY);
                }else{
                    updateInfoSong();
                }
                break;
            case R.id.iv_next:
                if(mediaMgr.next()){
                    updateInfoSong();
                }
                break;
            case R.id.iv_previous:
                if(mediaMgr.back()){
                    updateInfoSong();
                }
                break;
            case R.id.iv_stop:
                mediaMgr.stop();
                ivPlay.setImageLevel(LEVEL_PLAY);
                break;
            default:
                break;
        }
    }

    private void updateInfoSong() {
        Song song = mediaMgr.getCurrentSong();
        tvName.setText(song.getName());
        tvArtist.setText(song.getArtist());
        tvIndex.setText(mediaMgr.getIndexText());
        tvTime.setText("00:00/" + mediaMgr.getDuration(song.getDuration()));
        ivPlay.setImageLevel(LEVEL_PAUSE);
        seekBar.setMax(song.getDuration());
        new UpdateSeekBar().execute();
        //showNotification(song);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaMgr.seek(seekBar.getProgress());
    }

    private class UpdateSeekBar extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while(mediaMgr.isStarted()){
                try {
                    Thread.sleep(1000);
                    publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            tvTime.setText(mediaMgr.getCurrentTimeText());
            seekBar.setProgress(mediaMgr.getCurrentTime());
        }
    }

    public void showNotification(Song song){
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_notifi);
        builder.setContentTitle(song.getName());
        builder.setContentText(song.getArtist());
        builder.setSubText(song.getAlbum());

        Notification notification = builder.build();
        NotificationManager notifiMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent deleteIntent = new Intent(this, MainAct.class);
        PendingIntent piDelete = PendingIntent.getBroadcast(this,
                REQUEST_DELETE, deleteIntent,
                PendingIntent.FLAG_ONE_SHOT);
        builder.setDeleteIntent(piDelete);
        notifiMgr.notify(ID, notification);
    }

    @Override
    public void onBackPressed() {
        showNotification(mediaMgr.getCurrentSong());
        super.onBackPressed();
    }
}
