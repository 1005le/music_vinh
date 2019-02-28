package com.example.music_vinh.view;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.music_vinh.model.Song;

import java.util.ArrayList;
import java.util.List;

public interface MainView {
    void showSong( List<Song> songs);
    void intentSongForPlay(List<Song> song, int position);
}
