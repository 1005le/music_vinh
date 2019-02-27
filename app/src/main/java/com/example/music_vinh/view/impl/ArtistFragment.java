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
import com.example.music_vinh.adapter.ArtistAdapter;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.presenter.ArtistPresenter;
import com.example.music_vinh.presenter.impl.ArtistPresenterImpl;
import com.example.music_vinh.utils.Constants;
import com.example.music_vinh.utils.CustomTouchListener;
import com.example.music_vinh.utils.onItemClickListener;
import com.example.music_vinh.view.ArtistView;

import java.util.ArrayList;
import java.util.List;

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
    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    GridLayoutManager gridLayoutManager;
      @Inject
     ArtistAdapter artistAdapter;

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
        onLoadArtistList();
        setHasOptionsMenu(true);
        evenClick();
    }

    private void evenClick() {
        artistAdapter.setOnArtistItemClickListener(new ArtistAdapter.OnArtistItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(getContext(), ArtistInfoActivity.class);
                intent.putExtra("artist",artistList.get(position));
                getContext().startActivity(intent);
            }
        });
    }

    private void initPresenter(){
        artistPresenter = new ArtistPresenterImpl(this);

    }
    @Override
    public void showArtist(List<Artist> artists) {
        artistAdapter = new ArtistAdapter(getActivity(),artists);
        artistAdapter.setType(Constants.VIEW_GRID);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        artistRecyclerView.setLayoutManager(gridLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);
    }

    private void onLoadArtistList() {
//        artistList = new ArrayList<>();
//        artistList = getMusicArtist();
        artistPresenter.loadArtist(getContext());
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
        artistAdapter.setType(Constants.VIEW_LIST);
        artistAdapter = new ArtistAdapter(getActivity(),artistList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        artistRecyclerView.setLayoutManager(linearLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);
    }

    private void disPlayViewGrid() {
        artistAdapter = new ArtistAdapter(getActivity(),artistList);
        artistAdapter.setType(Constants.VIEW_GRID);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        artistRecyclerView.setLayoutManager(gridLayoutManager);
        artistRecyclerView.setAdapter(artistAdapter);
    }

}
