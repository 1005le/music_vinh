package com.example.music_vinh.presenter.impl;

import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.PlaySongInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.PlaySongPresenter;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.PlaySongView;

import java.util.ArrayList;

import javax.inject.Inject;

public class PlaySongPresenterImpl implements PlaySongPresenter, PlaySongInteractor {

    private PlaySongInteractorImpl playSongInteractorImpl;
    private PlaySongView playSongView;

    @Inject
    public PlaySongPresenterImpl() {

    }
    public PlaySongPresenterImpl(PlaySongInteractorImpl playSongInteractorImpl, PlaySongView playSongView) {
        this.playSongInteractorImpl = playSongInteractorImpl;
        this.playSongView = playSongView;
    }

    public PlaySongPresenterImpl(PlaySongView playSongView) {
        this.playSongView = playSongView;
        playSongInteractorImpl = new PlaySongInteractorImpl(this);
    }

    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {
        playSongView.showSong(songs);
    }

    @Override
    public void loadData() {

        playSongInteractorImpl.getSongCategories(this);
    }
}
