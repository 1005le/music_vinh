package com.example.music_vinh.interactor.impl;

import android.content.Context;
import android.os.Handler;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.ArtistInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.AlbumFragment;
import com.example.music_vinh.view.impl.ArtistFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class ArtistInteractorImpl implements ArtistInteractor{

    @Inject
    public ArtistInteractorImpl(ArtistInteractor artistInteractor){
    }
}
