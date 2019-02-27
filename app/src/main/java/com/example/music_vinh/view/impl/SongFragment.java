package com.example.music_vinh.view.impl;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;


import com.example.music_vinh.R;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.presenter.MainPresenter;
import com.example.music_vinh.presenter.impl.MainPresenterImpl;
import com.example.music_vinh.service.MusicService;
import com.example.music_vinh.service.ServiceCallback;
import com.example.music_vinh.utils.Constants;
import com.example.music_vinh.utils.CustomTouchListener;
import com.example.music_vinh.utils.StorageUtil;
import com.example.music_vinh.utils.onItemClickListener;
import com.example.music_vinh.view.MainView;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.music_vinh.view.impl.PlayActivity.arrSong;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends Fragment implements MainView {
    View view;
    boolean serviceBound = false;

    @BindView(R.id.recycleViewSong)
    RecyclerView songRecyclerView;
    @Inject
   SongAdapter songAdapter;
    //Presenter
    @Inject
    MainPresenter mainPresenter;
    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    GridLayoutManager gridLayoutManager;

    public static ArrayList<Song> songList;
    private MusicService mMusicService;
    private ServiceConnection mSCon;
    private int mCurentSong;
    private boolean mIsBound;

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
        onLoadSongList();
        setHasOptionsMenu(true);
        evenClick();
    }
    private void evenClick() {
        songAdapter.setOnSongItemClickListener(new SongAdapter.OnSongItemClickListener() {
            @Override
            public void onSongItemClicked(View view, int position) {
                StorageUtil storage = new StorageUtil(getContext());
                storage.storeAudio(songList);
                storage.storeAudioIndex(position);
                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), PlayActivity.class);
//                intent.putExtra(Constants.PLAY_TYPE, Constants.PLAY);
//                startActivity(intent);
            }
        });
    }

   private void initPresenter(){
        mainPresenter = new MainPresenterImpl(this);
     }

    @Override
    public void showSong(List<Song> songs) {
        songAdapter = new SongAdapter(getActivity(),songs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        songRecyclerView.setLayoutManager(linearLayoutManager);
        songRecyclerView.setAdapter(songAdapter);
    }
    private void onLoadSongList() {
        mainPresenter.loadData(getContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_view:
                disPlayViewList();
                return true;
            case R.id.grid_view:
                disPlayViewGrid();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void disPlayViewList() {
        songAdapter.setType(Constants.VIEW_LIST);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        songRecyclerView.setLayoutManager(linearLayoutManager);
        songRecyclerView.setAdapter(songAdapter);
    }

    private void disPlayViewGrid() {
        songAdapter.setType(Constants.VIEW_GRID);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        songRecyclerView.setLayoutManager(gridLayoutManager);
        songRecyclerView.setAdapter(songAdapter);
    }
}
