package com.example.music_vinh.view.impl;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.adapter.SortSongAdapter;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.DaggerPlaySongViewComponent;
import com.example.music_vinh.injection.DaggerSortViewComponent;
import com.example.music_vinh.injection.PlaySongViewModule;
import com.example.music_vinh.injection.SortViewModule;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.presenter.impl.SortPresenterImpl;
import com.example.music_vinh.view.SortView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import static com.example.music_vinh.view.impl.PlayActivity.song;

public class SortActivity extends BaseActivity implements SortView {

   Toolbar toolbarSort;
    RecyclerView sortSongRecycleview;
    ArrayList<Song> songArrayList;
    @Inject
   SortPresenterImpl sortPresenter;
    SortSongAdapter sortSongAdapter;
    MediaPlayer mediaPlayer;
   public static TextView tvNameSongBottom , tvNameArtistBottom;
   public static ImageButton imgButtonPauseBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        init();
        atc();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerSortViewComponent.builder()
                .appComponent(appComponent)
                .sortViewModule(new SortViewModule(this))
                .build()
                .inject(this);
    }

    private void atc() {
        setSupportActionBar(toolbarSort);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.playQueue);
        toolbarSort.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
              //  mediaPlayer.stop();
                songArrayList.clear();
            }
        });
    }

    private void init() {
        toolbarSort = findViewById(R.id.toolBarSortActivity);
        sortSongRecycleview = findViewById(R.id.recycleViewSortSong);
        tvNameSongBottom = findViewById(R.id.tvNameSong);
        tvNameArtistBottom = findViewById(R.id.tvNameArtist);
        imgButtonPauseBottom = findViewById(R.id.imgButtonPause);

        Intent intent = getIntent();
        if (intent.hasExtra("listSong")) {
            songArrayList = intent.getParcelableArrayListExtra("listSong");
           // arrSong.add(song);
        }
            initPresenter();
        sortPresenter.onLoadSongSuccess(songArrayList);

    }

//    public void playSong() {
//        long id = songArrayList.get(0).getId();
//        Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//        try {
//            mediaPlayer.setDataSource(getApplicationContext(), contentUri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaPlayer.start();
//    }

    private void initPresenter(){
        sortPresenter = new SortPresenterImpl(this);
    }
    @Override
    public void showSong(ArrayList<Song> songs) {

        sortSongAdapter = new SortSongAdapter(SortActivity.this, songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SortActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        sortSongRecycleview.setLayoutManager(linearLayoutManager);
        sortSongRecycleview.setAdapter(sortSongAdapter);
    }


}
