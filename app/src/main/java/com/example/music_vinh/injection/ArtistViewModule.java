package com.example.music_vinh.injection;

import com.example.music_vinh.interactor.impl.ArtistInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.presenter.impl.ArtistPresenterImpl;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.view.ArtistView;
import com.example.music_vinh.view.MainView;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtistViewModule {
    private ArtistView artistView;
    public ArtistViewModule(ArtistView view) {
        this.artistView = view;
    }

    @Provides
    public ArtistView provideView() {
        return artistView;
    }

    @Provides
    public ArtistPresenter providePresenter(ArtistView artistView, ArtistInteractorImpl artistInteractorImpl) {
        return new ArtistPresenterImpl(artistInteractorImpl, artistView);
    }
}
