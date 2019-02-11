package com.example.music_vinh.interactor.impl;

import android.content.Context;
import android.os.Handler;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.ArtistInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.AlbumFragment;
import com.example.music_vinh.view.impl.ArtistFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class ArtistInteractorImpl implements ArtistInteractor{

    private ArtistInteractor artistInteractor;
    Context context;

    @Inject
    public ArtistInteractorImpl() {
    }

    public ArtistInteractorImpl(ArtistInteractor artistInteractor){
        this.artistInteractor = artistInteractor;
    }
    public void createArtist(){
        ArrayList<Artist> artists = new ArrayList<>();
        artistInteractor.onLoadArtistSuccess(artists);
    }

    private  ArrayList<Artist> createArrayList() {
        ArrayList<Artist> artists = ArtistFragment.artistList;
        return artists;
    }

    public void getArtistCategories(final ArtistInteractor artistInteractor) {
                artistInteractor.onLoadArtistSuccess(createArrayList());
    }

    @Override
    public void onLoadArtistSuccess(ArrayList<Artist> songs) {

    }
}
