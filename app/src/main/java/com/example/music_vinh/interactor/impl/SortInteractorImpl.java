package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.interactor.SortInteractor;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

import javax.inject.Inject;

public class SortInteractorImpl {
    private SortInteractor sortInteractor;

    @Inject
    public SortInteractorImpl(){}

    public SortInteractorImpl(SortInteractor sortInteractor){
        this.sortInteractor = sortInteractor;
    }


    public void createSong(){
        ArrayList<Song> songArrayList= new ArrayList<>();
        sortInteractor.onLoadSongSuccess(songArrayList);
    }
}
