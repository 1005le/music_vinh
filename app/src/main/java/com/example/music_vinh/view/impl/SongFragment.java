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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends Fragment implements MainView {
    View view;
    RecyclerView songRecyclerView;
    SongAdapter songAdapter;
    ArrayList<Song> songList;
   //Presenter
    private MainPresenterImpl mainPresenter;
    private static final int MY_PERMISSION_REQUEST = 1;

    public SongFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_song, container, false);
        songRecyclerView = view.findViewById(R.id.recycleViewSong);
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
        mainPresenter = new MainPresenterImpl(this);

     }

    @Override
    public void showSong(ArrayList<Song>songs) {
        // songs = new ArrayList<>();
        songAdapter = new SongAdapter(getActivity(),songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        songRecyclerView.setLayoutManager(linearLayoutManager);
        songRecyclerView.setAdapter(songAdapter);
    }

    private void doStuff() {
        songList = new ArrayList<>();
        getMusicSong();
        mainPresenter.onLoadSongSuccess(songList);
    }
    public void getMusicSong() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        //    int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
          // int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
             //   String currentAlbum = songCursor.getString(songAlbum);
            //    String currentPath = songCursor.getString(songPath);

                songList.add(new Song(currentTitle, currentArtist));
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
