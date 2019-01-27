package com.example.music_vinh.interactor.impl;

import android.content.Context;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public class AlbumInteractorImpl {
    private AlbumInteractor albumInteractor;

    public AlbumInteractorImpl(AlbumInteractor albumInteractor){
        this.albumInteractor = albumInteractor;
    }

    public void createAlbum(){
        ArrayList<Album> albums = new ArrayList<>();
        albumInteractor.onLoadAlbumSuccess(albums);
    }

}
