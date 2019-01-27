package com.example.music_vinh.presenter.impl;

import com.example.music_vinh.interactor.ArtistInteractor;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.ArtistInteractorImpl;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.ArtistView;

import java.util.ArrayList;

public class ArtistPresenterImpl implements ArtistInteractor, ArtistPresenter {

    private ArtistInteractorImpl artistInteractorImpl;
    private ArtistView artistView;

    public ArtistPresenterImpl(ArtistView artistView) {
        this.artistView = artistView;
        artistInteractorImpl = new ArtistInteractorImpl(this);
    }
    @Override
    public void onLoadArtistSuccess(ArrayList<Artist> artists) {
        artistView.showArtist(artists);
    }

    @Override
    public void loadArtist() {
       artistInteractorImpl.createArtist();
    }
}
