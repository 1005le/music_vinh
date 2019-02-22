package com.example.music_vinh.view;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.music_vinh.model.Song;

import java.util.ArrayList;

public interface MainView {
    void showSong(ArrayList<Song> songs);
}
