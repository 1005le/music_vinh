package com.example.music_vinh.presenter;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;

public class AlbumPresenter implements AlbumInteractor {

    private AlbumInteractorImpl albumInteractorImpl;
    private AlbumView albumView;


    public AlbumPresenter(AlbumView albumView) {
        this.albumView = albumView;
        albumInteractorImpl = new AlbumInteractorImpl(this);
    }
    @Override
    public void onLoadAlbumSuccess(ArrayList<Album> albums) {
        albumView.showAlbum(albums);
    }
}
