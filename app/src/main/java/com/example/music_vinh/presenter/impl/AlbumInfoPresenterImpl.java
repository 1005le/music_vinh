package com.example.music_vinh.presenter.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.AlbumInteractorImpl;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.AlbumInfoPresenter;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.AlbumView;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AlbumInfoPresenterImpl implements AlbumInfoInteractor, AlbumInfoPresenter {

    private AlbumInfoView albumInfoView;
    List<Song> songArrayListAlbum;
    public AlbumInfoPresenterImpl(AlbumInfoView albumInfoView) {
        this.albumInfoView = albumInfoView;
        songArrayListAlbum = new ArrayList<>();
    }

    @Inject
    public AlbumInfoPresenterImpl() {

    }

    @Override
    public void getSongFromAlbum(Context context, Long idAlbum) {

        songArrayListAlbum = new ArrayList<>();
        Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor mediaCursor = context.getContentResolver().query(mediaUri, null, null, null, null);

        if (mediaCursor != null && mediaCursor.moveToFirst()) {
            //get Columns
            int titleColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumId = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int albumName = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songPath = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int durationColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            // Store the title, id and artist name in Song Array list.
            do {
                long thisId = mediaCursor.getLong(idColumn);
                long thisalbumId = mediaCursor.getLong(albumId);
                String thisTitle = mediaCursor.getString(titleColumn);
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisAlbumName = mediaCursor.getString(albumName);
                String thisPath = mediaCursor.getString(songPath);
                String thisDuration = mediaCursor.getString(durationColumn);

                // if (album.getId() == thisalbumId)
                if( (idAlbum) == thisalbumId) {
                    songArrayListAlbum.add(new Song(thisId, thisTitle, thisArtist, thisAlbumName, thisPath, Long.parseLong(thisDuration)));
                }
            }
            while (mediaCursor.moveToNext());
            mediaCursor.close();
            albumInfoView.showSong(songArrayListAlbum);
        }
    }

    @Override
    public void getAlbumForDetail(Context context, Long idAlbum) {
        Uri songUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor songCursor =  context.getContentResolver().query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int id_Album = songCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int imgAlbum = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int amountSong = songCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
            do {
                long currentId = songCursor.getLong(id_Album);
                String currentAlbum = songCursor.getString(songAlbum);
                String currentArtist = songCursor.getString(songArtist);
                String currentImages = songCursor.getString(imgAlbum);
                String currentamountSong = songCursor.getString(amountSong);
                  if(idAlbum == currentId) {

                     albumInfoView.setNameAlbum(currentAlbum);
                     albumInfoView.setArtAlbum(currentImages);
                     albumInfoView.setAmountSong(Integer.parseInt(currentamountSong));
                     getSongFromAlbum(context,currentId);

                  }
            } while (songCursor.moveToNext());
        }

    }

    @Override
    public void onCallIntent(int position) {
        albumInfoView.intentSongForPlay(songArrayListAlbum,position);
    }
}
