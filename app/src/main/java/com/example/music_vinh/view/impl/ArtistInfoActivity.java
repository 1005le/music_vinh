package com.example.music_vinh.view.impl;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongInAlbumAdapter;
import com.example.music_vinh.adapter.SongInArtistAdapter;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.presenter.impl.ArtistInfoPresenterImpl;
import com.example.music_vinh.view.ArtistInfoView;

import java.util.ArrayList;

public class ArtistInfoActivity extends AppCompatActivity implements ArtistInfoView {

    CoordinatorLayout coordinatorLayoutArtist;
    CollapsingToolbarLayout collapsingToolbarLayoutArtist;
    Toolbar toolbarArtist;
    RecyclerView songInArtistRecycleView;
    Artist artist;
    ArrayList<Song> songArrayList;
    SongInArtistAdapter songInArtistAdapter;

    ImageView imgViewArtist;

    private ArtistInfoPresenterImpl artistInfoPresenter;
    private static final int MY_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);

        init();
        act();
        getDataIntent();
        getData();

        initPresenter();
        if (ContextCompat.checkSelfPermission(ArtistInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ArtistInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(ArtistInfoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(ArtistInfoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {

            doStuff();
        }
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
        artist = (Artist) intent.getSerializableExtra("artistArrayList");

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

    private void init() {
        coordinatorLayoutArtist = findViewById(R.id.coordinatorArtist);
        collapsingToolbarLayoutArtist = findViewById(R.id.collapsingToolbarArtist);
        toolbarArtist = findViewById(R.id.toolbarArtistInfo);
        songInArtistRecycleView = findViewById(R.id.SongInArtistrecyclerView);
        imgViewArtist = findViewById(R.id.imgViewArtist);


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
        getSongArtist();

        artistInfoPresenter.onLoadSongSuccess(songArrayList);
    }

    public void getSongArtist() {
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
                    Log.wtf("SAME2SAME", String.valueOf(thisArtistId));
                    Log.wtf("SAME2SAME", String.valueOf(this.artist.getId()));

                    songArrayList.add(new Song(thisId, thisTitle, thisArtist,thisAlbumName,"",Long.parseLong(thisDuration)));
                }
            }
            while (mediaCursor.moveToNext());
            // For best practices, close the cursor after use.
            mediaCursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(ArtistInfoActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(ArtistInfoActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                } else {
                    Toast.makeText(ArtistInfoActivity.this, "No permission granted!", Toast.LENGTH_SHORT).show();
                    // finish();
                }
                return;
            }
        }
    }
}
