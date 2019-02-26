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
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.impl.AlbumInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    Context context;
    List<Album> albumList;
    private int type;
    private OnItemClickListener mOnItemClickLister;

    public AlbumAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    public int getType() {
        return type;
    }

    public void setType(int mType) {
        this.type = mType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (getType()) {
            case Constants.VIEW_GRID:
                return new AlbumViewHolder(
                        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album, viewGroup, false));
            case Constants.VIEW_LIST:
            default:
                return new AlbumViewHolder(
                        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album_list, viewGroup, false));
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
    public void onBindViewHolder(@NonNull AlbumViewHolder viewHolder, final int i) {

        Album album = albumList.get(i);
        viewHolder.tvName.setText(album.getName());
        viewHolder.tvNameArtist.setText(album.getNameArtist());

        Drawable img = Drawable.createFromPath(album.getImages());
        viewHolder.imgAlbum.setImageDrawable(img);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickLister = listener;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgAlbum) ImageView imgAlbum;
        @BindView(R.id.tvNameAlbum) TextView tvName;
        @BindView(R.id.tvNameArtist_A) TextView tvNameArtist;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
