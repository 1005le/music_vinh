package com.example.music_vinh.interactor;

import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public interface PlaySongInteractor {
    void onLoadSongSuccess(ArrayList<Song> songs);
}
