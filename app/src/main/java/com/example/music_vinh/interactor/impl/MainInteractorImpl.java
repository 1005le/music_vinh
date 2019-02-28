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


public class MainInteractorImpl implements MainInteractor{
    Context context;
    @Inject
    public MainInteractorImpl(MainInteractor mainInteractor){

    }
}
