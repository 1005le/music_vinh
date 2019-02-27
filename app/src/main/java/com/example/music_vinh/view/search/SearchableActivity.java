package com.example.music_vinh.view.search;

import android.app.SearchManager;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.StorageUtil;
import com.example.music_vinh.view.impl.AlbumInfoActivity;
import com.example.music_vinh.view.impl.ArtistFragment;
import com.example.music_vinh.view.impl.ArtistInfoActivity;
import com.example.music_vinh.view.impl.PlayActivity;
import com.example.music_vinh.view.impl.SongFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    private static final String TAG = "SearchableActivity";
    private MyHandler mHandler;
    private TextView txt;
    List<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        txt = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            mHandler = new MyHandler(this);
            mHandler.startQuery(0, null, intent.getData(), null, null, null, null);
            Log.d("DATA", intent.getDataString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void prepareIntent(String contentType, String data) {
        Log.d(TAG, contentType + " | " + data);
        switch (contentType) {
            case "SONG":
                Log.d(TAG, "playIntent");
                storeAudioIndex(data);
                //Log.d("dataSong",data);
                Intent playIntent = PlayActivity.getStartIntent(this);
                playIntent.putExtra(Constants.PLAY_TYPE, Constants.PLAY);
                startActivity(playIntent);
                finish();
                break;
            case "ALBUM":
                Log.d(TAG, "startAlbumIntent");
                Intent albumDetailIntent = AlbumInfoActivity.getStartIntent(this);
                albumDetailIntent.putExtra("album_ID", data);
                startActivity(albumDetailIntent);
                finish();
                break;
            case "ARTIST":
                Log.d(TAG, "artistDetailIntent");
                Intent artistDetailIntent = ArtistInfoActivity.getStartIntent(this);
                artistDetailIntent.putExtra("artist_ID", data);
                startActivity(artistDetailIntent);
                finish();
                break;
        }
    }

    private void storeAudioIndex(String data) {
        StorageUtil storage = new StorageUtil(getApplicationContext());
        if( songList == storage.loadAudio() || songList == SongFragment.songList) {
            for (Song song : songList) {
                if (String.valueOf(song.getId()).equals(data)) {
                    storage.storeAudioIndex(songList.indexOf(song));
                }
            }
        }
    }
    public void updateText(String text) {
        txt.setText(text);
    }

    static class MyHandler extends AsyncQueryHandler {
        // avoid memory leak
        WeakReference<SearchableActivity> activity;

        public MyHandler(SearchableActivity searchableActivity) {
            super(searchableActivity.getContentResolver());
            activity = new WeakReference<>(searchableActivity);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            if (cursor == null || cursor.getCount() == 0) return;

            cursor.moveToFirst();

            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String text = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
            long data_id = cursor.getLong(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID));
            String contentType = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_CONTENT_TYPE));
            String data = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_URI_PATH_QUERY));

            cursor.close();

            if (activity.get() != null) {
                activity.get().prepareIntent(contentType, data);
            }
        }
    }
}
