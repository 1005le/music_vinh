package com.example.music_vinh.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public class SongInAlbumAdapter extends RecyclerView.Adapter<SongInAlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;
    Album album;

    public SongInAlbumAdapter(Context context, ArrayList<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_song_in_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

         Song song = songArrayList.get(position);
         holder.tvNameSong.setText(song.getName());
         holder.tvNameAlbumInSong.setText(song.getNameAlbum());
         holder.imgSongA.setImageResource(R.drawable.ic_song);
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

          ImageView imgSongA;
          TextView tvNameSong;
          TextView tvNameAlbumInSong;

        public ViewHolder(View itemView) {
            super(itemView);
            imgSongA = itemView.findViewById(R.id.imgSongA);
            tvNameSong = itemView.findViewById(R.id.tvNameSong);
            tvNameAlbumInSong = itemView.findViewById(R.id.tvNameAlbumInSong);

        }

    }
}
