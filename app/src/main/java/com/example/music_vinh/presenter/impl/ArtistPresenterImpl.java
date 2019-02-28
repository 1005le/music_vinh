package com.example.music_vinh.presenter.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.music_vinh.interactor.ArtistInteractor;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.ArtistInteractorImpl;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.ArtistView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ArtistPresenterImpl implements ArtistInteractor, ArtistPresenter {

    private ArtistView artistView;
    List<Artist> artistList;
    @Inject
    public ArtistPresenterImpl() {

    }
    public ArtistPresenterImpl(ArtistView artistView) {
        this.artistView = artistView;
    }


    @Override
    public void onCallIntent(int positon) {
        artistView.intentArtistForDetail(artistList,positon);
    }
    @Override
    public void getMusicArtist(Context context) {
        artistList = new ArrayList<>();
        Uri songUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor songCursor =context.getContentResolver().query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

            int idArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int amountAlbum = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int amountSong = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            // int imgArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ALBUM_ART);
            do {
                String currentId = songCursor.getString(idArtist);
                String currentArtist = songCursor.getString(songArtist);
                String currentAmountAlbum = songCursor.getString(amountAlbum);
                String currentAmountSong = songCursor.getString(amountSong);

                //  String currentImgArtist = songCursor.getString(imgArtist);
                artistList.add(new Artist(Long.parseLong(currentId),currentArtist, Integer.parseInt(currentAmountAlbum),
                        Integer.parseInt(currentAmountSong),""));

            } while (songCursor.moveToNext());
            artistView.showArtist(artistList);
        }
    }
}
