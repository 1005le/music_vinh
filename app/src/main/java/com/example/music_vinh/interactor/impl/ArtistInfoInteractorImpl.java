package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.ArtistInfoInteractor;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public class ArtistInfoInteractorImpl {
    private ArtistInfoInteractor artistInfoInteractor;

    public ArtistInfoInteractorImpl(ArtistInfoInteractor artistInfoInteractor){
        this.artistInfoInteractor = artistInfoInteractor;
    }

    public void createSong(){
        ArrayList<Song> songArrayList= new ArrayList<>();
        artistInfoInteractor.onLoadSongSuccess(songArrayList);
    }
}
