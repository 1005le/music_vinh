package com.example.music_vinh.presenter;

import android.content.Context;

public interface AlbumInfoPresenter {
    void getSongFromAlbum(Context context, Long idAlbum);
    void getAlbumForDetail(Context context,Long idAlbum);
    void onCallIntent(int position);
}
