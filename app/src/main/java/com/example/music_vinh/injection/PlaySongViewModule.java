package com.example.music_vinh.injection;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.interactor.impl.PlaySongInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.presenter.PlaySongPresenter;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.view.MainView;
import com.example.music_vinh.view.PlaySongView;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaySongViewModule {
    private Context mContext;
    public PlaySongViewModule(Context context) {
        mContext = context;
    }
    @Provides
    Context provideContext(){
        return mContext;
    }

    @Provides
    PlaySongPresenter providePlaySongPresenter(PlaySongPresenterImpl playSongPresenter){
        return playSongPresenter;
    }
    @Provides
    PlaySongInteractor providePlaySongInteractor(PlaySongInteractor interactor) {
        return interactor;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(Context mContext) {
        return new LinearLayoutManager(mContext);
    }

    @Provides
    SongAdapter provideSongAdapter() {
        return new SongAdapter(mContext, new ArrayList<Song>());
    }

}
