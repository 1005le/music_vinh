package com.example.music_vinh.view.impl;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SortSongAdapter;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.DaggerSortViewComponent;
import com.example.music_vinh.injection.SortViewModule;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.SortPresenter;
import com.example.music_vinh.presenter.impl.SortPresenterImpl;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.view.SortView;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.CustomTouchListener;
import com.example.music_vinh.view.custom.StorageUtil;
import com.example.music_vinh.view.custom.onItemClickListener;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SortActivity extends BaseActivity implements SortView, View.OnClickListener {

    private static final String TAG = "SortActivity";
    @BindView(R.id.toolBarSortActivity)
    Toolbar toolbarSort;
    @BindView(R.id.recycleViewSortSong)
    RecyclerView sortSongRecycleview;

    @BindView(R.id.linearBottom)
    RelativeLayout linearLayoutBottom;

    @BindView(R.id.tvNameSongBottom)
    TextView tvNameSong;
    @BindView(R.id.tvNameArtistBottom)
    TextView tvNameArtist;

    @BindView(R.id.imgButtonPause)
    ImageButton imgPause;

    @BindView(R.id.imgButtonPlay)
    ImageButton imgBottomPlay;

    public static ArrayList<Song> songArrayList;
    public Song song;
    public int audioIndex;
    private MusicService mMusicService;
    private int mProgess;
    SeekBar seekBar;
    private int totalTime, currentTime;

    @Inject
    SortPresenter sortPresenter;

    SortSongAdapter sortSongAdapter;
    MediaPlayer mediaPlayer;
   // private MediaPlayerService player;
    boolean serviceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        seekBar = findViewById(R.id.seekBarBottom);
        bindServiceMedia();
        ButterKnife.bind(this);
        init();
        atc();
        loadAudioInfo();
        register_currentTimeAudio();
        register_DataSongFragment();
        register_durationAudio();

        eventClick();
        sortSongRecycleview.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {
                mMusicService.setSongs(songArrayList);
                mMusicService.setCurrentSong(index);
               // Log.d("songSort", songArrayList.get(index).getName()+"");
//                if (mProgess > 0) {
//                    mMusicService.seekTo(mProgess);
//                } else {
                    mMusicService.playSong();
               // }
            }
        }));
    }

    private void bindServiceMedia() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(Constants.ACTION_BIND_SERVICE);
        bindService(intent, mSCon, BIND_AUTO_CREATE);
    }

    private ServiceConnection mSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
           // mMusicService.setListener(SortActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSCon = null;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadAudioInfo() {
        Intent loadAudioIntent = new Intent(Constants.LOAD_AUDIO);
        sendBroadcast(loadAudioIntent);
    }

    private BroadcastReceiver dataSongFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            StorageUtil storage = new StorageUtil(getApplicationContext());
            songArrayList= storage.loadAudio();
            audioIndex = storage.loadAudioIndex();

            tvNameSong.setText(songArrayList.get(audioIndex).getName());
            tvNameArtist.setText(songArrayList.get(audioIndex).getNameArtist());
            getDataBottom();
        }
    };

    private void register_DataSongFragment() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND);
        registerReceiver(dataSongFragment, filter);
    }

    private BroadcastReceiver durationAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            totalTime = intent.getIntExtra(Constants.DURATION,0);
            seekBar.setMax(totalTime *1000);
        }
    };

    private void register_durationAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND_DURATION);
        registerReceiver(durationAudio, filter);
    }

    private BroadcastReceiver currentTimeAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int current = intent.getIntExtra(Constants.CURRENT_TIME, 0);
            seekBar.setProgress(current *1000);
            statusAudio();
        }
    };
    public void register_currentTimeAudio() {
        LocalBroadcastManager.getInstance(SortActivity.this).registerReceiver(currentTimeAudio, new IntentFilter(Constants.SEND_CURRENT));
    }
    private void statusAudio(){
        if (mMusicService.isPlay()) {
            imgPause.setVisibility(View.INVISIBLE);
            imgBottomPlay.setVisibility(View.VISIBLE);
        } else {
            imgPause.setVisibility(View.VISIBLE);
            imgBottomPlay.setVisibility(View.INVISIBLE);
        }
    }

    private void getDataBottom() {
        linearLayoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SortActivity.this, PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.KEY_SONGS, songArrayList);
                bundle.putInt(Constants.KEY_POSITION, audioIndex);
                intent.putExtra(Constants.KEY_BUNDLE, bundle);
               // intent.putExtra(Constants.KEY_PROGESS,mMediaPlayer.getCurrentPosition());
                startActivity(intent);
            }
        });
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
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.KEY_SONGS, songArrayList);
                bundle.putInt(Constants.KEY_POSITION, audioIndex);
                intent.putExtra(Constants.KEY_BUNDLE, bundle);
                // intent.putExtra(Constants.KEY_PROGESS,mMediaPlayer.getCurrentPosition());
                startActivity(intent);

//                Intent intent = new Intent(SortActivity.this, PlayActivity.class);
//                intent.putExtra("dragSong",(ArrayList)songArrayList);
//                startActivity(intent);
                finish();
                //  mediaPlayer.stop();
                // songArrayList.clear();
            }
        });
    }
    public void eventClick(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMusicService.seekTo(seekBar.getProgress());
            }
        });
        imgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d("click","click");
                if (mMusicService.isPlay()) {
                    mMusicService.pauseSong();
                } else {
                    mMusicService.continuesSong();
                }
                imgPause.setVisibility(View.INVISIBLE);
                imgBottomPlay.setVisibility(View.VISIBLE);
                //  imgPause.setImageResource(R.drawable.ic_stop);
            }
        });
        imgBottomPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMusicService.isPlay()) {
                    mMusicService.pauseSong();
                } else {
                    mMusicService.continuesSong();
                }
                imgPause.setVisibility(View.VISIBLE);
                imgBottomPlay.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void init() {
        Intent intent = getIntent();
        songArrayList = intent.getParcelableArrayListExtra("listSong");
        //  songArrayList = new StorageUtil(getApplicationContext()).loadAudio();
        // arrSong.add(song);
        // songArrayList = new StorageUtil(getApplicationContext()).loadAudio();
        initPresenter();
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

    private void initPresenter() {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPlay:
                if (mMusicService.isPlay()) {
                    mMusicService.pauseSong();
                } else {
                    mMusicService.continuesSong();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSCon != null) {
            unbindService(mSCon);
        }
        unregisterReceiver(dataSongFragment);
        unregisterReceiver(currentTimeAudio);
        unregisterReceiver(durationAudio);
    }


}
