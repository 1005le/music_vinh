package com.example.music_vinh.interactor;

import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public interface ArtistInfoInteractor {

    void onLoadSongSuccess(ArrayList<Song> songs);
}
