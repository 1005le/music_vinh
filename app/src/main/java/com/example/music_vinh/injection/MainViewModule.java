package com.example.music_vinh.injection;

import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.view.MainView;

import dagger.Module;
import dagger.Provides;

@Module
public class MainViewModule {

    private MainView mainView;
    public MainViewModule(MainView view) {
        this.mainView = view;
    }

    @Provides
    public MainView provideView() {
        return mainView;
    }

    @Provides
    public MainPresenter providePresenter(MainView mainView, MainInteractorImpl mainInteractorImpl) {
        return new MainPresenterImpl(mainInteractorImpl, mainView);
    }
}
