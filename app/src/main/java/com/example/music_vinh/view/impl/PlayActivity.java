package com.example.music_vinh.view.impl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongAdapter;

import com.example.music_vinh.injection.AppComponent;

import com.example.music_vinh.injection.DaggerPlaySongViewComponent;
import com.example.music_vinh.injection.PlaySongViewModule;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.PlaySongPresenter;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.view.PlaySongView;
import com.example.music_vinh.view.custom.CircularSeekBar;

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

import static com.example.music_vinh.view.impl.AlbumFragment.albumAdapter;
import static com.example.music_vinh.view.impl.AlbumFragment.albumList;
import static com.example.music_vinh.view.impl.ArtistFragment.artistAdapter;
import static com.example.music_vinh.view.impl.ArtistFragment.artistList;
import static com.example.music_vinh.view.impl.SongFragment.songAdapter;
import static com.example.music_vinh.view.impl.SongFragment.songList;

public class PlayActivity extends BaseActivity implements PlaySongView {

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

//    @BindView(R.id.seekBarSong)
//    SeekBar seekBar;
    @BindView(R.id.recycleViewPlaySong)
    RecyclerView playSongRecycleview;

    @BindView(R.id.tvNameArtistPlay)
    TextView tvNameArtistPlay;

    public static ArrayList<Song> arrSong = new ArrayList<Song>();
    SongAdapter songAdapter;

    public static Song song;
    MediaPlayer mediaPlayer;

    int position = 0;//xac dinh vị tri de nhan next, preview
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next = false;
    Random random;

    CircularSeekBar circularSeekBar;

    private MediaPlayerService player;
    boolean serviceBound = false;

    @Inject
    PlaySongPresenter playSongPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initPresenter();

        getDataIntent();

