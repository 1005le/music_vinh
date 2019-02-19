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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SortSongAdapter;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.DaggerSortViewComponent;
import com.example.music_vinh.injection.SortViewModule;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.SortPresenter;
import com.example.music_vinh.presenter.impl.SortPresenterImpl;
import com.example.music_vinh.service.MediaPlayerService;
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

import static com.example.music_vinh.service.MediaPlayerService.ACTION_PLAY;

public class SortActivity extends BaseActivity implements SortView, ServiceCallback, View.OnClickListener {

    private static final String TAG = "SortActivity";
    @BindView(R.id.toolBarSortActivity)
    Toolbar toolbarSort;
    @BindView(R.id.recycleViewSortSong)
    RecyclerView sortSongRecycleview;
    @BindView(R.id.linearBottom)
    LinearLayout linearLayoutBottom;

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

    @Inject
    SortPresenter sortPresenter;

    SortSongAdapter sortSongAdapter;
    MediaPlayer mediaPlayer;

   // private MediaPlayerService player;
    boolean serviceBound = false;
    public static final String Broadcast_PLAY_NEW_AUDIO_Sort = "com.example.music_vinh.view.impl.playNewAudioSort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        bindServiceMedia();
        ButterKnife.bind(this);
        init();
        atc();
        loadAudioInfo();
        register_DataSong();
        sortSongRecycleview.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {
//                playAudio(index);
//                tvNameSongBottom.setText(songArrayList.get(index).getName());
//                tvNameArtistBottom.setText(songArrayList.get(index).getNameArtist());

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
            mMusicService.setListener(SortActivity.this);
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
        Intent loadAudioIntent = new Intent("load_audio");
        sendBroadcast(loadAudioIntent);
    }

    private BroadcastReceiver dataSong = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // StorageUtil storage = new StorageUtil(getApplicationContext());
//            songArrayList = intent.getParcelableArrayListExtra(Constants.KEY_SONGS);
//            audioIndex = intent.getIntExtra(Constants.KEY_POSITION,0);
//            song = songArrayList.get(audioIndex);

            Log.d("hh", "a");
            StorageUtil storage = new StorageUtil(getApplicationContext());
            songArrayList = storage.loadAudio();
            audioIndex = storage.loadAudioIndex();
            song = songArrayList.get(audioIndex);

            tvNameSong.setText(song.getName());
            tvNameArtist.setText(song.getNameArtist());
            imgPause.setVisibility(View.INVISIBLE);
            imgBottomPlay.setVisibility(View.VISIBLE);
            getDataBottom();
        }
    };

    private void register_DataSong() {
        //Register playNewMedia receiver
        Log.d(TAG, "REGISTER");
        IntentFilter filter = new IntentFilter("send");
        registerReceiver(dataSong, filter);
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
                //   intent.putExtra(Constants.KEY_PROGESS,mMediaPlayer.getCurrentPosition());
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

//                Intent intent = new Intent(SortActivity.this, PlayActivity.class);
//                intent.putExtra("dragSong",(ArrayList)songArrayList);
//                startActivity(intent);
                finish();
                //  mediaPlayer.stop();
                // songArrayList.clear();
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
    public void postName(String songName, String author) {
        tvNameSong.setText(songName);
        tvNameArtist.setText(author);
    }

    @Override
    public void postTotalTime(long totalTime) {
//        mTextTotalTime.setText(mDateFormat.format(totalTime));
        //  circularSeekBar.setMax((int) totalTime);
    }

    //
    @Override
    public void postCurentTime(long currentTime) {
        //   tvTime.setText(mDateFormat.format(currentTime));
        //  if (!mTrackingSeekBar) {
        //  circularSeekBar.setProgress((int) currentTime);
        // }
    }

    @Override
    public void postPauseButon() {
        imgPause.setImageResource(R.drawable.ic_stop_seekbar);
    }

    @Override
    public void postStartButton() {
        imgPause.setImageResource(R.drawable.ic_play_seekbar);
    }


    @Override
    public void postShuffle(boolean isShuffle) {

    }

    @Override
    public void postLoop(boolean isLoop) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void postAvatar(String url) {

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("serviceStatus", serviceBound);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        serviceBound = savedInstanceState.getBoolean("serviceStatus");
//    }
//
//    //Binding this Client to the AudioPlayer Service
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
//            player = binder.getService();
//            serviceBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            serviceBound = false;
//        }
//    };
//
//    private void playAudio(int audioIndex) {
//        //Check is service is active
//        if (!serviceBound) {
//            //Store Serializable audioList to SharedPreferences
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            storage.storeAudio(songArrayList);
//            storage.storeAudioIndex(audioIndex);
//
//            Intent playerIntent = new Intent(this, MediaPlayerService.class);
//            startService(playerIntent);
//            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//
//            Intent broadcastIntent = new Intent("SortPlay");
//            sendBroadcast(broadcastIntent);
//        } else {
//            //Store the new audioIndex to SharedPreferences
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            // storage.storeAudio(songArrayList);
//            storage.storeAudioIndex(audioIndex);
//            //Service is active
//            //Send a broadcast to the service -> PLAY_NEW_AUDIO
//            Intent broadcastIntent = new Intent("SortPlay");
//            sendBroadcast(broadcastIntent);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSCon != null) {
            unbindService(mSCon);
        }
    }


}
