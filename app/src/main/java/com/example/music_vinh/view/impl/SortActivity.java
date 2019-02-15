package com.example.music_vinh.view.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SortSongAdapter;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.DaggerSortViewComponent;
import com.example.music_vinh.injection.SortViewModule;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.SortPresenter;
import com.example.music_vinh.presenter.impl.SortPresenterImpl;
import com.example.music_vinh.view.SortView;
import com.example.music_vinh.view.custom.CustomTouchListener;
import com.example.music_vinh.view.custom.onItemClickListener;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private MediaPlayerService player;
    boolean serviceBound = false;
  //  public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.music_vinh.view.impl.PlayNewAudio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        ButterKnife.bind(this);
        init();
        atc();

        sortSongRecycleview.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {
                playAudio(index);
            }
        }));
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("serviceStatus");
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(songArrayList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(SongFragment.Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }


}
