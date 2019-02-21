package com.example.music_vinh.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.impl.AlbumInfoActivity;
import com.example.music_vinh.view.impl.MainActivity;
import com.example.music_vinh.view.impl.PlayActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongInAlbumAdapter extends RecyclerView.Adapter<SongInAlbumAdapter.SongInAlbumViewHolder> {
    Context context;
    List<Song> songArrayList;
    Album album;

    public SongInAlbumAdapter(Context context, List<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }

    @Override
    public SongInAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_song_in_album,parent,false);
        return new SongInAlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongInAlbumViewHolder holder, final int position) {

         Song song = songArrayList.get(position);
         holder.tvNameSong.setText(song.getName());
         holder.tvNameAlbumInSong.setText(song.getNameAlbum());
         holder.imgSongA.setImageResource(R.drawable.ic_song);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(context, PlayActivity.class);
//                intent.putExtra("song", songArrayList.get(position));
//                intent.putExtra("arrSong",(ArrayList) songArrayList);
//                context.startActivity(intent);

//                Intent intent = new Intent(context, PlayActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList(Constants.KEY_SONGS,(ArrayList<? extends Parcelable>) songArrayList);
//                bundle.putInt(Constants.KEY_POSITION,position);
//                // intent.putExtra(Constants.KEY_PROGESS,currentPosition);
//                intent.putExtra(Constants.KEY_BUNDLE,bundle);

//                Intent intent = new Intent(context, PlayActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList(Constants.KEY_SONGS,(ArrayList<? extends Parcelable>) songArrayList);
//                bundle.putInt(Constants.KEY_POSITION, position);
//                Log.d("albumA", songArrayList.get(position).getName());
//                intent.putExtra(Constants.KEY_BUNDLE, bundle);
//                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class SongInAlbumViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgSongA)
        ImageView imgSongA;

        @BindView(R.id.tvNameSong)
          TextView tvNameSong;

        @BindView(R.id.tvNameAlbumInSong)
          TextView tvNameAlbumInSong;

        public SongInAlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
