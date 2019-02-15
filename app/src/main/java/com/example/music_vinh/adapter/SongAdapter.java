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

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    Context context;
    List<Song> songList;

    private AdapterView.OnItemClickListener itemClickListener;

    public SongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_song, viewGroup,false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder viewHolder,final int i) {

        Song song = songList.get(i);
        //  Log.d("hello3",songList.size()+"\n"+songList.get(3).getName()+"\n"+song.getNameArtist());
        viewHolder.tvNameSong.setText(song.getName());
        viewHolder.tvNameArtist.setText(song.getNameArtist());

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, PlayActivity.class);
//                intent.putExtra("song", songList.get(i));
//                intent.putExtra("arrSong",(ArrayList) songList);
//                context.startActivity(intent);
//
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvName)
        TextView tvNameSong;

        @BindView(R.id.tvNameArtist)
        TextView tvNameArtist;

        @BindView(R.id.imgSong)
        ImageView imgSong;

       // ImageButton imgPlay, imgPause;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void getFilte(List<Song> listItem){
        songList = new ArrayList<>();
        songList.addAll(listItem);
        notifyDataSetChanged();
    }
}
