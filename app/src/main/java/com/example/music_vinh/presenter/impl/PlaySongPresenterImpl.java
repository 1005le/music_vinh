package com.example.music_vinh.presenter.impl;

import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.PlaySongInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.PlaySongPresenter;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.PlaySongView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PlaySongPresenterImpl implements PlaySongPresenter, PlaySongInteractor {

    private PlaySongView playSongView;

    @Inject
    public PlaySongPresenterImpl() {

    }
//    public PlaySongPresenterImpl(PlaySongInteractorImpl playSongInteractorImpl, PlaySongView playSongView) {
//        this.playSongInteractorImpl = playSongInteractorImpl;
//        this.playSongView = playSongView;
//    }

    public PlaySongPresenterImpl(PlaySongView playSongView) {
        this.playSongView = playSongView;
    }

    @Override
    public void onLoadSongPlay(List<Song> songs) {
            playSongView.showSong(songs);
    }
}
