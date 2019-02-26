package com.example.music_vinh.presenter.impl;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.presenter.AlbumPresenter;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;

import javax.inject.Inject;

public class AlbumPresenterImpl implements AlbumInteractor, AlbumPresenter {

    private AlbumInteractorImpl albumInteractorImpl;
    private AlbumView albumView;

    public AlbumPresenterImpl(AlbumInteractorImpl albumInteractorImpl, AlbumView albumView) {
        this.albumInteractorImpl = albumInteractorImpl;
        this.albumView = albumView;
    }
    @Inject
    public AlbumPresenterImpl() {

    }

    public AlbumPresenterImpl(AlbumView albumView) {
        this.albumView = albumView;
        albumInteractorImpl = new AlbumInteractorImpl(this);
    }

    @Override
    public void onLoadAlbumSuccess(ArrayList<Album> albums) {

        albumView.showAlbum(albums);
    }
    @Override
    public void loadAlbums() {

        albumInteractorImpl.getAlbumCategories(this);
    }

}
