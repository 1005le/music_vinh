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

    private AlbumInteractorImpl albumInteractorImpl;
    private AlbumView albumView;

//    public AlbumPresenterImpl(AlbumInteractorImpl albumInteractorImpl, AlbumView albumView) {
//        //this.albumInteractorImpl = albumInteractorImpl;
//        this.albumView = albumView;
//    }
    @Inject
    public AlbumPresenterImpl() {

    }

    public AlbumPresenterImpl(AlbumView albumView) {
        this.albumView = albumView;
      //  albumInteractorImpl = new AlbumInteractorImpl(this);
    }

//    @Override
//    public void onLoadAlbumSuccess(List<Album> albums) {
//        albumView.showAlbum(albums);
//    }
//
//    @Override
//    public void loadAlbums(Context context) {
//        albumInteractorImpl.getAlbumCategories(this,context);
//    }
    @Override
    public void loadAlbums(Context context) {
      albumView.showAlbum(getAlbum(context));
    }

    public List<Album> getAlbum(Context context) {
        List<Album> albumList = new ArrayList<>();

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
        }
        return albumList;
    }
}
