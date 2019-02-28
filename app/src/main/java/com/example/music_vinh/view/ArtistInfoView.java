package com.example.music_vinh.view;

import com.example.music_vinh.model.Song;

import java.util.ArrayList;
import java.util.List;

public interface ArtistInfoView {
    void showSong(List<Song> songs);
    void setArtistInfo(String name);
    void intentSongForPlay(List<Song> songs, int position);
}
