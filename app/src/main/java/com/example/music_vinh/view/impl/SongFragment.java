package com.example.music_vinh.view.impl;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.service.MediaPlayerService;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.view.MainView;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.CustomTouchListener;
import com.example.music_vinh.view.custom.StorageUtil;
import com.example.music_vinh.view.custom.onItemClickListener;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.music_vinh.view.impl.PlayActivity.arrSong;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends Fragment implements MainView, ServiceCallback {
    View view;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.music_vinh.view.service.PlayNewAudio";

    private MediaPlayerService player;
    boolean serviceBound = false;

    @BindView(R.id.recycleViewSong)
    RecyclerView songRecyclerView;

   public static SongAdapter songAdapter;

   public static ArrayList<Song> songList;
    private MusicService mMusicService;
    // boolean serviceBound = false;
    private ServiceConnection mSCon;
    private int mCurentSong;
    private boolean mIsBound;


    //Presenter
   @Inject
   MainPresenter mainPresenter;

    public SongFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_song, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        initPresenter();
        doStuff();
                songRecyclerView.addOnItemTouchListener(new CustomTouchListener(getContext(), new onItemClickListener() {
                    @Override
                    public void onClick(View view, final int index) {
                      //  playAudio(index);
                      //  connectService(index);

                        Intent intent = new Intent(getActivity(), PlayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constants.KEY_SONGS, songList);
                        bundle.putInt(Constants.KEY_POSITION, index);

                        intent.putExtra(Constants.KEY_BUNDLE, bundle);
                        startActivity(intent);

//                  Intent intent = new Intent(getContext(), PlayActivity.class);
//                  intent.putExtra("index", index);
//                  intent.putParcelableArrayListExtra("arrSong",songList);
//                  getContext().startActivity(intent);
                    }
                }));

    }

    private void connectService(final int index) {
        mSCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
                mMusicService.setListener(SongFragment.this);
                mIsBound = true;
//                getDataIntent();
                mMusicService.setSongs(songList);
                mMusicService.setCurrentSong(index);
                // mProgess = bundle.getInt(Constants.KEY_PROGESS, 0);
//                if (mProgess > 0) {
//                    mMusicService.seekTo(mProgess);
//                } else {
                mMusicService.playSong();
                //}
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent intent = new Intent(getContext(), MusicService.class);
        intent.setAction(Constants.ACTION_BIND_SERVICE);
        getContext().startService(intent);
        getContext().bindService(intent, mSCon, BIND_AUTO_CREATE);
    }


    private void initPresenter(){
        mainPresenter = new MainPresenterImpl(this);
     }

    @Override
    public void showSong(ArrayList<Song>songs) {
        songAdapter = new SongAdapter(getActivity(),songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        songRecyclerView.setLayoutManager(linearLayoutManager);
        songRecyclerView.setAdapter(songAdapter);
    }

    @Override
    public void showSongGrid(ArrayList<Song> songs) {
        songAdapter = new SongAdapter(getActivity(),songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        songRecyclerView.setLayoutManager(gridLayoutManager);
        songRecyclerView.setAdapter(songAdapter);
    }

    private void doStuff() {
        songList = new ArrayList<>();
        songList = getMusicSongArr();
        mainPresenter.loadData();
    }

    public ArrayList<Song> getMusicSongArr() {
        ContentResolver contentResolver = getActivity().getContentResolver();
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

                songList.add(new Song(Long.parseLong(currentId),currentTitle, currentArtist,currentAlbum,currentPath, Long.parseLong(currentDuration)));

            } while (songCursor.moveToNext());
        }
        return songList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.list_view:
                songList = getMusicSongArr();
                mainPresenter.loadData();
                return true;
            case R.id.grid_view:
                songList = getMusicSongArr();
                mainPresenter.loadDataGid();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void postName(String songName, String author) {

    }

    @Override
    public void postTotalTime(long totalTime) {

    }

    @Override
    public void postCurentTime(long currentTime) {

    }

    @Override
    public void postPauseButon() {

    }

    @Override
    public void postStartButton() {

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
