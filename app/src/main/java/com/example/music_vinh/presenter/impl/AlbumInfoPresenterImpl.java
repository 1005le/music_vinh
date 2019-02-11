package com.example.music_vinh.presenter.impl;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.AlbumInfoPresenter;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;

public class AlbumInfoPresenterImpl implements AlbumInfoInteractor, AlbumInfoPresenter {

    private AlbumInfoInteractorImpl albumInfoInteractorImpl;
    private AlbumInfoView albumInfoView;

    public AlbumInfoPresenterImpl(AlbumInfoInteractorImpl albumInfoInteractorImpl, AlbumInfoView albumInfoView) {
        this.albumInfoInteractorImpl = albumInfoInteractorImpl;
        this.albumInfoView = albumInfoView;
    }

    public AlbumInfoPresenterImpl(AlbumInfoView albumInfoView) {
        this.albumInfoView = albumInfoView;
        albumInfoInteractorImpl = new AlbumInfoInteractorImpl(this);
    }

    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {

        albumInfoView.showSong(songs);
    }
    @Override
    public void loadData()
    {
       albumInfoInteractorImpl.getSongCategories(this);
    }
}
