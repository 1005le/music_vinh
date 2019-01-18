package com.example.music_vinh.interactor.impl;

import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.model.Song;

import java.util.ArrayList;
import java.util.List;

/*
 *- Lớp M: xử lý dữ liệu -> Trả dữ liệu về P thông qua callback
 * */

public class MainInteractorImpl {

    private MainInteractor mainInteractor;

    public MainInteractorImpl(MainInteractor mainInteractor){
        this.mainInteractor = mainInteractor;
    }

    //Xu ly tao du lieu

    public void createData(){
        List<Song> songs = new ArrayList<>();
        for( int i=0;i< 5;i++){
            Song song = new Song("Vung La Me Bay"+i+"","Nhu Quynh");
            songs.add(song);
        }
        mainInteractor.onLoadSongSuccess(songs);
    }
}
