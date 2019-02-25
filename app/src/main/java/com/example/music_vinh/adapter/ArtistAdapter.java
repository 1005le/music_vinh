package com.example.music_vinh.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.impl.AlbumInfoActivity;
import com.example.music_vinh.view.impl.ArtistInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    Context context;
    List<Artist> artistList;
    private int type;

    public ArtistAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }
    public int getViewType() {
        return type;
    }

    public void setViewType(int mViewType) {
        this.type = mViewType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (getViewType()) {
            case Constants.VIEW_GRID:
                return new ArtistViewHolder(
                        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, viewGroup, false));
            case Constants.VIEW_LIST:
            default:
                return new ArtistViewHolder(
                        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist_list, viewGroup, false));
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (type == Constants.VIEW_LIST) {
            return Constants.VIEW_LIST;
        } else {
            return Constants.VIEW_GRID;
        }
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
                intent.putExtra("index",i);
               // intent.putExtra("album_ID",artistList.get(i).getId());
//                Log.d("truyen",artistList.get(i).getId()+"");
                intent.putExtra("artistArrayList",(ArrayList) artistList);
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
}
