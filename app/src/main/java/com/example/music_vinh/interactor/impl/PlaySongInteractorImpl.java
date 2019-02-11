package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.PlayActivity;
import com.example.music_vinh.view.impl.SongFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class PlaySongInteractorImpl implements PlaySongInteractor{

    private PlaySongInteractor playSongInteractor;

    @Inject
    public PlaySongInteractorImpl(){

    }
    public PlaySongInteractorImpl(PlaySongInteractor playSongInteractor){
        this.playSongInteractor = playSongInteractor;
    }
    private  ArrayList<Song> createArrayList() {
        ArrayList<Song> songs = PlayActivity.arrSong;
        return songs;
    }

    public void getSongCategories(final PlaySongInteractor playSongInteractor) {
        playSongInteractor.onLoadSongSuccess(createArrayList());
    }

    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {

    }
}
