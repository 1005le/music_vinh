package com.example.music_vinh.view.search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import com.example.music_vinh.R;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestionProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.music_vinh.view.search.searchsuggestion";

    private static final int TYPE_ALL_SUGGESTIONS = 1;
    private static final int TYPE_SINGLE_SUGGESTION = 2;
    private static final String TAG = "BeautyMusic";

    private UriMatcher mUriMatcher;
    private List<String> contents;
    private List<String> contentIds;
    private List<ContentType> contentTypes;

    @Override
    public boolean onCreate() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "/#", TYPE_SINGLE_SUGGESTION);
        mUriMatcher.addURI(AUTHORITY, "search_suggest_query/*", TYPE_ALL_SUGGESTIONS);

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        if (contents == null || contents.isEmpty()
                || contentTypes == null || contentTypes.isEmpty()
                || contentIds == null || contentIds.isEmpty()) {
            try {
                Log.d(TAG, "Prepare content");

                contents = new ArrayList<>();
                contentTypes = new ArrayList<>();
                contentIds = new ArrayList<>();

                loadContentSuggesstioṇ̣̣̣();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TAG", "Cache!");
        }

        MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_ICON_1,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_CONTENT_TYPE,
                        SearchManager.SUGGEST_URI_PATH_QUERY,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );

        if (mUriMatcher.match(uri) == TYPE_ALL_SUGGESTIONS) {
            if (contents != null) {
                String query = uri.getLastPathSegment().toUpperCase();
                int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

                int lenght = contents.size();
                for (int i = 0; i < lenght && cursor.getCount() < limit; i++) {
                    String data = contents.get(i);
                    if (data.toUpperCase().contains(query)) {
                        cursor.addRow(new Object[]{
                                i,
                                getSuggestionIcon(i),
                                data,
                                contentTypes.get(i),
                                contentIds.get(i),
                                i
                        });
                    }
                }
            }
        } else if (mUriMatcher.match(uri) == TYPE_SINGLE_SUGGESTION) {
            int position = Integer.parseInt(uri.getLastPathSegment());
            String data = contents.get(position);
            cursor.addRow(new Object[]{
                    position,
                    getSuggestionIcon(position),
                    data,
                    contentTypes.get(position),
                    contentIds.get(position),
                    position
            });
        }
        return cursor;
    }

    private void loadContentSuggesstioṇ̣̣̣() {
        Log.d(TAG, "loadContentSuggesstioṇ̣̣̣");

        Uri uriSong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // Load song
        String[] songProjection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };

        Cursor songCursor = getContext().getApplicationContext().getContentResolver().query(uriSong,
                songProjection,
                null,
                null,
                null);


        if (songCursor != null) {
            while (songCursor.moveToNext()) {
                // check duration > 30s
                if (songCursor.getInt(5) > 30000) {
                    String songId = songCursor.getString(0);
                    String songName = songCursor.getString(1);

                    contentIds.add(songId);
                    contents.add(songName);
                    contentTypes.add(ContentType.SONG);
                }
            }
            songCursor.close();
            Log.d(TAG, "contents: " + contents.size());
        }

        // Load album
        Uri uriAlbum = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        String[] albumProjection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        Cursor albumCursor = getContext().getApplicationContext().getContentResolver().query(uriAlbum,
                albumProjection,
                null,
                null,
                null);


        if (albumCursor != null) {
            while (albumCursor.moveToNext()) {
                String albumId = albumCursor.getString(0);
                String albumName = albumCursor.getString(1);

                contentIds.add(albumId);
                contents.add(albumName);
                contentTypes.add(ContentType.ALBUM);
            }
            albumCursor.close();
            Log.d(TAG, "contents: " + contents.size());
        }

        // Load artist
        Uri uriArtist = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        String[] artistProjection = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        };

        Cursor artistCursor = getContext().getApplicationContext().getContentResolver().query(uriArtist,
                artistProjection,
                null,
                null,
                null);


        if (artistCursor != null) {
            while (artistCursor.moveToNext()) {
                String artistId = artistCursor.getString(0);
                String artistName = artistCursor.getString(1);

                contentIds.add(artistId);
                contents.add(artistName);
                contentTypes.add(ContentType.ARTIST);
            }
            artistCursor.close();
            Log.d(TAG, "contents: " + contents.size());
        }
    }

    private int getSuggestionIcon(int position) {
        switch (contentTypes.get(position)) {
            case SONG:
                return R.drawable.ic_song;
            case ALBUM:
                return R.drawable.img_album;
            case ARTIST:
                return R.drawable.img_headphone;
            default:
                return R.drawable.ic_song;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
