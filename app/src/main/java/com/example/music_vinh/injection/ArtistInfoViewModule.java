package com.example.music_vinh.injection;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.music_vinh.adapter.ArtistAdapter;
import com.example.music_vinh.adapter.SongInAlbumAdapter;
import com.example.music_vinh.adapter.SongInArtistAdapter;
import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.ArtistInfoInteractor;
import com.example.music_vinh.interactor.ArtistInteractor;
import com.example.music_vinh.interactor.impl.ArtistInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.ArtistInteractorImpl;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.AlbumInfoPresenter;
import com.example.music_vinh.presenter.ArtistInfoPresenter;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.presenter.impl.ArtistInfoPresenterImpl;
import com.example.music_vinh.presenter.impl.ArtistPresenterImpl;
import com.example.music_vinh.view.ArtistInfoView;
import com.example.music_vinh.view.ArtistView;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtistInfoViewModule {

    private Context mContext;
    public ArtistInfoViewModule(Context context) {
        mContext = context;
    }
    @Provides
    Context provideContext(){
        return mContext;
    }

    @Provides
    ArtistInfoPresenter provideArtistInfoPresenter(ArtistInfoPresenterImpl artistInfoPresenter){
        return artistInfoPresenter;
    }
    @Provides
    ArtistInfoInteractor provideArtistInfoInteractor(ArtistInfoInteractor interactor) {
        return interactor;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(Context mContext){
        return new LinearLayoutManager(mContext);
    }

    @Provides
    SongInArtistAdapter provideSongInArtistAdapter() {
        return new SongInArtistAdapter(mContext, new ArrayList<Song>());
    }
}
