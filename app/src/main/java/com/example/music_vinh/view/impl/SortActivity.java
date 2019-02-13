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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.adapter.SortSongAdapter;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.DaggerSortViewComponent;
import com.example.music_vinh.injection.PlaySongViewModule;
import com.example.music_vinh.injection.SortViewModule;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.SortPresenter;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.presenter.impl.SortPresenterImpl;
import com.example.music_vinh.view.SortView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.music_vinh.view.impl.PlayActivity.song;

public class SortActivity extends BaseActivity implements SortView {

    @BindView(R.id.toolBarSortActivity)
    Toolbar toolbarSort;
    @BindView(R.id.recycleViewSortSong)
    RecyclerView sortSongRecycleview;

    @BindView(R.id.tvNameSongBottom)
     TextView tvNameSongBottom ;
    @BindView(R.id.tvNameArtistBottom)
      TextView tvNameArtistBottom;
    @BindView(R.id.imgButtonPause)
     ImageButton imgButtonPauseBottom;

    public static ArrayList<Song> songArrayList;
    @Inject
    SortPresenter sortPresenter;

    SortSongAdapter sortSongAdapter;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        ButterKnife.bind(this);
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

                Intent intent = new Intent(SortActivity.this, PlayActivity.class);
                intent.putExtra("dragSong",(ArrayList)songArrayList);
                startActivity(intent);
                finish();
              //  mediaPlayer.stop();
                songArrayList.clear();
            }
        });
    }

    private void init() {

        Intent intent = getIntent();
        if (intent.hasExtra("listSong")) {
            songArrayList = intent.getParcelableArrayListExtra("listSong");
           // arrSong.add(song);
        }
            initPresenter();
        //sortPresenter.onLoadSongSuccess(songArrayList);

          sortPresenter.loadData();

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        sortSongRecycleview.addItemDecoration(divider);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder dragged, RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(songArrayList, position_dragged, position_target);

                sortSongAdapter.notifyItemMoved(position_dragged, position_target);

                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        helper.attachToRecyclerView(sortSongRecycleview);
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
