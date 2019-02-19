package com.example.music_vinh.view.impl;



import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.MainViewAdapter;
import com.example.music_vinh.injection.AppComponent;

import com.example.music_vinh.injection.DaggerMainViewComponent;
import com.example.music_vinh.injection.MainViewModule;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.service.MediaPlayerService;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.view.MainView;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.PlaybackStatus;
import com.example.music_vinh.view.custom.StorageUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.music_vinh.view.impl.AlbumFragment.albumAdapter;
import static com.example.music_vinh.view.impl.AlbumFragment.albumList;
import static com.example.music_vinh.view.impl.ArtistFragment.artistAdapter;
import static com.example.music_vinh.view.impl.ArtistFragment.artistList;
import static com.example.music_vinh.view.impl.SongFragment.songAdapter;
import static com.example.music_vinh.view.impl.SongFragment.songList;

public class  MainActivity extends BaseActivity implements ServiceCallback, View.OnClickListener{

    @BindView(R.id.myTabLayout)
    TabLayout tabLayout;
    @BindView(R.id.myViewPager)
    ViewPager viewPager;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolBarMainActivity)
    Toolbar toolbarMainActivity;
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
    @BindView(R.id.seekBarBottom)
    SeekBar seekBar;

    MainView mainView;

    public ArrayList<Song> songList;
    //public int audioIndex = -1;
    public int audioIndex ;
    public Song song;
    private MusicService mMusicService;
    private boolean mIsBound;
    private int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTab();
        act();
       // getDataBottom();
      /*  register_playAudio();
        register_stopAudio();
        register_nextAudio();
        register_preAudio();
        register_pauseAudio();*/

        register_DataSongFragment();
        bindServiceMedia();
      //  connectService();
        loadAudioInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMainViewComponent.builder()
                .appComponent(appComponent)
                .mainViewModule(new MainViewModule(mainView))
                .build()
                .inject(this);
    }

    private void act() {
        setSupportActionBar(toolbarMainActivity);
        getSupportActionBar().setTitle(getString(R.string.beauty));
    }
    /**
     * Khai báo các Tab
     */
    private void initTab() {
        MainViewAdapter mainViewAdapter = new MainViewAdapter(getSupportFragmentManager());
        mainViewAdapter.addFragment(new SongFragment(),getString(R.string.song));
        mainViewAdapter.addFragment(new AlbumFragment(),getString(R.string.album));
        mainViewAdapter.addFragment(new ArtistFragment(), getString(R.string.artist));
        viewPager.setAdapter(mainViewAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    //Nhan du lieu tu SongFragment
    private BroadcastReceiver dataSongFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//
//            Log.d("aa", "aaa");
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            songList = storage.loadAudio();
//            audioIndex = storage.loadAudioIndex();
//            song = songList.get(audioIndex);

            songList = intent.getParcelableArrayListExtra(Constants.KEY_SONGS);
            audioIndex = intent.getIntExtra(Constants.KEY_POSITION,0);
             totalTime = intent.getIntExtra(Constants.KEY_PROGESS,0);
            song = songList.get(audioIndex);
             Log.d("time", totalTime+"");
            tvNameSong.setText(song.getName());
            tvNameArtist.setText(song.getNameArtist());
            seekBar.setMax(totalTime);

           imgPause.setVisibility(View.INVISIBLE);
           imgBottomPlay.setVisibility(View.VISIBLE);
            getDataBottom();
        }
    };

    private void register_DataSongFragment() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter("send");
        registerReceiver(dataSongFragment, filter);
    }

    private void loadAudioInfo() {
        Intent loadAudioIntent = new Intent("load_audio");
        sendBroadcast(loadAudioIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSCon != null) {
            unbindService(mSCon);
        }
    }


