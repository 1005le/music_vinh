package com.example.music_vinh.interactor.impl;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.SongFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/*
 *- Lớp M: xử lý dữ liệu -> Trả dữ liệu về P thông qua callback
 * */

public class MainInteractorImpl implements MainInteractor {

    private MainInteractor mainInteractor;
         Context context;

    @Inject
    public MainInteractorImpl() {
    }

    public MainInteractorImpl(MainInteractor mainInteractor, Context context) {
        this.mainInteractor = mainInteractor;
        this.context = context;
    }

    public MainInteractorImpl(MainInteractor mainInteractor){
        this.mainInteractor = mainInteractor;
    }

    private  ArrayList<Song> createArrayList() {
        ArrayList<Song> songs = SongFragment.songList;
       return songs;
    }

    public void getSongCategories(final MainInteractor mainInteractor) {
                mainInteractor.onLoadSongSuccess(createArrayList());
    }

    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {

    }
}
