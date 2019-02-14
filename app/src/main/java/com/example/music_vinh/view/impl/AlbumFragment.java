package com.example.music_vinh.view.impl;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.AlbumAdapter;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.presenter.AlbumPresenter;
import com.example.music_vinh.presenter.impl.AlbumPresenterImpl;
import com.example.music_vinh.view.AlbumView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment implements AlbumView {

     View view;

    @BindView(R.id.recycleViewAlbum)
     RecyclerView albumRecyclerView;

      @Inject
      AlbumPresenter albumPresenter;

     static AlbumAdapter albumAdapter;
    public static ArrayList<Album> albumList;

    private static final int MY_PERMISSION_REQUEST = 1;
    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_album, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        initPresenter();
        doStuff();
    }

    private void initPresenter(){
       albumPresenter = new AlbumPresenterImpl(this);
    }
    @Override
    public void showAlbum(ArrayList<Album> albums) {
        albumAdapter = new AlbumAdapter(getActivity(),albums);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        albumRecyclerView.setLayoutManager(gridLayoutManager);
        albumRecyclerView.setAdapter(albumAdapter);

    }

    private void doStuff() {
        albumList = new ArrayList<>();
        albumList = getAlbum();
        albumPresenter.loadAlbums();
    }
    public ArrayList<Album> getAlbum() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int idAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int imgAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int amountSong = songCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
            do {
                String currentId = songCursor.getString(idAlbum);
                String currentAlbum = songCursor.getString(songAlbum);
                String currentArtist = songCursor.getString(songArtist);
                String currentImages = songCursor.getString(imgAlbum);
                String currentamountSong = songCursor.getString(amountSong);

                albumList.add(new Album(Long.parseLong(currentId),currentAlbum,currentArtist,
                        currentImages,Integer.parseInt(currentamountSong)));

            } while (songCursor.moveToNext());
        }
        return albumList;
    }
}
