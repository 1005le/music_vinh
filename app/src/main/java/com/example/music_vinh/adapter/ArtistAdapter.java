package com.example.music_vinh.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.view.impl.AlbumInfoActivity;
import com.example.music_vinh.view.impl.ArtistInfoActivity;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    Context context;
    ArrayList<Artist> artistList;

    public ArtistAdapter(Context context, ArrayList<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_artist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Artist artist = artistList.get(i);
        viewHolder.tvNameArtist.setText(artist.getName());
        viewHolder.tvAmountAlbum.setText(artist.getAmountAlbum()+" albums");
        viewHolder.tvAmountSong.setText(artist.getAmountSong()+" songs");

        Drawable img = Drawable.createFromPath(artist.getImages());
        viewHolder.imgArtist.setImageDrawable(img);

    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgArtist;
        TextView tvNameArtist;
        TextView tvAmountAlbum, tvAmountSong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgArtist = itemView.findViewById(R.id.imgArtist);
            tvNameArtist = itemView.findViewById(R.id.tvNameArtist);
            tvAmountAlbum = itemView.findViewById(R.id.tvAmountAlbum);
            tvAmountSong = itemView.findViewById(R.id.tvAmountSong);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ArtistInfoActivity.class);
                    intent.putExtra("artistArrayList",artistList.get(getPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }
}
