package com.example.music_vinh.presenter.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.music_vinh.interactor.ArtistInfoInteractor;
import com.example.music_vinh.interactor.impl.AlbumInfoInteractorImpl;
import com.example.music_vinh.interactor.impl.ArtistInfoInteractorImpl;
import com.example.music_vinh.model.Artist;
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
    List<Artist> artistList;
    List<Song> songArrayList;
    public ArtistInfoPresenterImpl(ArtistInfoView artistInfoView) {
        this.artistInfoView = artistInfoView;
        songArrayList = new ArrayList<>();
        artistList = new ArrayList<>();
    }

    @Inject
    public ArtistInfoPresenterImpl() {
        artistList = new ArrayList<>();
    }

    @Override
    public void getSongFromArtist(Context context, Long idArtist) {
         songArrayList = new ArrayList<>();
        Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor mediaCursor = context.getContentResolver().query(mediaUri, null, null, null, null);
        // if the cursor is null.
        if (mediaCursor != null && mediaCursor.moveToFirst()) {
            //get Columns
            int titleColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int artistId = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
            int albumName = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int artistSong = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int durationColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            // Store the title, id and artist name in Song Array list.
            do {
                long thisId = mediaCursor.getLong(idColumn);
                long thisArtistId = mediaCursor.getLong(artistId);
                String thisTitle = mediaCursor.getString(titleColumn);
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisAlbumName = mediaCursor.getString(albumName);
                String thisPath = mediaCursor.getString(artistSong);
                String thisDuration = mediaCursor.getString(durationColumn);

                // if(artist.getId() == thisArtistId)
                if (idArtist == thisArtistId) {
                    songArrayList.add(new Song(thisId, thisTitle, thisArtist, thisAlbumName, thisPath, Long.parseLong(thisDuration)));
                }
            }
            while (mediaCursor.moveToNext());
            // For best practices, close the cursor after use.
            mediaCursor.close();
            artistInfoView.showSong(songArrayList);
        }
    }

    @Override
    public void getArtistInfoDetail(Context context, Long idArtist) {

        Uri songUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor songCursor = context.getContentResolver().query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

            int id_Artist = songCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int amountAlbum = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int amountSong = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            do {
                long currentId = songCursor.getLong(id_Artist);
                String currentArtist = songCursor.getString(songArtist);
                String currentAmountAlbum = songCursor.getString(amountAlbum);
                String currentAmountSong = songCursor.getString(amountSong);

                if (idArtist == currentId) {
                    artistInfoView.setArtistInfo(currentArtist);
                    getSongFromArtist(context,currentId);
                }

            } while (songCursor.moveToNext());

        }
    }

    @Override
    public void onCallIntent(int position) {
        artistInfoView.intentSongForPlay(songArrayList,position);
    }
}