//    private BroadcastReceiver stopAudio = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //Get the new media index form SharedPreferences
//            tvNameSong.setText("");
//            tvNameArtist.setText("");
//            imgPause.setVisibility(View.VISIBLE);
//            imgBottomPlay.setVisibility(View.INVISIBLE);
//        }
//    };
//
//    private void register_stopAudio() {
//        //Register playNewMedia receiver
//        IntentFilter filter = new IntentFilter("StopMusic");
//        registerReceiver(stopAudio, filter);
//    }
//
//    private BroadcastReceiver playAudio = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            songList = storage.loadAudio();
//            audioIndex = storage.loadAudioIndex();
//            song = songList.get(audioIndex);
//
//            tvNameSong.setText(song.getName());
//            tvNameArtist.setText(song.getNameArtist());
//            imgPause.setVisibility(View.INVISIBLE);
//            imgBottomPlay.setVisibility(View.VISIBLE);
//        }
//    };
//
//    private void register_playAudio() {
//        //Register playNewMedia receiver
//        IntentFilter filter = new IntentFilter(MediaPlayerService.ACTION_PLAY);
//        registerReceiver(playAudio, filter);
//    }
//
//    private BroadcastReceiver pauseAudio = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
////            StorageUtil storage = new StorageUtil(getApplicationContext());
////            songList = storage.loadAudio();
////            audioIndex = storage.loadAudioIndex();
////            song = songList.get(audioIndex);
////
////            tvNameSong.setText(song.getName());
////            tvNameArtist.setText(song.getNameArtist());
//            imgPause.setVisibility(View.VISIBLE);
//            imgBottomPlay.setVisibility(View.INVISIBLE);
//        }
//    };
//
//    private void register_pauseAudio() {
//        //Register playNewMedia receiver
//        IntentFilter filter = new IntentFilter(MediaPlayerService.ACTION_PAUSE);
//        registerReceiver(pauseAudio, filter);
//
//    }
//    private BroadcastReceiver nextAudio = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            songList = storage.loadAudio();
//            audioIndex = storage.loadAudioIndex();
//            song = songList.get(audioIndex);
//
//            tvNameSong.setText(song.getName());
//            tvNameArtist.setText(song.getNameArtist());
//            imgPause.setVisibility(View.INVISIBLE);
//            imgBottomPlay.setVisibility(View.VISIBLE);
//        }
//    };
//
//    private void register_nextAudio() {
//        //Register playNewMedia receiver
//        IntentFilter filter = new IntentFilter(MediaPlayerService.ACTION_NEXT);
//        registerReceiver(nextAudio, filter);
//    }
//
//    private BroadcastReceiver preAudio = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//             songList = storage.loadAudio();
//            audioIndex = storage.loadAudioIndex();
//            song = songList.get(audioIndex);
//
//            tvNameSong.setText(song.getName());
//            tvNameArtist.setText(song.getNameArtist());
//            imgPause.setVisibility(View.INVISIBLE);
//            imgBottomPlay.setVisibility(View.VISIBLE);
//        }
//    };
//
//    private void register_preAudio() {
//        //Register playNewMedia receiver
//        IntentFilter filter = new IntentFilter(MediaPlayerService.ACTION_PREVIOUS);
//        registerReceiver(preAudio, filter);
//    }

    private void getDataBottom() {
            linearLayoutBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.KEY_SONGS, songList);
                    bundle.putInt(Constants.KEY_POSITION,audioIndex);
                    intent.putExtra(Constants.KEY_BUNDLE,bundle);
                 //   intent.putExtra(Constants.KEY_PROGESS,mMediaPlayer.getCurrentPosition());
                    startActivity(intent);
                }
            });
    }
    /**
     * khoi tao search
     */
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.search_view, menu);

     final MenuItem myActionMenuItem = menu.findItem( R.id.menu_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String searchQuery) {

                final List<Song>filterModel = filter(songList, searchQuery);
                songAdapter.getFilte(filterModel);

                final List<Album>filterModelAlbum = filterAlbum(albumList, searchQuery);
                albumAdapter.getFilte(filterModelAlbum);

//                final List<Artist>filterModelArtist = filterArtist(artistList, searchQuery);
//                artistAdapter.getFilte(filterModelArtist);

                return true;
            }
        });
        return true;
    }

    private List<Song> filter(List<Song>listItem, String query){
        query = query.toLowerCase();
        final List<Song>filterModel = new ArrayList<>();

        for( Song item :listItem){
            final String text = item.getName().toLowerCase();
            if( text.startsWith(query)){
                filterModel.add(item);
            }
        }
        return filterModel;
    }

    private List<Album> filterAlbum(List<Album>listItem, String query){
        query = query.toLowerCase();
        final List<Album>filterModel = new ArrayList<>();

        for( Album item :listItem){
            final String text = item.getName().toLowerCase();
            if( text.startsWith(query)){
                filterModel.add(item);
            }
        }
        return filterModel;
    }

    private List<Artist> filterArtist(List<Artist>listItem, String query){
        query = query.toLowerCase();
        final List<Artist>filterModel = new ArrayList<>();

        for( Artist item :listItem){
            final String text = item.getName().toLowerCase();
            if( text.startsWith(query)){
                filterModel.add(item);
            }
        }
        return filterModel;
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
            mMusicService.setListener(MainActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSCon = null;
        }
    };

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
}

