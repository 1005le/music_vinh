package com.example.music_vinh.injection;

import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.interactor.impl.PlaySongInteractorImpl;
import com.example.music_vinh.presenter.PlaySongPresenter;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.view.MainView;
import com.example.music_vinh.view.PlaySongView;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaySongViewModule {

    private PlaySongView playSongView;
    public PlaySongViewModule(PlaySongView view) {
        this.playSongView = view;
    }

    @Provides
    public PlaySongView provideView() {
        return playSongView;
    }

    @Provides
    public PlaySongPresenter providePresenter(PlaySongView playSongView, PlaySongInteractorImpl playSongInteractorImpl) {
        return new PlaySongPresenterImpl(playSongInteractorImpl, playSongView);
    }
}
