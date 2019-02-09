package com.example.music_vinh.injection;

import com.example.music_vinh.interactor.impl.PlaySongInteractorImpl;
import com.example.music_vinh.interactor.impl.SortInteractorImpl;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.presenter.impl.SortPresenterImpl;
import com.example.music_vinh.view.PlaySongView;
import com.example.music_vinh.view.SortView;

import dagger.Module;
import dagger.Provides;

@Module
public class SortViewModule {

    private SortView sortView;
    public SortViewModule(SortView view) {
        this.sortView = view;
    }

    @Provides
    public SortView provideView() {
        return sortView;
    }

    @Provides
    public SortPresenterImpl providePresenter(SortView sortView, SortInteractorImpl sortInteractorImpl) {
        return new SortPresenterImpl(sortInteractorImpl, sortView);
    }
}
