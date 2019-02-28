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

    private AlbumInfoInteractorImpl albumInfoInteractorImpl;
    private AlbumInfoView albumInfoView;

    public AlbumInfoPresenterImpl(AlbumInfoView albumInfoView) {
        this.albumInfoView = albumInfoView;
    }

    @Inject
    public AlbumInfoPresenterImpl() {
    }

    @Override
    public void getSongFromAlbum(Context context, Long idAlbum) {

        List<Song> songArrayListAlbum = new ArrayList<>();
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

}
