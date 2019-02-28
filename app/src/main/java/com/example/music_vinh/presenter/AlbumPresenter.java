package com.example.music_vinh.presenter;

import android.content.Context;

public interface AlbumPresenter {
    void getAlbum(Context context);
    void onCallDataIntent(int position);
}
