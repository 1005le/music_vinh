package com.example.music_vinh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public class SortSongAdapter extends RecyclerView.Adapter<SortSongAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> songList;

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
        holder.tvNameArtistSort.setText(song.getNameArtist());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSort, imgSongSort;
        TextView tvNameSort, tvNameArtistSort;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSort = itemView.findViewById(R.id.imgSort);
            imgSongSort = itemView.findViewById(R.id.imgSongSort);
            tvNameSort = itemView.findViewById(R.id.tvNameSort);
            tvNameArtistSort = itemView.findViewById(R.id.tvNameArtistSort);
        }
    }
}
