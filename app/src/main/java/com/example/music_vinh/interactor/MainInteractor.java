package com.example.music_vinh.interactor;

import com.example.music_vinh.model.Song;

import java.util.List;

public interface MainInteractor {

    void onLoadSongSuccess(List<Song> songs);
    void onLoadSongFailue(String message);

}
