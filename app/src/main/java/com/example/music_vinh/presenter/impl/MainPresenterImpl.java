package com.example.music_vinh.presenter.impl;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainPresenterImpl implements MainInteractor, MainPresenter {
    private MainInteractorImpl mainInteractorImpl;
    private MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        //mainInteractorImpl = new MainInteractorImpl(this);
    }
    @Inject
    public MainPresenterImpl() {
    }
//    @Override
//    public void onLoadSongSuccess(List<Song> songs) {
//        mainView.showSong(songs);
//    }
//    @Override
//    public void loadData(Context context) {
//        mainInteractorImpl.getSongCategories(this, context);
//    }
    @Override
    public void loadData(Context context) {
       mainView.showSong(getMusicSongArr(context));
    }

    public List<Song> getMusicSongArr(Context context) {
        List<Song> songList = new ArrayList<>();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = context.getContentResolver().query(songUri, null,
                null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                String currentId = songCursor.getString(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentAlbum = songCursor.getString(songAlbum);
                String currentPath = songCursor.getString(songPath);
                String currentDuration = songCursor.getString(songDuration);

                songList.add(new Song(Long.parseLong(currentId),currentTitle, currentArtist,currentAlbum,currentPath, Long.parseLong(currentDuration)));

            } while (songCursor.moveToNext());
        }
        return songList;
    }
}
