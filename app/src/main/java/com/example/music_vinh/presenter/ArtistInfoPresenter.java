package com.example.music_vinh.presenter;


import android.content.Context;

public interface ArtistInfoPresenter {
    void getSongFromArtist(Context context,Long idArtist);
    void getArtistInfoDetail(Context context, Long idArtist);
    void onCallIntent(int position);
}
