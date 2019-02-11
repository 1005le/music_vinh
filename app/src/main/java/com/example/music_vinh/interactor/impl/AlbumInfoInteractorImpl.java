package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.view.impl.AlbumInfoActivity;
import com.example.music_vinh.view.impl.SongFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class AlbumInfoInteractorImpl implements AlbumInfoInteractor{

    private AlbumInfoInteractor albumInfoInteractor;

     @Inject
     public AlbumInfoInteractorImpl(){

     }
    public AlbumInfoInteractorImpl(AlbumInfoInteractor albumInfoInteractor){
        this.albumInfoInteractor = albumInfoInteractor;
    }
    private  ArrayList<Song> createArrayList() {
        ArrayList<Song> songs = AlbumInfoActivity.songArrayList;
        return songs;
    }

    public void getSongCategories(final AlbumInfoInteractor albumInfoInteractor) {
        albumInfoInteractor.onLoadSongSuccess(createArrayList());
    }

    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {

    }
}
