package com.example.music_vinh.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.utils.Constants;
import com.example.music_vinh.view.impl.PlayActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    Context context;
    List<Song> songList;

    private int type;
    public OnSongItemClickListener onSongItemClickListener;

    public SongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
        this.type = Constants.VIEW_LIST;
    }
    public void setSongs(List<Song> songs) {
        songList = songs;
    }

    public void addData(List<Song> songs) {
        if (songs.size() > 0) {
            songList.addAll(songs);
            notifyDataSetChanged();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int mViewType) {
        this.type = mViewType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (getType()) {
            case Constants.VIEW_GRID:
                return new SongViewHolder(
                        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song_grid, viewGroup, false));
            case Constants.VIEW_LIST:
            default:
                return new SongViewHolder(
                        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song, viewGroup, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (type == Constants.VIEW_LIST) {
            return Constants.VIEW_LIST;
        } else {
            return Constants.VIEW_GRID;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder viewHolder,final int i) {

        Song song = songList.get(i);
        viewHolder.tvNameSong.setText(song.getName());
        viewHolder.tvNameArtist.setText(song.getNameArtist());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSongItemClickListener.onSongItemClicked(v,i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvName) TextView tvNameSong;

        @BindView(R.id.tvNameArtist) TextView tvNameArtist;

        @BindView(R.id.imgSong) ImageView imgSong;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnSongItemClickListener {
        void onSongItemClicked(View view, int position);
    }

    public void setOnSongItemClickListener(OnSongItemClickListener onSongItemClickListener) {
        this.onSongItemClickListener = onSongItemClickListener;
    }
}
