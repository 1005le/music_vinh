package com.example.music_vinh.presenter.impl;

import com.example.music_vinh.interactor.ArtistInfoInteractor;
import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.ArtistInfoInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.ArtistInfoPresenter;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.ArtistInfoView;

import java.util.ArrayList;

import javax.inject.Inject;

public class ArtistInfoPresenterImpl implements ArtistInfoInteractor, ArtistInfoPresenter {

    private ArtistInfoInteractorImpl artistInfoInteractorImpl;
    private ArtistInfoView artistInfoView;

    public ArtistInfoPresenterImpl(ArtistInfoInteractorImpl artistInfoInteractorImpl, ArtistInfoView artistInfoView) {
        this.artistInfoInteractorImpl = artistInfoInteractorImpl;
        this.artistInfoView = artistInfoView;
    }

    public ArtistInfoPresenterImpl(ArtistInfoView artistInfoView) {
        this.artistInfoView = artistInfoView;
        artistInfoInteractorImpl = new ArtistInfoInteractorImpl(this);
    }
    @Inject
    public ArtistInfoPresenterImpl() {

    }
    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {
        artistInfoView.showSong(songs);
    }

    @Override
    public void loadData() {
        artistInfoInteractorImpl.getSongCategories(this);
    }
}
