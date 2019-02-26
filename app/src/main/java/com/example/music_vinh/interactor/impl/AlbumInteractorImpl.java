package com.example.music_vinh.interactor.impl;

import android.content.Context;
import android.os.Handler;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.AlbumFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class AlbumInteractorImpl implements AlbumInteractor{
    private AlbumInteractor albumInteractor;

    @Inject
    public AlbumInteractorImpl() {
    }
    public AlbumInteractorImpl(AlbumInteractor albumInteractor){
        this.albumInteractor = albumInteractor;
    }
    private  ArrayList<Album> createArrayList() {
        ArrayList<Album> albums = AlbumFragment.albumList;

        return albums;
    }
    public void getAlbumCategories(final AlbumInteractor albumInteractor) {
                albumInteractor.onLoadAlbumSuccess(createArrayList());
    }
    @Override
    public void onLoadAlbumSuccess(ArrayList<Album> albums) {

    }
}
