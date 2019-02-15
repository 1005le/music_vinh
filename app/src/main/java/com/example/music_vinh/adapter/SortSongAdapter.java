package com.example.music_vinh.adapter;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.SortActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SortSongAdapter extends RecyclerView.Adapter<SortSongAdapter.SortSongViewHolder> {

    static Context context;
    static List<Song> songList;
    static MediaPlayer mediaPlayer;

    @BindView(R.id.tvNameSong)
    TextView tvNameSongBottom ;
    @BindView(R.id.tvNameArtist)
    TextView tvNameArtistBottom;
    @BindView(R.id.imgButtonPause)
    ImageButton imgButtonPauseBottom;


    public SortSongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public SortSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_sort, parent,false);

        return new SortSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SortSongViewHolder holder, final int position) {

        Song song = songList.get(position);
        //  Log.d("hello3",songList.size()+"\n"+songList.get(3).getName()+"\n"+song.getNameArtist());
        holder.tvNameSort.setText(song.getName());
        holder.tvNameArtistSort.setText(song.getNameArtist());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // holder.tvNameSort.setTextColor(R.color.colorAccent);
//                tvNameSongBottom.setText(songList.get(position).getName());
//                tvNameArtistBottom.setText(songList.get(position).getNameArtist());
//                imgButtonPauseBottom.setImageResource(R.drawable.ic_pause);

//                imgButtonPauseBottom.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        imgButtonPauseBottom.setImageResource(R.drawable.ic_stop);
//                        mediaPlayer.stop();
//                    }
//                });

//                Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                        songList.get(position).getId());
//
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//                try {
//                    mediaPlayer.setDataSource(context, contentUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    mediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mediaPlayer.start();
           }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SortSongViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgSort)
        ImageView imgSort;

        @BindView(R.id.imgSongSort)
        ImageView imgSongSort;

        @BindView(R.id.tvNameSort)
        TextView tvNameSort;

        @BindView(R.id.tvNameArtistSort)
        TextView tvNameArtistSort;

        public SortSongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
