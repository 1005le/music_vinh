package com.example.music_vinh.interactor.impl;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;
import java.util.List;

/*
 *- Lớp M: xử lý dữ liệu -> Trả dữ liệu về P thông qua callback
 * */

public class MainInteractorImpl {

    private MainInteractor mainInteractor;
     Context context;

    public MainInteractorImpl(MainInteractor mainInteractor){
        this.mainInteractor = mainInteractor;
    }

    //Xu ly tao du lieu, gia lap
//    public void createData(){
//      ArrayList<Song> songs = new ArrayList<>();
//        for( int i= 0;i <5;i++){
//            Song song = new Song("Vung La Me Bay "+i+"","Nhu Quynh");
//            songs.add(song);
//        }
//        mainInteractor.onLoadSongSuccess(songs);
//    }

}
