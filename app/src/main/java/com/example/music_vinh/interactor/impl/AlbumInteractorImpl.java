package com.example.music_vinh.interactor.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import com.example.music_vinh.interactor.AlbumInteractor;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.AlbumFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AlbumInteractorImpl implements AlbumInteractor{
    private AlbumInteractor albumInteractor;
    Context context;
    @Inject
    public AlbumInteractorImpl() {

    }
    public AlbumInteractorImpl(AlbumInteractor albumInteractor){
        this.albumInteractor = albumInteractor;
    }
    private List<Album> createArrayList(Context context) {
       List<Album> albums = getAlbum(context);
        return albums;
    }
    public void getAlbumCategories(final AlbumInteractor albumInteractor, Context context) {
                albumInteractor.onLoadAlbumSuccess(createArrayList(context));
    }
    @Override
    public void onLoadAlbumSuccess(List<Album> albums) {
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
