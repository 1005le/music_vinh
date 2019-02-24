package com.example.music_vinh.view.impl;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongAdapter;

import com.example.music_vinh.injection.AppComponent;

import com.example.music_vinh.injection.DaggerPlaySongViewComponent;
import com.example.music_vinh.injection.PlaySongViewModule;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.PlaySongPresenter;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.view.PlaySongView;
import com.example.music_vinh.view.custom.CircularSeekBar;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.StorageUtil;
import com.example.music_vinh.view.search.SearchableActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

import static com.example.music_vinh.view.impl.SongFragment.songList;

public class PlayActivity extends BaseActivity implements PlaySongView, ServiceCallback, View.OnClickListener {

    @BindView(R.id.toolbarPlaySong)
    Toolbar toolbarPlaySong;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.imgPrev)
    ImageView imgPre;
    @BindView(R.id.imgNext)
    ImageView imgNext;
    @BindView(R.id.imgShuttle)
    ImageView imgShuttle;
    @BindView(R.id.imgRepeat)
    ImageView imgRepeat;
    @BindView(R.id.imgRepeatOne)
    ImageView imgRepeatOne;
    @BindView(R.id.imgPlay)
    ImageView imgPlay;
    @BindView(R.id.recycleViewPlaySong)
    RecyclerView playSongRecycleview;

    @BindView(R.id.tvNameArtistPlay)
    TextView tvNameArtistPlay;
    private int mProgess;

  //  public int audioIndex =-1;
    public int audioIndex ;
  //  public static ArrayList<Song> arrSong = new ArrayList<Song>();
    public static ArrayList<Song> arrSong;
    SongAdapter songAdapter;
    public static Song song;
    MediaPlayer mediaPlayer;

    private boolean mIsShuffle;
    private boolean mIsRepeat;

    CircularSeekBar circularSeekBar;
    private MusicService mMusicService;
   // boolean serviceBound = false;
   private boolean mTrackingSeekBar = false;
    private ServiceConnection mSCon;
    private int mCurentSong;
    private SimpleDateFormat mDateFormat;
    private boolean mIsBound;
    int current, totalTime;

    @Inject
    PlaySongPresenter playSongPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initPresenter();
        getDataIntent();
       // getDataSong();
        ButterKnife.bind(this);
        circularSeekBar= (CircularSeekBar) findViewById(R.id.circularSeekBar1);
        mIsBound = false;
        mDateFormat = new SimpleDateFormat(getString(R.string.date_time));
        connectService();
        doStuff();
        act();
        registerListener();

