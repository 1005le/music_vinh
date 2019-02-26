package com.example.music_vinh.injection;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.adapter.SortSongAdapter;
import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.interactor.SortInteractor;
import com.example.music_vinh.interactor.impl.PlaySongInteractorImpl;
import com.example.music_vinh.interactor.impl.SortInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.PlaySongPresenter;
import com.example.music_vinh.presenter.SortPresenter;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.presenter.impl.SortPresenterImpl;
import com.example.music_vinh.view.PlaySongView;
import com.example.music_vinh.view.SortView;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class SortViewModule {

//    private SortView sortView;
//    public SortViewModule(SortView view) {
//        this.sortView = view;
//    }
//
//    @Provides
//    public SortView provideView() {
//        return sortView;
//    }
//
//    @Provides
//    public SortPresenter providePresenter(SortView sortView, SortInteractorImpl sortInteractorImpl) {
//        return new SortPresenterImpl(sortInteractorImpl, sortView);
//    }

    private Context mContext;
    public SortViewModule(Context context) {
        mContext = context;
    }
    @Provides
    Context provideContext(){
        return mContext;
    }

    @Provides
    SortPresenter provideSortPresenter(SortPresenterImpl sortPresenter){
        return sortPresenter;
    }
    @Provides
    SortInteractor provideSortInteractor(SortInteractor interactor) {
        return interactor;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(Context mContext) {
        return new LinearLayoutManager(mContext);
    }

    @Provides
    SortSongAdapter provideSortSongAdapter() {
        return new SortSongAdapter(mContext, new ArrayList<Song>());
    }

}
