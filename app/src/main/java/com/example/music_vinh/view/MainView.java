package com.example.music_vinh.view;

import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public interface MainView {
     void showSong(ArrayList<Song> songs);
     void showSongGrid(ArrayList<Song> songs);
}
