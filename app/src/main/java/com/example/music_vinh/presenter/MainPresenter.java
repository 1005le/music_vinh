package com.example.music_vinh.presenter;

import android.content.Context;

import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.MainView;

import java.util.List;

public interface MainPresenter {
    void getMusicSongArr(Context context);
    void onIntent(int position);
    void onAttach(MainView mainView);
}
