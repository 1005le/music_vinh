package com.example.music_vinh.view.impl;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.adapter.SongInAlbumAdapter;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.presenter.impl.PlaySongPresenterImpl;
import com.example.music_vinh.view.PlaySongView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements PlaySongView {

    Toolbar toolbarPlaySong;
    TextView tvTime;
    ImageView imgPre, imgNext, imgShuttle, imgRepeat,imgRepeatOne;
    SeekBar seekBar;
    RecyclerView playSongRecycleview;
   public static ArrayList<Song> arrSong = new ArrayList<Song>();
    SongAdapter songAdapter;
    TextView tvNameArtistPlay;
    public static Song song;
    MediaPlayer mediaPlayer;
    private static final int MY_PERMISSION_REQUEST = 1;

    int position = 0;//xac dinh vị tri de nhan next, preview
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next = false;

   // ArrayList<Song> songList;
    private PlaySongPresenterImpl playSongPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initPresenter();

        getDataIntent();
        init();
      //  act();
        doStuff();
    }

    private void playSong(long id) {
         // long id = song.getId();
        // android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
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

        seekBar.setThumb(new BitmapDrawable(BitmapFactory.decodeResource(
              getApplicationContext().getResources(), R.drawable.ic_play_seekbar)));
    }

    private void initPresenter(){
       playSongPresenter = new PlaySongPresenterImpl(this);
    }
    private void doStuff() {
     //  arrSong = new ArrayList<>();
       // getDataIntent();
           getMusicSong();
         playSongPresenter.onLoadSongSuccess(arrSong);
      //  playSongPresenter.onLoadSongSuccess(SongFragment.songList);
    }

    private void getDataSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        tvTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
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
        }
    }

    public void getMusicSong() {
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


    private void init() {
        toolbarPlaySong = findViewById(R.id.toolbarPlaySong);

        imgPre = findViewById(R.id.imgPrev);
        imgNext = findViewById(R.id.imgNext);
        imgShuttle= findViewById(R.id.imgShuttle);
        imgRepeat = findViewById(R.id.imgRepeat);
        imgRepeatOne = findViewById(R.id.imgRepeatOne);
        seekBar = findViewById(R.id.seekBarSong);
        playSongRecycleview = findViewById(R.id.recycleViewPlaySong);
        tvTime = findViewById(R.id.tvTime);
        tvNameArtistPlay = findViewById(R.id.tvNameArtistPlay);

        playSong(song.getId());
        act();
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
        //khi co ca khuc phat len, thi nut play thay doi trang thais

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            //khi keo chuot den vi tri nao thi bai nghe se duoc phat tu vi tri do tro di
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
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
                        seekBar.setThumb(new BitmapDrawable(BitmapFactory.decodeResource(
                                getApplicationContext().getResources(), R.drawable.ic_stop_seekbar)));
                        position++;
                        if(repeat == true){
                            if(position == 0){
                               // position= SongFragment.songList.size();
                                position= arrSong.size();
                            }
                            position -=1;
                        }
                        if(checkrandom == true){
                            Random random = new Random();
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
                        seekBar.setThumb(new BitmapDrawable(BitmapFactory.decodeResource(
                                getApplicationContext().getResources(), R.drawable.ic_stop_seekbar)));
                        position--;
                        if(position < 0){
                            position = arrSong.size() -1;
                        }
                        if(repeat == true){
                            position +=1;
                        }
                        if(checkrandom == true){
                            Random random = new Random();
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
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
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

                            } catch (InterruptedException e) {
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
                        seekBar.setThumb(new BitmapDrawable(BitmapFactory.decodeResource(
                                getApplicationContext().getResources(), R.drawable.ic_stop_seekbar)));
                        position++;
                        if(repeat == true){
                            if(position == 0){
                                position= arrSong.size();
                            }
                            position -=1;
                        }
                        if(checkrandom == true){
                            Random random = new Random();
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
}
