package com.example.music_vinh.view.impl;

import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongInAlbumAdapter;
import com.example.music_vinh.injection.AlbumInfoViewModule;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.DaggerAlbumInfoViewComponent;
import com.example.music_vinh.interactor.AlbumInfoInteractor;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.AlbumInfoPresenter;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.CustomTouchListener;
import com.example.music_vinh.view.custom.StorageUtil;
import com.example.music_vinh.view.custom.onItemClickListener;
import com.example.music_vinh.view.search.SearchableActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumInfoActivity extends BaseActivity implements AlbumInfoView {

    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbarDanhSach) Toolbar toolbar;
    @BindView(R.id.recyclerViewListSong) RecyclerView listSongrecyclerView;
    Album album;
    public static ArrayList<Song> songArrayListAlbum;

    @Inject
    SongInAlbumAdapter songInAlbumAdapter;
    @Inject
    AlbumInfoPresenter albumInfoPresenter;
    @Inject
    LinearLayoutManager linearLayoutManager;

    @BindView(R.id.imgAlbumInfo) ImageView imgAlbumInfo;
    @BindView(R.id.imgIconAlbum) ImageView imgIconAlbum;
    @BindView(R.id.tvNameAlbumInfo) TextView tvNameAlbumInfo;
    @BindView(R.id.tvamountSongA) TextView tvamountSongA;


    @BindView(R.id.linearBottom) RelativeLayout linearLayoutBottom;
    @BindView(R.id.tvNameSongBottom) TextView tvNameSong;
    @BindView(R.id.tvNameArtistBottom) TextView tvNameArtist;
    @BindView(R.id.imgButtonPause) ImageButton imgPause;
    @BindView(R.id.imgButtonPlay) ImageButton imgBottomPlay;

    public int audioIndex;
    private MusicService mMusicService;
    private int mProgess;
    SeekBar seekBar;
    public Song song;
    private int totalTime, currentTime, currentPosition;
    // int indexAlbum;
    Long idAlbum;
    String indexAlbum;
    List<Album> albumList;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_infor);
        ButterKnife.bind(this);
        getDataIntent();

        getData();
        act();
        seekBar = findViewById(R.id.seekBarBottom);
        //lay bai hat
        initPresenter();
        doStuff();

        bindServiceMedia();
        loadAudioInfo();
        register_DataSongFragment();
        register_durationAudio();
        register_currentTimeAudio();
        evenClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // loadAudioInfo();
    }

    public void evenClick() {
        listSongrecyclerView.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {

                Intent intent = new Intent(AlbumInfoActivity.this, PlayActivity.class);
                StorageUtil storage = new StorageUtil(getApplicationContext());
                storage.storeAudio(songArrayListAlbum);
                storage.storeAudioIndex(index);
                intent.putExtra(Constants.PLAY_TYPE, Constants.PLAY);
                startActivity(intent);
            }
        }));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMusicService.seekTo(seekBar.getProgress() * 1000);
            }
        });
        imgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMusicService.isPlay()) {
                    mMusicService.pauseSong();
                } else {
                    mMusicService.continuesSong();
                }
                imgPause.setVisibility(View.INVISIBLE);
                imgBottomPlay.setVisibility(View.VISIBLE);
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

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AlbumInfoActivity.class);
        return intent;
    }

    private void bindServiceMedia() {
        Intent intent = new Intent(AlbumInfoActivity.this, MusicService.class);
        intent.setAction(Constants.ACTION_BIND_SERVICE);
        bindService(intent, mSCon, BIND_AUTO_CREATE);
    }

    private ServiceConnection mSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSCon = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSCon != null) {
            unbindService(mSCon);
        }
        LocalBroadcastManager.getInstance(AlbumInfoActivity.this).unregisterReceiver(dataSongFragment);
        LocalBroadcastManager.getInstance(AlbumInfoActivity.this).unregisterReceiver(currentTimeAudio);
        LocalBroadcastManager.getInstance(AlbumInfoActivity.this).unregisterReceiver(durationAudio);
    }

    private void loadAudioInfo() {
        Intent loadAudioIntent = new Intent(Constants.LOAD_AUDIO);
        sendBroadcast(loadAudioIntent);
    }

    private BroadcastReceiver dataSongFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            StorageUtil storage = new StorageUtil(getApplicationContext());
            if (storage.loadAudio() != null && storage.loadAudioIndex() > -1) {
                audioIndex = storage.loadAudioIndex();
                tvNameSong.setText(storage.loadAudio().get(audioIndex).getName());
                tvNameArtist.setText(storage.loadAudio().get(audioIndex).getNameArtist());
                getDataBottom();
            }
        }
    };

    private void register_DataSongFragment() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND);
        LocalBroadcastManager.getInstance(AlbumInfoActivity.this).registerReceiver(dataSongFragment, filter);
    }

    private BroadcastReceiver durationAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            totalTime = intent.getIntExtra(Constants.DURATION, 0);
            seekBar.setMax(totalTime);
        }
    };

    private void register_durationAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND_DURATION);
        LocalBroadcastManager.getInstance(AlbumInfoActivity.this).registerReceiver(durationAudio, filter);
    }

    private BroadcastReceiver currentTimeAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int current = intent.getIntExtra(Constants.CURRENT_TIME, 0);
            //Log.d("curent", current+"");
            seekBar.setProgress(current);
            statusAudio();
        }
    };

    public void register_currentTimeAudio() {
        IntentFilter filter = new IntentFilter(Constants.SEND_CURRENT);
        LocalBroadcastManager.getInstance(AlbumInfoActivity.this).registerReceiver(
                currentTimeAudio, filter);
    }

    private void statusAudio() {
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
                Intent intent = new Intent(AlbumInfoActivity.this, PlayActivity.class);
                intent.putExtra(Constants.PLAY_TYPE, Constants.RESUME);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerAlbumInfoViewComponent.builder()
                .appComponent(appComponent)
                .albumInfoViewModule(new AlbumInfoViewModule(this))
                .build()
                .inject(this);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("album")) {
            album = intent.getParcelableExtra("album");
            idAlbum = album.getId();
        }
        /*nhân tu search
         * */
        if (intent.hasExtra("album_ID")) {
            albumList = AlbumFragment.albumList;
            indexAlbum = intent.getStringExtra("album_ID");
            album = albumList.get(Integer.parseInt(indexAlbum) -1);
            idAlbum = Long.parseLong(indexAlbum);
        }
    }

    private void getData() {
        Drawable img = Drawable.createFromPath(album.getImages());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            collapsingToolbarLayout.setBackground(img);
        }
        imgAlbumInfo.setImageDrawable(img);
        imgIconAlbum.setImageDrawable(img);
        tvNameAlbumInfo.setText(album.getName());
        tvamountSongA.setText(album.getAmountSong() + R.string.songs);
    }

    private void act() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(album.getName());
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout.setTitle(album.getName());
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    private void initPresenter() {
        albumInfoPresenter = new AlbumInfoPresenterImpl(this);
    }

    @Override
    public void showSong(ArrayList<Song> songs) {
        songInAlbumAdapter = new SongInAlbumAdapter(AlbumInfoActivity.this, songs);

        linearLayoutManager = new LinearLayoutManager(AlbumInfoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listSongrecyclerView.setLayoutManager(linearLayoutManager);
        listSongrecyclerView.setAdapter(songInAlbumAdapter);

    }

    private void doStuff() {
        songArrayListAlbum = new ArrayList<>();
        songArrayListAlbum = getSongAlbum();
        //  albumInfoPresenter.onLoadSongSuccess(songArrayList);
        albumInfoPresenter.loadData();
    }

    public ArrayList<Song> getSongAlbum() {
        ContentResolver contentResolver = getContentResolver();
        Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor mediaCursor = contentResolver.query(mediaUri, null, null, null, null);

        // if the cursor is null.
        if (mediaCursor != null && mediaCursor.moveToFirst()) {
            //get Columns
            int titleColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumId = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int albumName = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songPath = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int durationColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            // Store the title, id and artist name in Song Array list.
            do {
                long thisId = mediaCursor.getLong(idColumn);
                long thisalbumId = mediaCursor.getLong(albumId);
                String thisTitle = mediaCursor.getString(titleColumn);
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisAlbumName = mediaCursor.getString(albumName);
                String thisPath = mediaCursor.getString(songPath);
                String thisDuration = mediaCursor.getString(durationColumn);

                // Add the info to our array.
               // if (album.getId() == thisalbumId)
                if( (idAlbum) == thisalbumId) {
                    songArrayListAlbum.add(new Song(thisId, thisTitle, thisArtist, thisAlbumName, thisPath, Long.parseLong(thisDuration)));
                }

            }
            while (mediaCursor.moveToNext());
            // For best practices, close the cursor after use.
            mediaCursor.close();
        }
        return songArrayListAlbum;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) searchItem.getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchableActivity.class)));
        mSearchView.setIconifiedByDefault(true);

        return true;
    }


}
