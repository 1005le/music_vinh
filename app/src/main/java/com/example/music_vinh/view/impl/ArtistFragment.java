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
import com.example.music_vinh.adapter.ArtistAdapter;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.presenter.impl.ArtistPresenterImpl;
import com.example.music_vinh.view.ArtistView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment implements ArtistView {

    View view;
    RecyclerView artistRecyclerView;

    @Inject
    ArtistPresenterImpl artistPresenter;

    ArtistAdapter artistAdapter;
    ArrayList<Artist> artistList;
    private static final int MY_PERMISSION_REQUEST = 1;

    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist, container, false);
        artistRecyclerView = view.findViewById(R.id.recycleViewArtist);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPresenter();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(getActivity(),

                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(),

                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {

            doStuff();
        }
    }

    private void initPresenter(){
        artistPresenter = new ArtistPresenterImpl(this);

    }
    @Override
    public void showArtist(ArrayList<Artist> artists) {
        artistAdapter = new ArtistAdapter(getActivity(),artists);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
      //  gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        artistRecyclerView.setLayoutManager(gridLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);
    }

    private void doStuff() {
       artistList = new ArrayList<>();
        getMusicArtist();
        artistPresenter.onLoadArtistSuccess(artistList);
    }
    public void getMusicArtist() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

            int idArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int amountAlbum = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int amountSong = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
           // int imgArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ALBUM_ART);
            do {
                String currentId = songCursor.getString(idArtist);
                String currentArtist = songCursor.getString(songArtist);
                String currentAmountAlbum = songCursor.getString(amountAlbum);
                String currentAmountSong = songCursor.getString(amountSong);

               //  String currentImgArtist = songCursor.getString(imgArtist);
                artistList.add(new Artist(Long.parseLong(currentId),currentArtist, Integer.parseInt(currentAmountAlbum),
                       Integer.parseInt(currentAmountSong),""));

            } while (songCursor.moveToNext());
        }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                } else {
                    Toast.makeText(getContext(), "No permission granted!", Toast.LENGTH_SHORT).show();
                    // finish();
                }
                return;

            }
        }
    }

}
