package com.example.music_vinh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.PlayActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongInArtistAdapter extends RecyclerView.Adapter<SongInArtistAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;

    public SongInArtistAdapter(Context context, ArrayList<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_song_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
       Song song = songArrayList.get(position);
       holder.imgIconSong.setImageResource(R.drawable.ic_song_artist);
       holder.tvNameSongInArtist.setText(song.getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
       holder.tvDuration.setText(simpleDateFormat.format(song.getDuration()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("song", songArrayList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgIconSong)
         ImageView imgIconSong;

         @BindView(R.id.tvNameSongInArtist)
         TextView tvNameSongInArtist;

         @BindView(R.id.tvDuration)
         TextView tvDuration;
        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
