package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.PlaySongInteractor;
import com.example.music_vinh.interactor.SortInteractor;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.SongFragment;
import com.example.music_vinh.view.impl.SortActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class SortInteractorImpl implements SortInteractor {

    @Inject
    public SortInteractorImpl(SortInteractor sortInteractor){
    }
    private  ArrayList<Song> createArrayList() {
        ArrayList<Song> songs = SortActivity.songArrayList;
        return songs;
    }
    public void getSongCategories(final SortInteractor sortInteractor) {
        sortInteractor.onLoadSongSuccess(createArrayList());
    }
    @Override
    public void onLoadSongSuccess(ArrayList<Song> songs) {

    }

}
