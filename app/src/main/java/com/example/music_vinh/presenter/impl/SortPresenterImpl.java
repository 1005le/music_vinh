package com.example.music_vinh.presenter.impl;

import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.interactor.SortInteractor;
import com.example.music_vinh.interactor.impl.PlaySongInteractorImpl;
import com.example.music_vinh.interactor.impl.SortInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.PlaySongPresenter;
import com.example.music_vinh.presenter.SortPresenter;
import com.example.music_vinh.view.PlaySongView;
import com.example.music_vinh.view.SortView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SortPresenterImpl implements SortPresenter, SortInteractor {

    private SortView sortView;

    @Inject
    public SortPresenterImpl() {

    }

    public SortPresenterImpl(SortView sortView) {
        this.sortView = sortView;
    }

    @Override
    public void loadSongFromPlay(List<Song> songs) {
        sortView.showSong(songs);
    }
}
