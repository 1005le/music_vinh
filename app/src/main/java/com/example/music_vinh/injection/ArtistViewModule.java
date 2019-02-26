package com.example.music_vinh.injection;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.example.music_vinh.adapter.AlbumAdapter;
import com.example.music_vinh.adapter.ArtistAdapter;
import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.ArtistInteractor;
import com.example.music_vinh.interactor.impl.ArtistInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.presenter.AlbumPresenter;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.presenter.impl.AlbumPresenterImpl;
import com.example.music_vinh.presenter.impl.ArtistPresenterImpl;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.view.ArtistView;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtistViewModule {


    private Context mContext;
    public ArtistViewModule(Context context) {
        mContext = context;
    }
    @Provides
    Context provideContext(){
        return mContext;
    }

//    private ArtistView artistView;
//    public ArtistViewModule(ArtistView view) {
//        this.artistView = view;
//    }
//
//    @Provides
//    public ArtistView provideView() {
//        return artistView;
//    }
//
//    @Provides
//    public ArtistPresenter providePresenter(ArtistView artistView, ArtistInteractorImpl artistInteractorImpl) {
//        return new ArtistPresenterImpl(artistInteractorImpl, artistView);
//    }

    @Provides
    ArtistPresenter provideArtistPresenter(ArtistPresenterImpl artistPresenter){
        return artistPresenter;
    }
    @Provides
    ArtistInteractor provideArtistInteractor(ArtistInteractor interactor) {
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
    ArtistAdapter provideArtistAdapter() {
        return new ArtistAdapter(mContext, new ArrayList<Artist>());
    }
}
