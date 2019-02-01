package com.example.music_vinh.adapter;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.SortActivity;

import java.io.IOException;
import java.util.ArrayList;

public class SortSongAdapter extends RecyclerView.Adapter<SortSongAdapter.ViewHolder> {

    static Context context;
    static ArrayList<Song> songList;
    static MediaPlayer mediaPlayer;

    public SortSongAdapter(Context context, ArrayList<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_sort, parent,false);

        return new SortSongAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Song song = songList.get(position);
        //  Log.d("hello3",songList.size()+"\n"+songList.get(3).getName()+"\n"+song.getNameArtist());
        holder.tvNameSort.setText(song.getName());

//        boolean isSelectedAfterClick = !holder.tvNameSort.isSelected();
//        holder.tvNameSort.setSelected(isSelectedAfterClick);

        holder.tvNameArtistSort.setText(song.getNameArtist());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSort, imgSongSort;
        TextView tvNameSort, tvNameArtistSort;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSort = itemView.findViewById(R.id.imgSort);
            imgSongSort = itemView.findViewById(R.id.imgSongSort);
            tvNameSort = itemView.findViewById(R.id.tvNameSort);
            tvNameArtistSort = itemView.findViewById(R.id.tvNameArtistSort);

            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                   // tvNameSort.setT
                    boolean isSelectedAfterClick = !view.isSelected();
                    view.setSelected(isSelectedAfterClick);

                    SortActivity.tvNameSongBottom.setText(songList.get(getPosition()).getName());
                    SortActivity.tvNameArtistBottom.setText(songList.get(getPosition()).getNameArtist());
                    SortActivity.imgButtonPauseBottom.setImageResource(R.drawable.ic_pause);

                    SortActivity.imgButtonPauseBottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SortActivity.imgButtonPauseBottom.setImageResource(R.drawable.ic_stop);
                             mediaPlayer.stop();
                        }
                    });

                    Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            songList.get(getPosition()).getId());

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    try {
                        mediaPlayer.setDataSource(context, contentUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                }
            });
        }
    }
}
