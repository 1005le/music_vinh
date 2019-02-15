package com.example.music_vinh.view.impl;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
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
import com.example.music_vinh.view.MainView;
import com.example.music_vinh.view.custom.CustomTouchListener;
import com.example.music_vinh.view.custom.onItemClickListener;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends Fragment implements MainView {
    View view;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.music_vinh.view.impl.PlayNewAudio";

    private MediaPlayerService player;
    boolean serviceBound = false;

    @BindView(R.id.recycleViewSong)
    RecyclerView songRecyclerView;

   public static SongAdapter songAdapter;

   public static ArrayList<Song> songList;

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
            public void onClick(View view, int index) {
                playAudio(index);

                String actionName = "SongFragment";
                Intent intent = new Intent(actionName);
           //Thiết lập tên để cho Receiver nhận được thì biết đó là loại Intent
                intent.setAction(actionName);
            //Dữ liệu gắn vào Intent thiết lập bằng putExtra với (tên, dữ liệu), dữ liệu là
             //các kiểu cơ bản Int, String ... hoặc các loại đối tượng lớp kế thừa từ Serializable
                intent.putExtra("song",songList.get(index));
             //Thực hiện lan truyền Intent trong hệ thống
                getContext().sendBroadcast(intent);

                Intent intent1 = new Intent(getContext() , PlayActivity.class);
                intent1.putExtra("song", songList.get(index));
                intent1.putExtra("arrSong",(ArrayList) songList);
                getContext().startActivity(intent);
            }
        }));
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
            StorageUtil storage = new StorageUtil(getContext());
            storage.storeAudio(songList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(getContext(), MediaPlayerService.class);
            getContext().startService(playerIntent);
            getContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getContext());
           // storage.storeAudio(songList);
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            getContext().sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
           getContext().unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
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

}
