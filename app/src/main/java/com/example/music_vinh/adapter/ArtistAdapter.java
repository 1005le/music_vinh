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
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.view.impl.AlbumInfoActivity;
import com.example.music_vinh.view.impl.ArtistInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    Context context;
    List<Artist> artistList;

    public ArtistAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_artist, viewGroup, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder viewHolder, final int i) {

        Artist artist = artistList.get(i);
        viewHolder.tvNameArtist.setText(artist.getName());
        viewHolder.tvAmountAlbum.setText(artist.getAmountAlbum()+" albums");
        viewHolder.tvAmountSong.setText(artist.getAmountSong()+" songs");

        Drawable img = Drawable.createFromPath(artist.getImages());
        viewHolder.imgArtist.setImageDrawable(img);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ArtistInfoActivity.class);
                intent.putExtra("artistArrayList",artistList.get(i));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgArtist)
        ImageView imgArtist;

        @BindView(R.id.tvNameArtist)
        TextView tvNameArtist;

       @BindView(R.id.tvAmountAlbum)
        TextView tvAmountAlbum;

        @BindView(R.id.tvAmountSong)
        TextView tvAmountSong;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void getFilte(List<Artist> listItem){
        artistList = new ArrayList<>();
        artistList.addAll(listItem);
        notifyDataSetChanged();
    }
}
