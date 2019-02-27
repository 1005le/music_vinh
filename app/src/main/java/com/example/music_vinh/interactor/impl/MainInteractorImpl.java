package com.example.music_vinh.interactor.impl;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.impl.SongFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/*
 *- Lớp M: xử lý dữ liệu -> Trả dữ liệu về P thông qua callback
 * */

public class MainInteractorImpl implements MainInteractor{
    Context context;
    @Inject
    public MainInteractorImpl(MainInteractor mainInteractor){

    }

    private  List<Song> createArrayList(Context context) {
       // ArrayList<Song> songs = SongFragment.songList;
        List<Song> songs = getMusicSongArr(context);
      // List<Song> songs = getAllAudioFromDevice(context);
       return songs;
    }
    public void getSongCategories(MainInteractor mainInteractor, Context context) {
                mainInteractor.onLoadSongSuccess(createArrayList(context));
    }

    @Override
    public void onLoadSongSuccess(List<Song> songs) {
    }

//    public List<Song> getAllAudioFromDevice( Context context) {
//
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        List<Song> songList = new ArrayList<>();
//        //String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//        String[] projection = {
//                MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.DURATION,
//        };
//
//        Cursor songCursor = context.getContentResolver().query(uri,
//                projection,
//                null,
//                null,
//                null);
//
//        if (songCursor != null) {
//            while (songCursor.moveToNext()) {
//
//                // check duration > 30s
//                if (songCursor.getInt(5) > 30000) {
//                    Song song = new Song();
//
//                    String id = songCursor.getString(0);
//                    String name = songCursor.getString(1);
//                    String currentArtist = songCursor.getString(2);
//                    String currentAlbum = songCursor.getString(3);
//                    String currentPath = songCursor.getString(4);
//
//                    song.setId(Long.parseLong(id));
//                    song.setName(name);
//                    song.setNameArtist(currentArtist);
//                    song.setNameAlbum(currentAlbum);
//                    song.setPath(currentPath);
//
//                   songList.add(song);
//                  //  songList.add(new Song(Long.parseLong(id),name, currentArtist,currentAlbum,currentPath));
//                }
//            }
//            songCursor.close();
//        }
//        return songList;
//    }

    public List<Song> getMusicSongArr(Context context) {
        List<Song> songList = new ArrayList<>();
        Uri songUri =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = context.getContentResolver().query(songUri, null,
                null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                String currentId = songCursor.getString(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentAlbum = songCursor.getString(songAlbum);
                String currentPath = songCursor.getString(songPath);
                String currentDuration = songCursor.getString(songDuration);

                songList.add(new Song(Long.parseLong(currentId),currentTitle, currentArtist,currentAlbum,currentPath, Long.parseLong(currentDuration)));

            } while (songCursor.moveToNext());
        }
        return songList;
    }

}