//        register_DataSongFragment();
//        register_currentTimeAudio();
//        register_durationAudio();
//        loadAudioInfo();
      //  eventClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void registerListener() {
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
              //  mMusicService.seekTo(seekBar.getProgress() *1000);
               mMusicService.seekTo(seekBar.getProgress());
            }
            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
            }
        });
        imgPre.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        imgRepeat.setOnClickListener(this);
        imgShuttle.setOnClickListener(this);
        imgNext.setOnClickListener(this);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerPlaySongViewComponent.builder()
                .appComponent(appComponent)
                .playSongViewModule(new PlaySongViewModule(this))
                .build()
                .inject(this);
    }

    private void connectService() {
        mSCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
                mMusicService.setListener(PlayActivity.this);
                mIsBound = true;

              //  getDataIntent();
                mMusicService.setSongs(arrSong);
                mMusicService.setCurrentSong(mCurentSong);
                Log.d("arrAlbum",arrSong.get(mCurentSong).getName());

                if (mProgess > 0) {
                    Log.d("mProgress",mProgess+"");
                   // mMusicService.seekTo(mProgess);
                    mMusicService.seekTo((int)mMusicService.getCurrentPosition());
                } else {
                     mMusicService.playSong();
                }

//                if (current > 0) {
//                    mMusicService.seekTo(current);
//                } else {
//                    mMusicService.playSong();
//                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mSCon = null;
            }
        };
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(Constants.ACTION_BIND_SERVICE);
        startService(intent);
        bindService(intent, mSCon, BIND_AUTO_CREATE);
    }
    private void act() {
        setSupportActionBar(toolbarPlaySong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setTitle(song.getName());
        toolbarPlaySong.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void init() {
        act();
        getDataSong();
    }

    private void getDataSong() {
       // circularSeekBar.setProgress(mediaPlayer.getCurrentPosition());
        // tvNameArtistPlay.setText(song.getNameArtist());
      //  tvNameArtistPlay.setText(song.getNameArtist());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//        tvTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
          Intent intent = getIntent();
//         mCurentSong= intent.getIntExtra("song", 0);
//         arrSong = intent.getParcelableArrayListExtra("arrSong");
        if (intent != null) {
            //truyen 1 ca khuc duy nhat
            if (intent.hasExtra("song")) {
                mCurentSong= intent.getIntExtra("song", 0);
            }
            if (intent.hasExtra("arrSong")) {
                arrSong = intent.getParcelableArrayListExtra("arrSong");
            }
            Log.d("get",arrSong.get(mCurentSong).getName());
        }
       // songArrayList = intent.getParcelableArrayListExtra("listSong");
    }

    private void initPresenter(){
       playSongPresenter = new PlaySongPresenterImpl(this);
    }

    private void doStuff() {
         arrSong = new ArrayList<>();
         getDataIntent();
        //  getDataSong();
         playSongPresenter.loadData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSCon != null){
            unbindService(mSCon);
        }
    }

   public void getDataIntent() {
        Intent intent = getIntent();

       StorageUtil storage = new StorageUtil(getApplicationContext());
       arrSong= storage.loadAudio();
       mCurentSong = storage.loadAudioIndex();
       Log.d("progresssStore",mProgess+"");
       if(intent.hasExtra(Constants.KEY_PROGESS)) {
          // intent.putExtra("duration", totalTime);
//           int total = intent.getIntExtra("duration", 0);
//           Log.d("nhanTotal",total+"");
//           circularSeekBar.setMax(total);
           Log.d("progresssIntent",mProgess+"");
         mProgess= intent.getIntExtra(Constants.KEY_PROGESS, 0);
       }

      /* Log.d("intent", intent+"");
       if(intent !=null){
           if(intent.hasExtra("current")) {
           //intent.putExtra("current", currentPosition);
           int current = intent.getIntExtra("current", 0);
           mProgess = intent.getIntExtra("current", 0);
           Log.d("nhanCurr", current + "");
           // circularSeekBar.setProgress(current);
           //Log.d("musicCurrent1", mMusicService.getCurrentPosition() + "");
          // mMusicService.seekTo((int) mMusicService.getCurrentPosition());
         //  Log.d("musicCurrent2", mMusicService.getCurrentPosition() + "");   */
//           if (mProgess > 0) {
//               mMusicService.seekTo(mProgess);
//           } else {
//               mMusicService.playSong();
//           }
     //  }
      // }

  /*     if (intent != null) {
            Bundle bundle = intent.getBundleExtra(Constants.KEY_BUNDLE);
            arrSong = bundle.getParcelableArrayList(Constants.KEY_SONGS);
            mCurentSong = bundle.getInt(Constants.KEY_POSITION, 0);
             Log.d("albumPlay", arrSong.get(mCurentSong).getName());
//            mMusicService.setSongs(arrSong);
//            mMusicService.setCurrentSong(mCurentSong);      */
           // mProgess = bundle.getInt(Constants.KEY_PROGESS, 0);
             // mProgess = bundle.getInt(Constants.KEY_PROGESS, 0);

      //  }     */
    }

    private void loadAudioInfo() {
        Intent loadAudioIntent = new Intent(Constants.LOAD_AUDIO);
        LocalBroadcastManager.getInstance(PlayActivity.this).sendBroadcast(loadAudioIntent);
    }

    private BroadcastReceiver dataSongFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            StorageUtil storage = new StorageUtil(getApplicationContext());
            arrSong= storage.loadAudio();
            mCurentSong = storage.loadAudioIndex();

            getSupportActionBar().setTitle(arrSong.get(mCurentSong).getName());
            tvNameArtistPlay.setText(arrSong.get(mCurentSong).getNameArtist());
        }
    };

    private void register_DataSongFragment() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND);
        LocalBroadcastManager.getInstance(PlayActivity.this).registerReceiver(dataSongFragment, filter);
    }

    private BroadcastReceiver durationAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          totalTime = intent.getIntExtra(Constants.DURATION,0);
            circularSeekBar.setMax((int) totalTime );
        }
    };

    private void register_durationAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Constants.SEND_DURATION);
        LocalBroadcastManager.getInstance(PlayActivity.this).registerReceiver(durationAudio, filter);
    }

    private BroadcastReceiver currentTimeAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            current = intent.getIntExtra(Constants.CURRENT_TIME, 0);

            Log.d("nhancurent", current+"");
            circularSeekBar.setProgress(current);
            tvTime.setText(mDateFormat.format(current *1000));
        }
    };
    public void register_currentTimeAudio() {
        LocalBroadcastManager.getInstance(PlayActivity.this).registerReceiver(
                currentTimeAudio, new IntentFilter(Constants.SEND_CURRENT));
    }

    @Override
    public void postName(String songName, String author) {
        getSupportActionBar().setTitle(songName);
        Log.d("album",songName);
        tvNameArtistPlay.setText(author);
    }
    @Override
    public void postTotalTime(long totalTime)
    {
       circularSeekBar.setMax((int) totalTime);
    }
    //
    @Override
    public void postCurentTime(long currentTime) {
        tvTime.setText(mDateFormat.format(currentTime));
        if(!mTrackingSeekBar) {
            circularSeekBar.setProgress((int) currentTime);
        }
    }

    @Override
    public void postPauseButon() {
        imgPlay.setImageResource(R.drawable.ic_stop_seekbar);
    }

    @Override
    public void postStartButton() {
        imgPlay.setImageResource(R.drawable.ic_play_seekbar);
    }

    @Override
    public void postShuffle(boolean isShuffle) {
        mIsShuffle = isShuffle;
        if (mIsShuffle) {
            imgShuttle.setImageResource(R.drawable.ic_shuttled);
        } else {
            imgShuttle.setImageResource(R.drawable.ic_shuttle);
        }
    }

    @Override
    public void postLoop(boolean isLoop) {
        mIsRepeat = isLoop;
        if (mIsRepeat) {
            imgRepeat.setImageResource(R.drawable.ic_repeat_one);
        } else {
            imgRepeat.setImageResource(R.drawable.ic_repeat);
        }
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void postAvatar(String url) {

    }

    @Override
    public void onClick(View v) {
        if (mIsBound) {
            switch (v.getId()) {
                case R.id.imgRepeat:
                    mMusicService.changeLoop();
                    break;
                case R.id.imgPrev:
                    mMusicService.previous();
                    break;
                case R.id.imgPlay:
                    if (mMusicService.isPlay()) {
                        mMusicService.pauseSong();
                    } else {
                        mMusicService.continuesSong();
                    }
                    break;
                case R.id.imgNext:
                    mMusicService.next();
                    break;
                case R.id.imgShuttle:
                    mMusicService.changeShuffle();
                    break;
            }
        }
    }

    public ArrayList<Song> getMusicSong() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.d("uri",songUri+"");
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                String currentId = songCursor.getString(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentAlbum = songCursor.getString(songAlbum);
                String currentPath = songCursor.getString(songPath);
                String currentDuration = songCursor.getString(songDuration);

                arrSong.add(new Song(Long.parseLong(currentId),currentTitle, currentArtist,currentAlbum,currentPath,Long.parseLong(currentDuration)));
            } while (songCursor.moveToNext());
        }
        return arrSong;
    }


 /*   @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.play_view, menu);

       final MenuItem myActionMenuItem = menu.findItem( R.id.menu_search_sort);
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
                return true;
            }
        });
        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.play_view, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_sort);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchableActivity.class)));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            Toast.makeText(this, "Suggestion: " + uri, Toast.LENGTH_SHORT).show();
        }
    }
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PlayActivity.class);
        return intent;
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


    @Override
    public void showSong(ArrayList<Song> songs) {

        songAdapter = new SongAdapter(PlayActivity.this, songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        playSongRecycleview.setLayoutManager(linearLayoutManager);
        playSongRecycleview.setAdapter(songAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_setting_sort:
                Collections.sort(arrSong, new Comparator<Song>() {
                    @Override
                    public int compare(Song song, Song t1) {
                        return (song.getName().compareTo(t1.getName()));
                    }
                });
              Intent intent =  new Intent(PlayActivity.this, SortActivity.class);
               intent.putExtra("listSong",arrSong);
              startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
