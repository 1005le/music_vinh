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
import com.example.music_vinh.adapter.SongInArtistAdapter;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.ArtistInfoViewModule;
import com.example.music_vinh.injection.DaggerArtistInfoViewComponent;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.ArtistInfoPresenter;
import com.example.music_vinh.presenter.impl.ArtistInfoPresenterImpl;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.view.ArtistInfoView;
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

public class ArtistInfoActivity extends BaseActivity implements ArtistInfoView {

    @BindView(R.id.coordinatorArtist) CoordinatorLayout coordinatorLayoutArtist;
    @BindView(R.id.collapsingToolbarArtist) CollapsingToolbarLayout collapsingToolbarLayoutArtist;
    @BindView(R.id.toolbarArtistInfo) Toolbar toolbarArtist;
    @BindView(R.id.SongInArtistrecyclerView) RecyclerView songInArtistRecycleView;
    Artist artist;
   public static ArrayList<Song> songArrayList;
    @BindView(R.id.imgViewArtist) ImageView imgViewArtist;

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
    private int totalTime, currentTime,currentPosition;
     ArrayList<Artist> artistListInfo;
   //  List<Artist> artistListInfo;
    String indexArtist;
    long idArtist;
    private SearchView mSearchView;

    @Inject
    ArtistInfoPresenter artistInfoPresenter;
     @Inject
    SongInArtistAdapter songInArtistAdapter;
     @Inject
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);
        getDataIntent();
        seekBar = findViewById(R.id.seekBarBottom);
        ButterKnife.bind(this);
        act();
       // getData();
        initPresenter();
        doStuff();

        bindServiceMedia();
        loadAudioInfo();

        register_DataSongFragment();
        register_durationAudio();
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
                StorageUtil storage = new StorageUtil(getApplicationContext());
                storage.storeAudio(songArrayList);
                storage.storeAudioIndex(index);
                intent.putExtra(Constants.PLAY_TYPE, Constants.PLAY);
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
        LocalBroadcastManager.getInstance(ArtistInfoActivity.this).unregisterReceiver(dataSongFragment);
        LocalBroadcastManager.getInstance(ArtistInfoActivity.this).unregisterReceiver(durationAudio);
        LocalBroadcastManager.getInstance(ArtistInfoActivity.this).unregisterReceiver(currentTimeAudio);
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
        LocalBroadcastManager.getInstance(ArtistInfoActivity.this).registerReceiver(dataSongFragment, filter);
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
        LocalBroadcastManager.getInstance(ArtistInfoActivity.this).registerReceiver(durationAudio, filter);
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
        LocalBroadcastManager.getInstance(ArtistInfoActivity.this).registerReceiver(
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
                Intent intent = new Intent(ArtistInfoActivity.this, PlayActivity.class);
                intent.putExtra(Constants.PLAY_TYPE, Constants.RESUME);
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
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ArtistInfoActivity.class);
        return intent;
    }

    private void getData() {

        Drawable img = Drawable.createFromPath(artist.getImages());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            collapsingToolbarLayoutArtist.setBackground(img);
        }
        imgViewArtist.setImageDrawable(img);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("artist")) {
            artist= intent.getParcelableExtra("artist");
            idArtist = artist.getId();
        }
        /*nhân tu search
         * */

        if (intent.hasExtra("artist_ID")) {
            artistListInfo = ArtistFragment.artistList;
            indexArtist = intent.getStringExtra("artist_ID");
            idArtist= Long.parseLong(indexArtist);
          //  artist = artistListInfo.get(Integer.parseInt(indexArtist));
        }

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
        //collapsingToolbarLayoutArtist.setTitle(artist.getName());
        collapsingToolbarLayoutArtist.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayoutArtist.setCollapsedTitleTextColor(Color.WHITE);
    }

    private void initPresenter(){
        artistInfoPresenter = new ArtistInfoPresenterImpl(this);
    }
    @Override
    public void showSong(ArrayList<Song> songs) {
        songInArtistAdapter = new SongInArtistAdapter(ArtistInfoActivity.this, songs);

        linearLayoutManager = new LinearLayoutManager(ArtistInfoActivity.this);
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
            int artistSong = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int durationColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            // Store the title, id and artist name in Song Array list.
            do
            {
                long thisId = mediaCursor.getLong(idColumn);
                long thisArtistId = mediaCursor.getLong(artistId);
                String thisTitle = mediaCursor.getString(titleColumn);
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisAlbumName = mediaCursor.getString(albumName);
                String thisPath = mediaCursor.getString(artistSong);
                String thisDuration = mediaCursor.getString(durationColumn);

                // Add the info to our array.
               // if(artist.getId() == thisArtistId)
                if(idArtist == thisArtistId)
                {
                    songArrayList.add(new Song(thisId, thisTitle, thisArtist,thisAlbumName,thisPath,Long.parseLong(thisDuration)));
                    collapsingToolbarLayoutArtist.setTitle(thisArtist);
                }
            }
            while (mediaCursor.moveToNext());
            // For best practices, close the cursor after use.
            mediaCursor.close();
        }
        return songArrayList;
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
