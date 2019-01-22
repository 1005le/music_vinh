package com.example.music_vinh.view.impl;


import android.content.ContentResolver;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        getSupportActionBar().setTitle("Beauty Music");

    }
    //khai baso cac Tab
    private void initTab() {
        MainViewAdapter mainViewAdapter = new MainViewAdapter(getSupportFragmentManager());

        mainViewAdapter.addFragment(new SongFragment(),"SONGS");
        mainViewAdapter.addFragment(new AlbumFragment(),"ALBUMS");
        mainViewAdapter.addFragment(new ArtistFragment(), "ARTIST");
        // mainViewAdapter.addFragment(new);

        viewPager.setAdapter(mainViewAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void init() {
        tabLayout = findViewById(R.id.myTabLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbarMainActivity = findViewById(R.id.toolBarMainActivity);
        viewPager = findViewById(R.id.myViewPager);
    }
}
