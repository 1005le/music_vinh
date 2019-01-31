package com.example.music_vinh.view.impl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.music_vinh.R;

public class SortActivity extends AppCompatActivity {

    Toolbar toolbarSort;
    RecyclerView sortSongRecycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
    }



}
