package com.example.music_vinh.presenter.impl;

import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainPresenterImpl implements MainInteractor, MainPresenter {
    private MainInteractorImpl mainInteractorImpl;
    private MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        mainInteractorImpl = new MainInteractorImpl(this);
    }

    @Inject
    public MainPresenterImpl() {
    }
    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {
        mainView.showSong(songs);
    }

    @Override
    public void loadData() {
        mainInteractorImpl.getSongCategories(this);
    }

}
