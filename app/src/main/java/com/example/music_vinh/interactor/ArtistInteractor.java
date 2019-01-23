package com.example.music_vinh.interactor;

import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public interface ArtistInteractor {
    void onLoadArtistSuccess(ArrayList<Artist> songs);
}
