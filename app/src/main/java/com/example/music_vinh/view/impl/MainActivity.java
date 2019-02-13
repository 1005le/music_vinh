package com.example.music_vinh.view.impl;



import android.content.Intent;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.MainViewAdapter;
import com.example.music_vinh.injection.AppComponent;

import com.example.music_vinh.injection.DaggerMainViewComponent;
import com.example.music_vinh.injection.MainViewModule;
import com.example.music_vinh.model.Album;
import com.example.music_vinh.model.Artist;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.music_vinh.view.impl.AlbumFragment.albumAdapter;
import static com.example.music_vinh.view.impl.AlbumFragment.albumList;
import static com.example.music_vinh.view.impl.ArtistFragment.artistAdapter;
import static com.example.music_vinh.view.impl.ArtistFragment.artistList;
import static com.example.music_vinh.view.impl.SongFragment.songAdapter;
import static com.example.music_vinh.view.impl.SongFragment.songList;

public class  MainActivity extends BaseActivity {

    @BindView(R.id.myTabLayout)
    TabLayout tabLayout;
    @BindView(R.id.myViewPager)
    ViewPager viewPager;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolBarMainActivity)
    Toolbar toolbarMainActivity;
    @BindView(R.id.linearBottom)
    LinearLayout linearLayoutBottom;

    @BindView(R.id.tvNameSongBottom)
    TextView tvNameSong;
    @BindView(R.id.tvNameArtistBottom)
    TextView tvNameArtist;
    @BindView(R.id.imgButtonPause)
    ImageButton imgPause;

    MainView mainView;
    public Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTab();
        act();
       // getDataBottom();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerMainViewComponent.builder()
                .appComponent(appComponent)
                .mainViewModule(new MainViewModule(mainView))
                .build()
                .inject(this);
    }

    private void act() {
        setSupportActionBar(toolbarMainActivity);
        getSupportActionBar().setTitle(getString(R.string.beauty));
    }
    /**
     * Khai báo các Tab
     */
    private void initTab() {
        MainViewAdapter mainViewAdapter = new MainViewAdapter(getSupportFragmentManager());

        mainViewAdapter.addFragment(new SongFragment(),getString(R.string.song));
        mainViewAdapter.addFragment(new AlbumFragment(),getString(R.string.album));
        mainViewAdapter.addFragment(new ArtistFragment(), getString(R.string.artist));
        viewPager.setAdapter(mainViewAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void getDataBottom() {
       tvNameSong.setText(PlayActivity.song.getName());
        tvNameArtist.setText(PlayActivity.song.getNameArtist());
        imgPause.setImageResource(R.drawable.ic_pause);

//        if(song.getName().length() > 0 && song.getNameArtist().length() >0) {
//            linearLayoutBottom.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
//                    intent.putExtra("song", song);
//                    startActivity(intent);
//                }
//            });
//        }
    }

    /**
     * khoi tao search
     */
    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.search_view, menu);

     final MenuItem myActionMenuItem = menu.findItem( R.id.menu_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String searchQuery) {

                final List<Song>filterModel = filter(songList, searchQuery);
                songAdapter.getFilte(filterModel);

                final List<Album>filterModelAlbum = filterAlbum(albumList, searchQuery);
                albumAdapter.getFilte(filterModelAlbum);

                final List<Artist>filterModelArtist = filterArtist(artistList, searchQuery);
               artistAdapter.getFilte(filterModelArtist);

                return true;
            }
        });
        return true;
    }

    private List<Song> filter(List<Song>listItem, String query){
        query = query.toLowerCase();
        final List<Song>filterModel = new ArrayList<>();

        for( Song item :listItem){
            final String text = item.getName().toLowerCase();
            if( text.startsWith(query)){
                filterModel.add(item);
            }
        }
        return filterModel;
    }

    private List<Album> filterAlbum(List<Album>listItem, String query){
        query = query.toLowerCase();
        final List<Album>filterModel = new ArrayList<>();

        for( Album item :listItem){
            final String text = item.getName().toLowerCase();
            if( text.startsWith(query)){
                filterModel.add(item);
            }
        }
        return filterModel;
    }

    private List<Artist> filterArtist(List<Artist>listItem, String query){
        query = query.toLowerCase();
        final List<Artist>filterModel = new ArrayList<>();

        for( Artist item :listItem){
            final String text = item.getName().toLowerCase();
            if( text.startsWith(query)){
                filterModel.add(item);
            }
        }
        return filterModel;
    }

}
