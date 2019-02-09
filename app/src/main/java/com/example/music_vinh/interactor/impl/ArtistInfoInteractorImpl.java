package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.ArtistInfoInteractor;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

import javax.inject.Inject;

public class ArtistInfoInteractorImpl {
    private ArtistInfoInteractor artistInfoInteractor;

    @Inject
    public ArtistInfoInteractorImpl() {
    }

    public ArtistInfoInteractorImpl(ArtistInfoInteractor artistInfoInteractor){
        this.artistInfoInteractor = artistInfoInteractor;
    }

    public void createSong(){
        ArrayList<Song> songArrayList= new ArrayList<>();
        artistInfoInteractor.onLoadSongSuccess(songArrayList);
    }
}
