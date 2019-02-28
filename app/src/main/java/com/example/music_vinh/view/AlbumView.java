package com.example.music_vinh.view;

import com.example.music_vinh.model.Album;

import java.util.ArrayList;
import java.util.List;

public interface AlbumView {
    void showAlbum(List<Album> albums);
    void intentAlbumForDetail(List<Album> albums, int position);
}
