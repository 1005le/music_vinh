package com.example.music_vinh.view.impl;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.AlbumAdapter;
import com.example.music_vinh.adapter.SongAdapter;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.presenter.AlbumPresenter;
import com.example.music_vinh.presenter.impl.AlbumPresenterImpl;
import com.example.music_vinh.utils.Constants;
import com.example.music_vinh.view.AlbumView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment implements AlbumView {
     View view;
    @BindView(R.id.recycleViewAlbum)
     RecyclerView albumRecyclerView;
      @Inject
      AlbumPresenter albumPresenter;
       @Inject
     AlbumAdapter albumAdapter;
       @Inject
    LinearLayoutManager linearLayoutManager;
       @Inject
    GridLayoutManager gridLayoutManager;

    public ArrayList<Album> albumList;

    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_album, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        initRecycleView();
        initPresenter();
        onLoadAlbumList();
        setHasOptionsMenu(true);
        eventClick();

    }

    private void initRecycleView() {
        albumList = new ArrayList<>();
        albumAdapter = new AlbumAdapter(getActivity(),albumList);
        albumAdapter.setType(Constants.VIEW_GRID);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        albumRecyclerView.setLayoutManager(gridLayoutManager);
        albumRecyclerView.setAdapter(albumAdapter);
    }

    private void eventClick() {
        albumAdapter.setOnAlbumItemClickListener(new AlbumAdapter.OnAlbumItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                albumPresenter.onCallDataIntent(position);
            }
        });
    }
    private void initPresenter(){
       albumPresenter = new AlbumPresenterImpl(this);
    }
    @Override
    public void showAlbum(List<Album> albums) {
        albumAdapter.addData(albums);
        albumList.addAll(albums);
    }

    @Override
    public void intentAlbumForDetail(List<Album> albums, int position) {
        Intent intent = new Intent(getContext(), AlbumInfoActivity.class);
        intent.putExtra("album",albumList.get(position));
        getContext().startActivity(intent);
    }

    private void onLoadAlbumList() {
        albumPresenter.getAlbum(getContext());
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
        albumAdapter.setType(Constants.VIEW_LIST);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        albumRecyclerView.setLayoutManager(linearLayoutManager);
        albumRecyclerView.setAdapter(albumAdapter);
    }

    private void disPlayViewGrid() {
        albumAdapter.setType(Constants.VIEW_GRID);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        albumRecyclerView.setLayoutManager(gridLayoutManager);
        albumRecyclerView.setAdapter(albumAdapter);
    }
}
