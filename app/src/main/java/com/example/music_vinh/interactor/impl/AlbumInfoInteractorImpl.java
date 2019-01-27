package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;

import java.util.ArrayList;

public class AlbumInfoInteractorImpl {

    private AlbumInfoInteractor albumInfoInteractor;

    public AlbumInfoInteractorImpl(AlbumInfoInteractor albumInfoInteractor){
        this.albumInfoInteractor = albumInfoInteractor;
    }

    public void createSong(){
        ArrayList<Song> songArrayList= new ArrayList<>();
        albumInfoInteractor.onLoadSongSuccess(songArrayList);
    }
}
