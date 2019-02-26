package com.example.music_vinh.injection;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.example.music_vinh.adapter.AlbumAdapter;
import com.example.music_vinh.adapter.SongInAlbumAdapter;
import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.AlbumInfoPresenter;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.presenter.impl.AlbumPresenterImpl;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.AlbumView;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumInfoViewModule {

//    private AlbumInfoView albumInfoView;
//    public AlbumInfoViewModule(AlbumInfoView view) {
//        this.albumInfoView = view;
//    }
//
//    @Provides
//    public AlbumInfoView provideView() {
//        return albumInfoView;
//    }

    //    @Provides
//    public AlbumInfoPresenter providePresenter(AlbumInfoView albumInfoView, AlbumInfoInteractorImpl albumInfoInteractorImpl) {
//        return new AlbumInfoPresenterImpl(albumInfoInteractorImpl, albumInfoView);
//    }

    private Context mContext;
    public AlbumInfoViewModule(Context context) {
        mContext = context;
    }
    @Provides
    Context provideContext(){
        return mContext;
    }

    @Provides
    AlbumInfoPresenter providePresenter(AlbumInfoPresenterImpl albumInfoPresenter){
        return albumInfoPresenter;
    }

    @Provides
    AlbumInfoInteractor provideAlbumInfoInteractor( AlbumInfoInteractor albumInfoInteractor){
        return albumInfoInteractor;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(){
        return new LinearLayoutManager(mContext);
    }

    @Provides
    SongInAlbumAdapter provideSongInAlbumAdapter() {
        return new SongInAlbumAdapter(mContext, new ArrayList<Song>());
    }
}
