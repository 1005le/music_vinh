package com.example.music_vinh.view.impl;


import android.content.ContentResolver;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.example.music_vinh.R;
import com.example.music_vinh.adapter.MainViewAdapter;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    Toolbar toolbarMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initTab();
        act();
    }

    private void act() {
        setSupportActionBar(toolbarMainActivity);
        getSupportActionBar().setTitle(getString(R.string.beauty));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void init() {
        tabLayout = findViewById(R.id.myTabLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbarMainActivity = findViewById(R.id.toolBarMainActivity);
        viewPager = findViewById(R.id.myViewPager);

    }

    /**
     * khoi tao search
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
              //  Log.d("AAB",query);
               // searchTuKhoaTuDien(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}
