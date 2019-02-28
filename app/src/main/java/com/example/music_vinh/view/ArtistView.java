package com.example.music_vinh.view;

import com.example.music_vinh.model.Artist;

import java.util.ArrayList;
import java.util.List;

public interface ArtistView {
    void showArtist(List<Artist> artists);
    void intentArtistForDetail(List<Artist>artists ,int position);

}
