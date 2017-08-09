package com.nac.mediademo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Thanh Nguyen on 3/28/2017.
 */
public class SongAdapter extends BaseAdapter {
    private List<Song> mListSong;
    private Context mContext;

    public SongAdapter(List<Song> mListSong, Context mContext) {
        this.mListSong = mListSong;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListSong.size();
    }

    @Override
    public Song getItem(int position) {
        return mListSong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = View.inflate(mContext, R.layout.item_view, null);
        TextView tvName = (TextView)view.findViewById(R.id.tv_name);
        TextView tvArtist = (TextView)view.findViewById(R.id.tv_artist);

        Song song = mListSong.get(position);
        tvName.setText(song.getName());
        tvArtist.setText(song.getArtist());
        return view;
    }
}
