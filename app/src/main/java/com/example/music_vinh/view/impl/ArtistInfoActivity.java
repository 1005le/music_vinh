package com.example.music_vinh.view.impl;

import android.Manifest;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongInArtistAdapter;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.ArtistInfoViewModule;
import com.example.music_vinh.injection.DaggerArtistInfoViewComponent;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.ArtistInfoPresenter;
import com.example.music_vinh.presenter.impl.ArtistInfoPresenterImpl;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.view.ArtistInfoView;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.CustomTouchListener;
import com.example.music_vinh.view.custom.onItemClickListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistInfoActivity extends BaseActivity implements ArtistInfoView {

    @BindView(R.id.coordinatorArtist)
    CoordinatorLayout coordinatorLayoutArtist;
    @BindView(R.id.collapsingToolbarArtist)
    CollapsingToolbarLayout collapsingToolbarLayoutArtist;
    @BindView(R.id.toolbarArtistInfo)
    Toolbar toolbarArtist;
    @BindView(R.id.SongInArtistrecyclerView)
    RecyclerView songInArtistRecycleView;
    Artist artist;
   public static ArrayList<Song> songArrayList;
    SongInArtistAdapter songInArtistAdapter;
    @BindView(R.id.imgViewArtist)
    ImageView imgViewArtist;

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

    public int audioIndex;
    private MusicService mMusicService;
    private int mProgess;
    SeekBar seekBar;
    public Song song;
    private long totalTime, currentTime,currentPosition;

    @Inject
    ArtistInfoPresenter artistInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);
        getDataIntent();
        seekBar = findViewById(R.id.seekBarBottom);
        ButterKnife.bind(this);
        act();
       getData();
        initPresenter();
        doStuff();

        bindServiceMedia();
        loadAudioInfo();
        register_DataSong();
        register_currentTimeAudio();
        eventClick();
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

        songInArtistRecycleView.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {

                Intent intent = new Intent(ArtistInfoActivity.this, PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.KEY_SONGS,ArtistInfoActivity.songArrayList);
                bundle.putInt(Constants.KEY_POSITION,index);
                intent.putExtra(Constants.KEY_BUNDLE,bundle);
                Log.d("nameArtist",ArtistInfoActivity.songArrayList.get(index).getName());
                // intent.putExtra(Constants.KEY_PROGESS,currentPosition);
                startActivity(intent);
            }
        }));

    }
    private void bindServiceMedia() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(Constants.ACTION_BIND_SERVICE);
        // startService(intent);
        bindService(intent, mSCon, BIND_AUTO_CREATE);
    }

    private ServiceConnection mSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
           // mMusicService.setListener(ArtistInfoActivity.this);
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
        unregisterReceiver(dataSongAlbum);
    }
    private void loadAudioInfo() {
        Intent loadAudioIntent = new Intent(Constants.LOAD_AUDIO);
        sendBroadcast(loadAudioIntent);
    }

    private BroadcastReceiver dataSongAlbum = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // StorageUtil storage = new StorageUtil(getApplicationContext());
            songArrayList = intent.getParcelableArrayListExtra(Constants.KEY_SONGS);
            audioIndex = intent.getIntExtra(Constants.KEY_POSITION,0);
            song = songArrayList.get(audioIndex);
            Log.d("songSortRece", songArrayList.get(audioIndex).getName()+"");
            totalTime = intent.getIntExtra(Constants.DURATION,0);
            currentPosition = intent.getIntExtra(Constants.KEY_PROGESS,0);
           // Log.d("timeSort", totalTime+"currentTime"+currentTime);

            seekBar.setMax((int) totalTime);
            seekBar.setProgress((int) currentTime);
            tvNameSong.setText(song.getName());
            tvNameArtist.setText(song.getNameArtist());
            imgPause.setVisibility(View.INVISIBLE);
            imgBottomPlay.setVisibility(View.VISIBLE);
            getDataBottom();
        }
    };

    private void register_DataSong() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND);
        registerReceiver(dataSongAlbum, filter);
    }

    private BroadcastReceiver currentTimeAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the new media index form SharedPreferences
            currentTime = intent.getIntExtra(Constants.CURRENT_TIME,0);
//            Log.d("timeMain", totalTime+"currentTime"+currentTime);
            seekBar.setProgress((int) mMusicService.getCurrentPosition());
        }
    };

    private void register_currentTimeAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND_CURRENT);
        registerReceiver(currentTimeAudio, filter);
    }

    private void getDataBottom() {
        linearLayoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArtistInfoActivity.this, PlayActivity.class);
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
        DaggerArtistInfoViewComponent.builder()
                .appComponent(appComponent)
                .artistInfoViewModule(new ArtistInfoViewModule(this))
                .build()
                .inject(this);
    }


    private void getData() {
        Drawable img = Drawable.createFromPath(artist.getImages());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            collapsingToolbarLayoutArtist.setBackground(img);
        }
        // collapsingToolbarLayout.setBackground(img);
        imgViewArtist.setImageDrawable(img);
//        imgIconAlbum.setImageDrawable(img);
//        tvNameAlbumInfo.setText(album.getName());
//        tvamountSongA.setText(album.getAmountSong()+"songs");
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        artist = (Artist) intent.getParcelableExtra("artistArrayList");
    }
    private void act() {
        setSupportActionBar(toolbarArtist);
        //getSupportActionBar().setTitle(album.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarArtist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // collapsingToolbarLayoutArtist.setTitle(artist.getName());
        collapsingToolbarLayoutArtist.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayoutArtist.setCollapsedTitleTextColor(Color.WHITE);
    }


    private void initPresenter(){
        artistInfoPresenter = new ArtistInfoPresenterImpl(this);
    }
    @Override
    public void showSong(ArrayList<Song> songs) {
        songInArtistAdapter = new SongInArtistAdapter(ArtistInfoActivity.this, songs);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArtistInfoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        songInArtistRecycleView.setLayoutManager(linearLayoutManager);
        songInArtistRecycleView.setAdapter(songInArtistAdapter);
    }

    private void doStuff() {
        songArrayList= new ArrayList<>();
         songArrayList = getSongArtist();

        artistInfoPresenter.loadData();
    }

    public ArrayList<Song> getSongArtist() {
        ContentResolver contentResolver = getContentResolver();
        Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor mediaCursor = contentResolver.query(mediaUri, null, null, null, null);

        // if the cursor is null.
        if(mediaCursor != null && mediaCursor.moveToFirst())
        {
            //get Columns
            int titleColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int artistId = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
            int albumName = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            // Store the title, id and artist name in Song Array list.
            do
            {
                long thisId = mediaCursor.getLong(idColumn);
                long thisArtistId = mediaCursor.getLong(artistId);
                String thisTitle = mediaCursor.getString(titleColumn);
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisAlbumName = mediaCursor.getString(albumName);
                String thisDuration = mediaCursor.getString(durationColumn);

                // Add the info to our array.
                if(artist.getId() == thisArtistId)
                {
                    songArrayList.add(new Song(thisId, thisTitle, thisArtist,thisAlbumName,"",Long.parseLong(thisDuration)));
                }
            }
            while (mediaCursor.moveToNext());
            // For best practices, close the cursor after use.
            mediaCursor.close();
        }
        return songArrayList;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.search_view, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.menu_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }
}
