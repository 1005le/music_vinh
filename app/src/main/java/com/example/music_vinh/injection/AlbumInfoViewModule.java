package com.example.music_vinh.injection;

import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.presenter.impl.AlbumPresenterImpl;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.AlbumView;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumInfoViewModule {

    private AlbumInfoView albumInfoView;
    public AlbumInfoViewModule(AlbumInfoView view) {
        this.albumInfoView = view;
    }

    @Provides
    public AlbumInfoView provideView() {
        return albumInfoView;
    }

    @Provides
    public AlbumInfoPresenterImpl providePresenter(AlbumInfoView albumInfoView, AlbumInfoInteractorImpl albumInfoInteractorImpl) {
        return new AlbumInfoPresenterImpl(albumInfoInteractorImpl, albumInfoView);
    }

}
