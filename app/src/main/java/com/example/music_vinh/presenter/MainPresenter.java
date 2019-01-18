package com.example.music_vinh.presenter;

import com.example.music_vinh.interactor.MainInteractor;
import com.example.music_vinh.interactor.impl.MainInteractorImpl;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.MainView;

import java.util.List;

/**
  * - Presenter: xử lý logic từ dữ liệu nhận được.
  * - Nhận dữ liệu từ lớp Model
  * - Đẩy dữ liệu lên lớp V.
 */
public class MainPresenter implements MainInteractor {
    private MainInteractorImpl mainInteractorImpl;
    private MainView mainView;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
        mainInteractorImpl = new MainInteractorImpl(this);
    }

    public void loadData(){
        mainInteractorImpl.createData();
    }
    @Override
    public void onLoadSongSuccess(List<Song> songs) {
        mainView.showSong(songs);
    }

    @Override
    public void onLoadSongFailue(String message) {

    }
}
