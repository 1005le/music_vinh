package com.example.music_vinh.presenter;

import android.content.Context;

import com.example.music_vinh.model.Song;

import java.util.List;

public interface MainPresenter {
    void getMusicSongArr(Context context);
    void onIntent(int position);
}
