package com.example.music_vinh.interactor.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.AlbumFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AlbumInteractorImpl implements AlbumInteractor{
    Context context;
    @Inject
    public AlbumInteractorImpl() {
    }
}
