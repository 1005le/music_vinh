package com.example.music_vinh.injection;

import com.example.music_vinh.interactor.impl.ArtistInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.ArtistInteractorImpl;
import com.example.music_vinh.presenter.ArtistInfoPresenter;
import com.example.music_vinh.presenter.impl.ArtistInfoPresenterImpl;
import com.example.music_vinh.presenter.impl.ArtistPresenterImpl;
import com.example.music_vinh.view.ArtistInfoView;
import com.example.music_vinh.view.ArtistView;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtistInfoViewModule {

    private ArtistInfoView artistInfoView;
    public ArtistInfoViewModule(ArtistInfoView view) {
        this.artistInfoView = view;
    }

    @Provides
    public ArtistInfoView provideView() {
        return artistInfoView;
    }

    @Provides
    public ArtistInfoPresenter providePresenter(ArtistInfoView artistInfoView, ArtistInfoInteractorImpl artistInfoInteractorImpl) {
        return new ArtistInfoPresenterImpl(artistInfoInteractorImpl, artistInfoView);
    }

}
