package com.example.music_vinh.interactor;

import com.example.music_vinh.model.Album;

import java.util.ArrayList;

public interface AlbumInteractor {
    void onLoadAlbumSuccess(ArrayList<Album> albums);
}
