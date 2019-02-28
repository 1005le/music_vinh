package com.example.music_vinh.presenter.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.music_vinh.interactor.ArtistInfoInteractor;
import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.ArtistInfoInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.ArtistInfoPresenter;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.ArtistInfoView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ArtistInfoPresenterImpl implements ArtistInfoInteractor, ArtistInfoPresenter {

    private ArtistInfoView artistInfoView;

    public ArtistInfoPresenterImpl(ArtistInfoView artistInfoView) {
        this.artistInfoView = artistInfoView;
    }
    @Inject
    public ArtistInfoPresenterImpl() {

    }
    @Override
    public void getSongFromArtist(Context context, Long idArtist) {
        List<Song>  songArrayList = new ArrayList<>();
            Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor mediaCursor = context.getContentResolver().query(mediaUri, null, null, null, null);
            // if the cursor is null.
            if(mediaCursor != null && mediaCursor.moveToFirst())
            {
                //get Columns
                int titleColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int idColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int artistId = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
                int albumName = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int artistSong = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int durationColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                // Store the title, id and artist name in Song Array list.
                do
                {
                    long thisId = mediaCursor.getLong(idColumn);
                    long thisArtistId = mediaCursor.getLong(artistId);
                    String thisTitle = mediaCursor.getString(titleColumn);
                    String thisArtist = mediaCursor.getString(artistColumn);
                    String thisAlbumName = mediaCursor.getString(albumName);
                    String thisPath = mediaCursor.getString(artistSong);
                    String thisDuration = mediaCursor.getString(durationColumn);

                    // if(artist.getId() == thisArtistId)
                    if(idArtist == thisArtistId)
                    {
                        songArrayList.add(new Song(thisId, thisTitle, thisArtist,thisAlbumName,thisPath,Long.parseLong(thisDuration)));
                       // collapsingToolbarLayoutArtist.setTitle(thisArtist);
                    }
                }
                while (mediaCursor.moveToNext());
                // For best practices, close the cursor after use.
                mediaCursor.close();
                artistInfoView.showSong(songArrayList);
            }
    }
}
