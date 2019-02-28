package com.example.music_vinh.presenter;

import com.example.music_vinh.model.Song;

import java.util.List;

public interface PlaySongPresenter {
    void onLoadSongPlay(List<Song> songs);
}
