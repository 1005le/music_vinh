package com.example.music_vinh.view.impl;



import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.MainViewAdapter;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.adapter.SongSearchAdapter;
import com.example.music_vinh.injection.AppComponent;

import com.example.music_vinh.injection.DaggerMainViewComponent;
import com.example.music_vinh.injection.MainViewModule;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
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
import static com.example.music_vinh.view.impl.SongFragment.songList;

public class  MainActivity extends BaseActivity implements  View.OnClickListener{

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

    //@BindView(R.id.seekBarBottom)
    SeekBar seekBar;

    MainView mainView;

    public ArrayList<Song> songList;
    //public int audioIndex = -1;
    public int audioIndex ;
    public Song song;
    private MusicService mMusicService;
    private boolean mIsBound;
    private long totalTime, currentTime, currentPosition;
    SongSearchAdapter songSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTab();
        act();
        seekBar = findViewById(R.id.seekBarBottom);
        bindServiceMedia();

        loadAudioInfo();
        register_DataSongFragment();
        register_currentTimeAudio();
       // imgPause.setOnClickListener(this);
        eventClick();
    }

//    public void getMusicSongArr() {
//        ContentResolver contentResolver = getContentResolver();
//        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Log.d("uri",songUri+"");
//        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
//        if (songCursor != null && songCursor.moveToFirst()) {
//            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
//            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
//            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
//            do {
//                String currentId = songCursor.getString(songId);
//                String currentTitle = songCursor.getString(songTitle);
//                String currentArtist = songCursor.getString(songArtist);
//                String currentAlbum = songCursor.getString(songAlbum);
//                String currentPath = songCursor.getString(songPath);
//                String currentDuration = songCursor.getString(songDuration);
//
//                songsearch.add(new Song(Long.parseLong(currentId),currentTitle, currentArtist,currentAlbum,currentPath, Long.parseLong(currentDuration)));
//
//            } while (songCursor.moveToNext());
//        }
//    }

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

    @Override
    protected void onResume() {
        super.onResume();
     //   mMusicService.playSong();
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

    private BroadcastReceiver dataSongFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            songList = intent.getParcelableArrayListExtra(Constants.KEY_SONGS);
            audioIndex = intent.getIntExtra(Constants.KEY_POSITION,0);
            totalTime = intent.getIntExtra(Constants.DURATION,0);
            currentPosition = intent.getIntExtra(Constants.KEY_PROGESS,0);
            song = songList.get(audioIndex);
          //  Log.d("songMain", songList.get(audioIndex).getName()+"");
//                if (mProgess > 0) {
//                    mMusicService.seekTo(mProgess);
//                } else {
           // mMusicService.playSong();
            tvNameSong.setText(song.getName());
            tvNameArtist.setText(song.getNameArtist());
            seekBar.setMax((int) totalTime);
          //  mMusicService.playSong();
           imgPause.setVisibility(View.INVISIBLE);
           imgBottomPlay.setVisibility(View.VISIBLE);
            getDataBottom();
        }
    };

    private void register_DataSongFragment() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND);
        registerReceiver(dataSongFragment, filter);
    }

    private void loadAudioInfo() {
        Intent loadAudioIntent = new Intent(Constants.LOAD_AUDIO);
        sendBroadcast(loadAudioIntent);
    }

    private BroadcastReceiver currentTimeAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the new media index form SharedPreferences
            currentTime = intent.getIntExtra(Constants.CURRENT_TIME,0);
           // Log.d("timeMain", totalTime+"currentTime"+currentTime);
            seekBar.setProgress((int) mMusicService.getCurrentPosition());
           // seekBar.setProgress((int)currentTime);
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
                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.KEY_SONGS, songList);
                    bundle.putInt(Constants.KEY_POSITION,audioIndex);
                    intent.putExtra(Constants.KEY_BUNDLE,bundle);
                    intent.putExtra(Constants.KEY_PROGESS,currentPosition);
                    startActivity(intent);
                }
            });
    }
    /**
     * khoi tao search
     */
 /*   @Override
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

//                final List<Song>filterModel = filter(songList, searchQuery);
//                songAdapter.getFilte(filterModel);
//
//                final List<Album>filterModelAlbum = filterAlbum(albumList, searchQuery);
//                albumAdapter.getFilte(filterModelAlbum);

//                final List<Artist>filterModelArtist = filterArtist(artistList, searchQuery);
//                artistAdapter.getFilte(filterModelArtist);

                return true;
            }
        });
        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_view, menu);
        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.menu_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        //searchAutoComplete.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        // Create a new ArrayAdapter and add data to search auto complete object.
//        String dataArr[] = {"Apple", "Amazon", "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
//        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
//        searchAutoComplete.setAdapter(newsAdapter);
        //  searchAutoComplete.setAdapter(songAdapter);

        songSearchAdapter = new SongSearchAdapter(MainActivity.this,SongFragment.songList);
      //  Log.d("songList",SongFragment.songList.size()+"");
        searchAutoComplete.setAdapter(songSearchAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
//                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
//                searchAutoComplete.setText("" + queryString);
            }
        });
        return super.onCreateOptionsMenu(menu);
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
       // startService(intent);
        bindService(intent, mSCon, BIND_AUTO_CREATE);
    }

    private ServiceConnection mSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
           // mMusicService.setListener(MainActivity.this);
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
        unregisterReceiver(dataSongFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgButtonPlay:
                if (mMusicService.isPlay()) {
                    mMusicService.pauseSong();
                } else {
                    mMusicService.continuesSong();
                }
                imgBottomPlay.setImageResource(R.drawable.ic_stop);
                break;
        }
    }

}

