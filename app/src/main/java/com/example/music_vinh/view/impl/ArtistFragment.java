package com.example.music_vinh.view.impl;


import android.Manifest;
import android.content.ContentResolver;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.AlbumAdapter;
import com.example.music_vinh.adapter.ArtistAdapter;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.presenter.impl.ArtistPresenterImpl;
import com.example.music_vinh.view.ArtistView;
import com.example.music_vinh.view.custom.Constants;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment implements ArtistView {

    View view;
    @BindView(R.id.recycleViewArtist)
    RecyclerView artistRecyclerView;

    @Inject
    ArtistPresenter artistPresenter;

  public static ArtistAdapter artistAdapter;
  public static ArrayList<Artist> artistList;
    private static final int MY_PERMISSION_REQUEST = 1;

    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        initPresenter();
        doStuff();
        setHasOptionsMenu(true);
    }

    private void initPresenter(){
        artistPresenter = new ArtistPresenterImpl(this);

    }
    @Override
    public void showArtist(ArrayList<Artist> artists) {
        artistAdapter = new ArtistAdapter(getActivity(),artists);
        artistAdapter.setViewType(Constants.VIEW_GRID);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
      //  gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        artistRecyclerView.setLayoutManager(gridLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);
    }

    private void doStuff() {
       artistList = new ArrayList<>();
        artistList = getMusicArtist();
        artistPresenter.loadArtist();
    }
    public ArrayList<Artist> getMusicArtist() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

            int idArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int amountAlbum = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int amountSong = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
           // int imgArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ALBUM_ART);
            do {
                String currentId = songCursor.getString(idArtist);
                String currentArtist = songCursor.getString(songArtist);
                String currentAmountAlbum = songCursor.getString(amountAlbum);
                String currentAmountSong = songCursor.getString(amountSong);

               //  String currentImgArtist = songCursor.getString(imgArtist);
                artistList.add(new Artist(Long.parseLong(currentId),currentArtist, Integer.parseInt(currentAmountAlbum),
                       Integer.parseInt(currentAmountSong),""));

            } while (songCursor.moveToNext());
        }
        return artistList;
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
        artistAdapter.setViewType(Constants.VIEW_LIST);
        artistAdapter = new ArtistAdapter(getActivity(),artistList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        artistRecyclerView.setLayoutManager(linearLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);
    }

    private void disPlayViewGrid() {
        artistAdapter = new ArtistAdapter(getActivity(),artistList);
        artistAdapter.setViewType(Constants.VIEW_GRID);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        artistRecyclerView.setLayoutManager(gridLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);
    }

}
