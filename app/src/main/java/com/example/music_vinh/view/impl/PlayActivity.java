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

   // ArrayList<Song> songList;
    private PlaySongPresenterImpl playSongPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initPresenter();

        getDataIntent();
        init();
        act();
       //  getDataSong();
        doStuff();
    }

    private void playSong() {
        long id = song.getId();
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
      //  getMusicSong();
      //  playSongPresenter.onLoadSongSuccess(arrSong);
        playSongPresenter.onLoadSongSuccess(SongFragment.songList);
    }

    private void getDataSong() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//        tvTime.setText(simpleDateFormat.format(arrSong.get(0).getDuration()));
//        tvNameArtistPlay.setText(arrSong.get(0).getNameArtist());


    }

    private void getDataIntent() {
        Intent intent = getIntent();
      //  arrSong.clear();
        if (intent != null) {
            if (intent.hasExtra("song")) {
                song = intent.getParcelableExtra("song");

                tvTime = findViewById(R.id.tvTime);
                tvNameArtistPlay = findViewById(R.id.tvNameArtistPlay);
              //  getSupportActionBar().setTitle(song.getName());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                tvTime.setText(simpleDateFormat.format(song.getDuration()));
                tvNameArtistPlay.setText(song.getNameArtist());
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

        toolbarPlaySong.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        playSong();
    }

    @Override
    public void showSong(ArrayList<Song> songs) {

        songAdapter = new SongAdapter(PlayActivity.this, songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        playSongRecycleview.setLayoutManager(linearLayoutManager);
        playSongRecycleview.setAdapter(songAdapter);
    }

}
