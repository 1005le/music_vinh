package com.example.music_vinh.presenter;

import com.example.music_vinh.interactor.ArtistInteractor;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.ArtistInteractorImpl;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.ArtistView;
import com.example.music_vinh.view.impl.ArtistFragment;

import java.util.ArrayList;

public class ArtistPresenter implements ArtistInteractor {

    private ArtistInteractorImpl artistInteractorImpl;
    private ArtistView artistView;


    public ArtistPresenter(ArtistView artistView) {
        this.artistView = artistView;
        artistInteractorImpl = new ArtistInteractorImpl(this);
    }

    @Override
    public void onLoadArtistSuccess(ArrayList<Artist> artists) {
        artistView.showArtist(artists);
    }
}
