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
import com.example.music_vinh.adapter.SongInAlbumAdapter;
import com.example.music_vinh.injection.AlbumInfoViewModule;
import com.example.music_vinh.injection.AppComponent;
import com.example.music_vinh.injection.DaggerAlbumInfoViewComponent;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.AlbumInfoPresenter;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.view.AlbumInfoView;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.CustomTouchListener;
import com.example.music_vinh.view.custom.onItemClickListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumInfoActivity extends BaseActivity implements AlbumInfoView {


    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbarDanhSach)
    Toolbar toolbar;
    @BindView(R.id.recyclerViewListSong)
    RecyclerView listSongrecyclerView;
     Album album ;
   public static ArrayList<Song> songArrayList;
   SongInAlbumAdapter songInAlbumAdapter;

   @Inject
   AlbumInfoPresenter albumInfoPresenter;
    @BindView(R.id.imgAlbumInfo)
    ImageView imgAlbumInfo;
    @BindView(R.id.imgIconAlbum)
    ImageView imgIconAlbum;
    @BindView(R.id.tvNameAlbumInfo)
    TextView tvNameAlbumInfo;
    @BindView(R.id.tvamountSongA)
    TextView tvamountSongA;


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
    private long totalTime, currentTime;

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
        register_DataSong();
        register_currentTimeAudio();
        evenClick();
    }
    public void evenClick(){
        listSongrecyclerView.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {
//                mMusicService.setSongs(songArrayList);
//                mMusicService.setCurrentSong(index);
               // Log.d("songSort", songArrayList.get(index).getName()+"");
//                if (mProgess > 0) {
//                    mMusicService.seekTo(mProgess);
//                } else {
              //  mMusicService.playSong();
                // }
                Intent intent = new Intent(AlbumInfoActivity.this, PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.KEY_SONGS,AlbumInfoActivity.songArrayList);
                bundle.putInt(Constants.KEY_POSITION,index);
                intent.putExtra(Constants.KEY_BUNDLE,bundle);
                Log.d("nameAl",songArrayList.get(index).getName());
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
        Intent loadAudioIntent = new Intent("load_audio");
        sendBroadcast(loadAudioIntent);
    }

    private BroadcastReceiver dataSongAlbum = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // StorageUtil storage = new StorageUtil(getApplicationContext());
            songArrayList = intent.getParcelableArrayListExtra(Constants.KEY_SONGS);
            audioIndex = intent.getIntExtra(Constants.KEY_POSITION,0);
            song = songArrayList.get(audioIndex);
          //  Log.d("songSortRece", songArrayList.get(audioIndex).getName()+"");
            totalTime = intent.getIntExtra("duration",0);
            currentTime = intent.getIntExtra("currentTime",0);

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
       // Log.d("album", "REGISTER");
        IntentFilter filter = new IntentFilter("send");
        registerReceiver(dataSongAlbum, filter);
    }
    private BroadcastReceiver currentTimeAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the new media index form SharedPreferences
            currentTime = intent.getIntExtra("currentTime",0);
//            Log.d("timeMain", totalTime+"currentTime"+currentTime);
            seekBar.setProgress((int) mMusicService.getCurrentPosition());
        }
    };

    private void register_currentTimeAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter("sendCurrent");
        registerReceiver(currentTimeAudio, filter);
    }

    private void getDataBottom() {
        linearLayoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumInfoActivity.this, PlayActivity.class);
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
        DaggerAlbumInfoViewComponent.builder()
                .appComponent(appComponent)
                .albumInfoViewModule(new AlbumInfoViewModule(this))
                .build()
                .inject(this);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        album = (Album) intent.getParcelableExtra("albumArrayList");

    }
    private void getData() {

        Drawable img = Drawable.createFromPath(album.getImages());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            collapsingToolbarLayout.setBackground(img);
        }
          imgAlbumInfo.setImageDrawable(img);

         imgIconAlbum.setImageDrawable(img);
        tvNameAlbumInfo.setText(album.getName());
        tvamountSongA.setText(album.getAmountSong()+"songs");
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
        collapsingToolbarLayout.setTitle(album.getNameArtist());
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    private void initPresenter(){
        albumInfoPresenter = new AlbumInfoPresenterImpl(this);
    }

    @Override
    public void showSong(ArrayList<Song> songs) {
        songInAlbumAdapter = new SongInAlbumAdapter(AlbumInfoActivity.this, songs);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AlbumInfoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listSongrecyclerView.setLayoutManager(linearLayoutManager);
        listSongrecyclerView.setAdapter(songInAlbumAdapter);

    }
    private void doStuff() {
        songArrayList= new ArrayList<>();
        songArrayList = getSongAlbum();
      //  albumInfoPresenter.onLoadSongSuccess(songArrayList);
        albumInfoPresenter.loadData();
    }
    public ArrayList<Song> getSongAlbum() {
        ContentResolver contentResolver = getContentResolver();
        Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.wtf("SKJDBKJ", mediaUri.toString());
        Cursor mediaCursor = contentResolver.query(mediaUri, null, null, null, null);

        // if the cursor is null.
        if(mediaCursor != null && mediaCursor.moveToFirst())
        {
            Log.wtf("DSJK", "entered cursor");
            //get Columns
            int titleColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumId = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int albumName = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            // Store the title, id and artist name in Song Array list.
            do
            {
                long thisId = mediaCursor.getLong(idColumn);
                long thisalbumId = mediaCursor.getLong(albumId);
                String thisTitle = mediaCursor.getString(titleColumn);
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisAlbumName = mediaCursor.getString(albumName);
                String thisDuration = mediaCursor.getString(durationColumn);

                // Add the info to our array.
                if(album.getId() == thisalbumId)
                {
                    Log.wtf("SAME2SAME", String.valueOf(thisalbumId));
                    Log.wtf("SAME2SAME", String.valueOf(this.album.getId()));
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
                if (TextUtils.isEmpty(newText)) {
//                    adapter.filter("");
//                    listView.clearTextFilter();


                } else {
                    // adapter.filter(newText);
                }
                return true;
            }
        });
        return true;
    }
}
