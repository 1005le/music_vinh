package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

import javax.inject.Inject;

public class PlaySongInteractorImpl {

    private PlaySongInteractor playSongInteractor;

    @Inject
    public PlaySongInteractorImpl(){

    }
    public PlaySongInteractorImpl(PlaySongInteractor playSongInteractor){
        this.playSongInteractor = playSongInteractor;
    }

    public void createSong(){
        ArrayList<Song> songArrayList= new ArrayList<>();
        playSongInteractor.onLoadSongSuccess(songArrayList);
    }
}
