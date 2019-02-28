package com.example.music_vinh.presenter.impl;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.presenter.AlbumPresenter;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AlbumPresenterImpl implements AlbumInteractor, AlbumPresenter {

    private AlbumView albumView;
    List<Album> albumList;
    @Inject
    public AlbumPresenterImpl() {

    }
    public AlbumPresenterImpl(AlbumView albumView) {
        this.albumView = albumView;
    }

    @Override
    public void onCallDataIntent(int position) {
        albumView.intentAlbumForDetail(albumList,position);
    }
    @Override
    public void getAlbum(Context context) {
         albumList = new ArrayList<>();

        Uri songUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor songCursor =  context.getContentResolver().query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int idAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int imgAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int amountSong = songCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
            do {
                String currentId = songCursor.getString(idAlbum);
                String currentAlbum = songCursor.getString(songAlbum);
                String currentArtist = songCursor.getString(songArtist);
                String currentImages = songCursor.getString(imgAlbum);
                String currentamountSong = songCursor.getString(amountSong);

                albumList.add(new Album(Long.parseLong(currentId),currentAlbum,currentArtist,
                        currentImages,Integer.parseInt(currentamountSong)));

            } while (songCursor.moveToNext());
            albumView.showAlbum(albumList);
        }

    }
}
