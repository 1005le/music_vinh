package com.example.music_vinh.injection;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.example.music_vinh.adapter.AlbumAdapter;
import com.example.music_vinh.adapter.MainViewAdapter;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.presenter.impl.ArtistPresenterImpl;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class MainViewModule {

    private Context mContext;
    public MainViewModule(Context context) {
        mContext = context;
    }
    @Provides
    Context provideContext(){
        return mContext;
    }

//    private MainView mainView;
//    public MainViewModule(MainView view) {
//        this.mainView = view;
//    }
//
//    @Provides
//    public MainView provideView() {
//        return mainView;
//    }
//
//    @Provides
//    public MainPresenter providePresenter(MainView mainView, MainInteractorImpl mainInteractorImpl) {
//        return new MainPresenterImpl(mainInteractorImpl, mainView);
//    }

       @Provides
       MainPresenter provideMainPresenter(MainPresenterImpl mainPresenter){
       return mainPresenter;
      }
    @Provides
    MainInteractor provideMainInteractor(MainInteractor interactor) {
        return interactor;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(Context mContext) {
        return new LinearLayoutManager(mContext);
    }

    @Provides
    GridLayoutManager provideGridLayoutManager(Context mContext) {
        return new GridLayoutManager(mContext, 2);
    }
    @Provides
    SongAdapter provideSongAdapter() {
        return new SongAdapter(mContext, new ArrayList<Song>());
    }
}
