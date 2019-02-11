package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.ArtistInfoInteractor;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.AlbumInfoActivity;
import com.example.music_vinh.view.impl.ArtistInfoActivity;

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

    private  ArrayList<Song> createArrayList() {
        ArrayList<Song> songs = ArtistInfoActivity.songArrayList;
        return songs;
    }

    public void getSongCategories(final ArtistInfoInteractor artistInfoInteractor) {
        artistInfoInteractor.onLoadSongSuccess(createArrayList());
    }
    public void createSong(){
        ArrayList<Song> songArrayList= new ArrayList<>();
        artistInfoInteractor.onLoadSongSuccess(songArrayList);
    }
}
