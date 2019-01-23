package com.example.music_vinh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    Context context;
    ArrayList<Album> albumList;

    public AlbumAdapter(Context context, ArrayList<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view =  layoutInflater.inflate(R.layout.item_album, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Album album = albumList.get(i);
        viewHolder.tvName.setText(album.getName());
        viewHolder.tvNameArtist.setText(album.getNameArtist());
      //  viewHolder.imgAlbum.setImageResource(R.drawable.img_album);

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         ImageView imgAlbum;
         TextView tvName;
         TextView tvNameArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAlbum = itemView.findViewById(R.id.imgAlbum);
            tvName = itemView.findViewById(R.id.tvNameAlbum);
            tvNameArtist = itemView.findViewById(R.id.tvNameArtist_A);


        }
    }
}
