package com.example.music_vinh.injection;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.example.music_vinh.adapter.AlbumAdapter;
import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.presenter.AlbumPresenter;
import com.example.music_vinh.presenter.impl.AlbumPresenterImpl;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumViewModule {

    private Context mContext;
    public AlbumViewModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext(){
        return mContext;
    }

    @Provides
    AlbumPresenter provideAlbumPresenter(AlbumPresenterImpl albumPresenter){
        return albumPresenter;
    }
    @Provides
    AlbumInteractor provideAlbumInteractor(AlbumInteractor interactor) {
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
    AlbumAdapter provideAlbumAdapter() {
        return new AlbumAdapter(mContext, new ArrayList<Album>());
    }

}
