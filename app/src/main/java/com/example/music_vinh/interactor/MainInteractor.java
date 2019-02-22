package com.example.music_vinh.interactor;

import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;
import java.util.List;

public interface MainInteractor {

    void onLoadSongSuccess(ArrayList<Song> songs);
}
