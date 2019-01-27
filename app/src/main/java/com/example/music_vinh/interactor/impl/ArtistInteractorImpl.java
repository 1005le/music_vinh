package com.example.music_vinh.interactor.impl;

import android.content.Context;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.ArtistInteractor;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public class ArtistInteractorImpl {

    private ArtistInteractor artistInteractor;
    Context context;

    public ArtistInteractorImpl(ArtistInteractor artistInteractor){
        this.artistInteractor = artistInteractor;
    }
    public void createArtist(){
        ArrayList<Artist> artists = new ArrayList<>();
        artistInteractor.onLoadArtistSuccess(artists);
    }
}
