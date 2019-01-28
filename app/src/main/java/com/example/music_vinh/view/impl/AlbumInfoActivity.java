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
import android.support.annotation.RequiresApi;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongInAlbumAdapter;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.impl.AlbumInfoPresenterImpl;
import com.example.music_vinh.presenter.impl.AlbumPresenterImpl;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.view.AlbumInfoView;

import java.util.ArrayList;

public class AlbumInfoActivity extends AppCompatActivity implements AlbumInfoView {

    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView listSongrecyclerView;
     Album album ;
    ArrayList<Song> songArrayList;
   SongInAlbumAdapter songInAlbumAdapter;

    private AlbumInfoPresenterImpl albumInfoPresenter;
    private static final int MY_PERMISSION_REQUEST = 1;

    ImageView imgAlbumInfo;
    TextView tvNameArtistInfoA;
    ImageView imgIconAlbum;
    TextView tvNameAlbumInfo;
    TextView tvamountSongA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_infor);
        init();
        getDataIntent();
        getData();
        act();

        //lay bai hat
        initPresenter();
        if (ContextCompat.checkSelfPermission(AlbumInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AlbumInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(AlbumInfoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(AlbumInfoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {

            doStuff();
        }

    }
    private void getDataIntent() {
        Intent intent = getIntent();
        album = (Album) intent.getSerializableExtra("albumArrayList");

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

    private void init() {
        coordinatorLayout = findViewById(R.id.coordinator);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        toolbar = findViewById(R.id.toolbarDanhSach);
        listSongrecyclerView = findViewById(R.id.recyclerViewListSong);

        imgAlbumInfo = findViewById(R.id.imgAlbumInfo);
        imgIconAlbum = findViewById(R.id.imgIconAlbum);
        tvNameAlbumInfo = findViewById(R.id.tvNameAlbumInfo);
        tvamountSongA = findViewById(R.id.tvamountSongA);

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
        getSongAlbum();
        albumInfoPresenter.onLoadSongSuccess(songArrayList);
    }
    public void getSongAlbum() {
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
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(AlbumInfoActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(AlbumInfoActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                } else {
                    Toast.makeText(AlbumInfoActivity.this, "No permission granted!", Toast.LENGTH_SHORT).show();
                    // finish();
                }
                return;

            }

        }

    }


}
