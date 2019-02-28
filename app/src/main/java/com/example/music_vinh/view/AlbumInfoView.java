package com.example.music_vinh.view;

import com.example.music_vinh.model.Song;

import java.util.ArrayList;
import java.util.List;

public interface AlbumInfoView {

    void showSong(List<Song> songs);
    void setNameAlbum(String name);
    void setArtAlbum(String artPath);
    void setAmountSong(int amount);
    void intentSongForPlay(List<Song> songs, int position);
}
