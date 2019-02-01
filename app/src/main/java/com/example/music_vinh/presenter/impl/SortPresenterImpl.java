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

public class SortPresenterImpl implements SortPresenter, SortInteractor {

    private SortInteractorImpl sortInteractorImpl;
    private SortView sortView;

    public SortPresenterImpl(SortView sortView) {
        this.sortView = sortView;
        sortInteractorImpl = new SortInteractorImpl(this);
    }

    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {
       sortView.showSong(songs);
    }

    @Override
    public void onLoadSongFailue(String message) {

    }

    @Override
    public void loadData() {
      sortInteractorImpl.createSong();
    }
}
