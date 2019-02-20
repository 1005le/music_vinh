package com.example.music_vinh.injection;

import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.presenter.AlbumPresenter;
import com.example.music_vinh.presenter.impl.AlbumPresenterImpl;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.MainView;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumViewModule {

    private AlbumView albumView;
    public AlbumViewModule(AlbumView view) {
        this.albumView = view;
    }

    @Provides
    public AlbumView provideView() {
        return albumView;
    }

    @Provides
    public AlbumPresenter providePresenter(AlbumView albumView, AlbumInteractorImpl albumInteractorImpl) {
        return new AlbumPresenterImpl(albumInteractorImpl, albumView);

    }
//    @Provides
//    public AlbumPresenter providePresenter1(AlbumPresenterImpl albumInteractorImpl) {
//        return albumInteractorImpl;
//    }

}
