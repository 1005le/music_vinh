package com.example.music_vinh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.PlayActivity;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> songList;

    private AdapterView.OnItemClickListener itemClickListener;

    public SongAdapter(Context context, ArrayList<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_song, viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Song song = songList.get(i);
        //  Log.d("hello3",songList.size()+"\n"+songList.get(3).getName()+"\n"+song.getNameArtist());
        viewHolder.tvNameSong.setText(song.getName());
        viewHolder.tvNameArtist.setText(song.getNameArtist());
      //  viewHolder.tvNameArtist.setText(song.getPath());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvNameSong, tvNameArtist;
        ImageView imgSong;
       // ImageButton imgPlay, imgPause;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameSong = itemView.findViewById(R.id.tvName);
            tvNameArtist = itemView.findViewById(R.id.tvNameArtist);

            imgSong = itemView.findViewById(R.id.imgSong);
          //  imgPlay = itemView.findViewById(R.id.imgPlay);
          //  imgPause = itemView.findViewById(R.id.imgPause);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayActivity.class);
                    intent.putExtra("song", songList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