        ButterKnife.bind(this);
        init();
      //  act();
        doStuff();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerPlaySongViewComponent.builder()
                .appComponent(appComponent)
                .playSongViewModule(new PlaySongViewModule(this))
                .build()
                .inject(this);
    }

    private void playSong(long id) {
        Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), contentUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        UpdateTime();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //  super.onRestoreInstanceState(savedInstanceState);
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
            storage.storeAudio(songList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(getApplicationContext(), MediaPlayerService.class);
            getApplicationContext().startService(playerIntent);
            getApplicationContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
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

    private void initPresenter(){
       playSongPresenter = new PlaySongPresenterImpl(this);
    }
    private void doStuff() {
        arrSong = new ArrayList<>();
        // arrSong = getMusicSong();
         getDataIntent();
         playSongPresenter.loadData();
    }

    private void getDataSong() {
         //  seekBar.setProgress(mediaPlayer.getCurrentPosition());
           circularSeekBar.setProgress(mediaPlayer.getCurrentPosition());
        tvNameArtistPlay.setText(song.getNameArtist());
    }

    private void getDataIntent() {
        Intent intent = getIntent();
      //  arrSong.clear();
        if (intent != null) {
            if (intent.hasExtra("song")) {
                song = intent.getParcelableExtra("song");
                arrSong.add(song);
            }
            if (intent.hasExtra("arrSong")) {
                arrSong = intent.getParcelableArrayListExtra("arrSong");
            }
            if (intent.hasExtra("dragSong")) {
                arrSong = intent.getParcelableArrayListExtra("dragSong");
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

    private void act() {
        setSupportActionBar(toolbarPlaySong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(song.getName());
        toolbarPlaySong.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                arrSong.clear();
            }
        });
    }

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

    private void init() {
        playSong(song.getId());
        act();
        circularSeekBar= (CircularSeekBar) findViewById(R.id.circularSeekBar1);

        getDataSong();
        eventClick();
    }

    @Override
    public void showSong(ArrayList<Song> songs) {

        songAdapter = new SongAdapter(PlayActivity.this, songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        playSongRecycleview.setLayoutManager(linearLayoutManager);
        playSongRecycleview.setAdapter(songAdapter);
    }

    private void eventClick() {
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
     //         seekBar.setMax(mediaPlayer.getDuration());
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//            //khi keo chuot den vi tri nao thi bai nghe se duoc phat tu vi tri do tro di
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.seekTo(seekBar.getProgress());
//            }
//        });
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                circularSeekBar.setMax(mediaPlayer.getDuration());
            }
            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                mediaPlayer.seekTo(circularSeekBar.getProgress());
            }
            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.ic_stop_seekbar);
                }else{
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.drawable.ic_play_seekbar);
                }
            }
        });

        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( repeat == false){
                    if(checkrandom == true){
                        checkrandom = false;
                        imgRepeat.setImageResource(R.drawable.ic_repeat_one);
                        imgShuttle.setImageResource(R.drawable.ic_shuttle);
                    }
                    imgRepeat.setImageResource(R.drawable.ic_repeat_one);
                    repeat = true;
                }else{
                    imgRepeat.setImageResource(R.drawable.ic_repeat);
                    repeat= false;
                }
            }
        });

        imgShuttle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkrandom == false){
                    if(repeat == true){
                        repeat = false;
                        imgShuttle.setImageResource(R.drawable.ic_shuttled);
                        imgRepeat.setImageResource(R.drawable.ic_repeat);
                    }
                    imgShuttle.setImageResource(R.drawable.ic_shuttled);
                    checkrandom = true;
                }else{
                    imgShuttle.setImageResource(R.drawable.ic_shuttle);
                    checkrandom = false;
                }
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( arrSong.size() > 0){
                    //nếu có ca khúc đang phát
                    if(mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        //dong bo lại
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    //gia tri phai nho hon kich thuoc cua mang thi cho next
                    if(position < (arrSong.size())){

                        position++;
                        if(repeat == true){
                            if(position == 0){
                               // position= SongFragment.songList.size();
                                position= arrSong.size();
                            }
                            position -=1;
                        }
                        if(checkrandom == true){
                             random = new Random();
                            int index = random.nextInt(arrSong.size());
                            if( index == position){
                                position = index -1;
                            }
                            position = index;
                        }
                        if( position > (arrSong.size()-1)){
                            position = 0;
                        }
                         playSong(arrSong.get(position).getId());
                        tvNameArtistPlay.setText(arrSong.get(position).getNameArtist());
                        getSupportActionBar().setTitle(arrSong.get(position).getName());
                        UpdateTime();
                    }
                }
                imgPre.setClickable(false);
                imgNext.setClickable(false);
                //delay mot khoang thoi gian
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgPre.setClickable(true);
                        imgNext.setClickable(true);
                    }
                },5000);
            }
        });
        imgPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( arrSong.size() > 0){
                    if(mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        //dong bo lại
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    //gia tri phai nho hon kich thuoc cua mang thi cho next
                    if(position < (arrSong.size())){

                        position--;
                        if(position < 0){
                            position = arrSong.size() -1;
                        }
                        if(repeat == true){
                            position +=1;
                        }
                        if(checkrandom == true){
                            random = new Random();
                            int index = random.nextInt(arrSong.size());
                            if( index == position){
                                position = index -1;
                            }
                            position = index;
                        }
                        playSong(arrSong.get(position).getId());
                        tvNameArtistPlay.setText(arrSong.get(position).getNameArtist());
                        getSupportActionBar().setTitle(arrSong.get(position).getName());
                        UpdateTime();
                    }
                }
                imgPre.setClickable(false);
                imgNext.setClickable(false);
                //delay mot khoang thoi gian
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgPre.setClickable(true);
                        imgNext.setClickable(true);
                    }
                },1000);
            }
        });

    }

    private void UpdateTime(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if( mediaPlayer != null){
                  //  seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    circularSeekBar.setProgress(mediaPlayer.getCurrentPosition());

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    tvTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    //Thoi gian duoc cap nhat lien tuc
                    handler.postDelayed(this,300);
                    //lăng nghe khi media da chay xong hoan tat
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next = true;
                            try {
                                Thread.sleep(1000);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        },300);
        //lang nghe khi chuyen bai hat thi se lam cai gi
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(next == true){
                    if(position < (arrSong.size())){

                        position++;
                        if(repeat == true){
                            if(position == 0){
                                position= arrSong.size();
                            }
                            position -=1;
                        }
                        if(checkrandom == true){
                           random = new Random();
                            int index = random.nextInt(arrSong.size());
                            if( index == position){
                                position = index -1;
                            }
                            position = index;
                        }
                        if( position > (arrSong.size()-1)){
                            position = 0;
                        }
                        tvNameArtistPlay.setText(arrSong.get(position).getNameArtist());
                        playSong(arrSong.get(position).getId());
                        getSupportActionBar().setTitle(arrSong.get(position).getName());
                    }

                    imgPre.setClickable(false);
                    imgNext.setClickable(false);
                    //delay mot khoang thoi gian
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgPre.setClickable(true);
                            imgNext.setClickable(true);
                        }
                    },5000);
                    //khi chuyen duoc bai hat roi
                    next = false;
                    handler1.removeCallbacks(this);
                }else{
                    handler1.postDelayed(this, 1000);
                }
            }
        },1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_setting:
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
