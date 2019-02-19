package com.example.music_vinh.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.music_vinh.R;
import com.example.music_vinh.model.Song;
import com.example.music_vinh.view.custom.Constants;
import com.example.music_vinh.view.custom.StorageUtil;
import com.example.music_vinh.view.impl.MainActivity;
import com.example.music_vinh.view.impl.PlayActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.internal.Utils;

import static com.example.music_vinh.view.custom.Constants.ACTION_NEXT;
import static com.example.music_vinh.view.custom.Constants.ACTION_PLAY;
import static com.example.music_vinh.view.custom.Constants.ACTION_PREVIOUS;

public class MusicService extends Service implements BaseMediaPlayer
        , MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener
        , MediaPlayer.OnErrorListener {
    private MyBinder mMyBinder = new MyBinder();
    private MediaPlayer mMediaPlayer;
    private ArrayList<Song> mSongs;
    private int mCurrentPossition;

    private boolean mIsShuffle;
    private boolean mIsLoop;
    private Random mRandom;
    private int mProgess;
    private ServiceCallback mServiceCallback;

    private Handler mHandler = new Handler();
    private Runnable mTimeRunnable = new Runnable() {
        @Override
        public void run() {
            mServiceCallback.postCurentTime(getCurrentPosition());
            if (isPlay()) {
                mHandler.postDelayed(this, Constants.DELAY_MILLIS);
            }
        }
    };

    public void setListener(ServiceCallback listener) {
        this.mServiceCallback = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRandom = new Random();
        mCurrentPossition = 0;
        mProgess = 0;
//        mIsShuffle = Utils.getState(getApplicationContext(), Constants.KEY_SHUFFLE);
//        mIsLoop = Utils.getState(getApplicationContext(), Constants.KEY_LOOP);
        mMediaPlayer = new MediaPlayer();
        initMediaPlayer();
        register_loadAudio();
    }

    private void initMediaPlayer() {
        mMediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null) {
            handlerIntent(intent);
        }
        return mMyBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            handlerIntent(intent);
        }
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        release();
        stopSelf();

        unregisterReceiver(loadAudio);
        super.onDestroy();
    }

    @Override
    public void playSong() {
        Song song = mSongs.get(mCurrentPossition);
        Log.d("ten",song.getName());
//        String uri = null;
//        if (song.getId() == 0) {
//          uri = song.getPath();
//          } else {
////           // uri = Utils.getUrlDownload(song.getUri());
//           }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(song.getPath());
            Log.d("path",song.getPath());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
          //  mServiceCallback.showError(getString(R.string.message_error_path));
        }
    }

    @Override
    public void startSong() {
        mMediaPlayer.start();
        mHandler.postDelayed(mTimeRunnable, Constants.DELAY_MILLIS);
    }

    @Override
    public void stopSong() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mServiceCallback.postPauseButon();
        }
    }

    @Override
    public void pauseSong() {
        if (isPlay()) {
            mProgess = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
            mServiceCallback.postPauseButon();
            postNotification();
        }
    }

    @Override
    public void release() {
        mMediaPlayer.release();
    }

    @Override
    public void seekTo(int progess) {
        if (mMediaPlayer != null) {
            mProgess = progess;
            mMediaPlayer.seekTo(mProgess);
            startSong();
        }
    }

    @Override
    public void next() {
        if (mIsShuffle) {
            mCurrentPossition = mRandom.nextInt(mSongs.size());
        } else {
            if (mCurrentPossition == mSongs.size() - 1) {
                mCurrentPossition = 0;
            } else {
                mCurrentPossition++;
            }
        }
        playSong();
    }

    @Override
    public void previous() {
        if (mIsShuffle) {
            mCurrentPossition = mRandom.nextInt(mSongs.size());
        } else {
            if (mCurrentPossition == 0) {
                mCurrentPossition = mSongs.size() - 1;
            } else {
                mCurrentPossition--;
            }
        }
        playSong();
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public boolean isPlay() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mServiceCallback.postTotalTime(getDuration());
        postCurrentTime();
        postTitle(mSongs.get(mCurrentPossition));
        mServiceCallback.postStartButton();
        Log.d("total",getDuration()+"");
//        if (mSongs.get(mCurrentPossition).getAvatarUrl()!=null){
//            mServiceCallback.postAvatar(mSongs.get(mCurrentPossition).getAvatarUrl());
//        }
        mMediaPlayer.start();
        sendAudioInfoBroadcast();
        postNotification();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mIsLoop) {
            playSong();
        } else if (mIsShuffle) {
            mCurrentPossition = mRandom.nextInt(mSongs.size() - 1);
            playSong();
        } else {
            next();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.mSongs = songs;
    }

    public void setCurrentSong(int currentSong) {
        this.mCurrentPossition = currentSong;
    }

    public void continuesSong() {
        mMediaPlayer.seekTo(getProgess());
        mMediaPlayer.start();
        mServiceCallback.postStartButton();
        postCurrentTime();
        postNotification();
    }

    public int getProgess() {
        return mProgess;
    }

    public class MyBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    public void changeLoop() {
        mIsLoop = !mIsLoop;
        mServiceCallback.postLoop(mIsLoop);
    }

    public void changeShuffle() {
        mIsShuffle = !mIsShuffle;
        mServiceCallback.postShuffle(mIsShuffle);
    }

    private BroadcastReceiver loadAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendAudioInfoBroadcast();
        }
    };

    private void register_loadAudio() {
        IntentFilter filter = new IntentFilter("load_audio");
        registerReceiver(loadAudio, filter);
    }

    public void sendAudioInfoBroadcast(){

        //Truyen đen mainActivity
        Intent intent1 = new Intent("send");
        intent1.putExtra(Constants.KEY_POSITION,mCurrentPossition);
        intent1.putExtra(Constants.KEY_PROGESS,mMediaPlayer.getDuration());
        intent1.putParcelableArrayListExtra(Constants.KEY_SONGS, mSongs);

        Log.d("get", mMediaPlayer.getDuration()+"");
//        StorageUtil storage = new StorageUtil(getApplicationContext());
//        storage.storeAudio(mSongs);
//        storage.storeAudioIndex(mCurrentPossition);
        sendBroadcast(intent1);
    }

   private void postNotification() {

        Intent intent = new Intent(this, PlayActivity.class);
      //  Intent intent = new Intent("send");
     /*   Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.KEY_SONGS, mSongs);
        bundle.putInt(Constants.KEY_POSITION,mCurrentPossition);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        intent.putExtra(Constants.KEY_PROGESS,mMediaPlayer.getCurrentPosition());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);*/

//        //Truyen đen mainActivity
//        Intent intent1 = new Intent("send");
//        // intent1.putExtra(Constants.KEY_BUNDLE,bundle);
////        intent1.putExtra(Constants.KEY_BUNDLE,bundle);
//
////        intent1.putExtra(Constants.KEY_POSITION,mCurrentPossition);
////        intent1.putParcelableArrayListExtra(Constants.KEY_SONGS, mSongs);
//
//       StorageUtil storage = new StorageUtil(getApplicationContext());
//         storage.storeAudio(mSongs);
//         storage.storeAudioIndex(mCurrentPossition);
//
//        sendBroadcast(intent1);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);

       PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
               new Intent(this, PlayActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseStartIntent = new Intent(this, MusicService.class);
        pauseStartIntent.setAction(Constants.ACTION_PLAY);

        PendingIntent pplayIntent = PendingIntent.getService(getApplicationContext(),
                0, pauseStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(Constants.ACTION_NEXT);
        PendingIntent pnextIntent = PendingIntent.getService(getApplicationContext(),
                0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previousIntent = new Intent(this, MusicService.class);
        previousIntent.setAction(Constants.ACTION_PREVIOUS);
        PendingIntent ppreviousIntent = PendingIntent.getService(getApplicationContext(),
                0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int iconPauseStart;
        if (mMediaPlayer.isPlaying()) {
            iconPauseStart = R.drawable.ic_pause;
        } else {
            iconPauseStart = R.drawable.ic_stop;
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        Song song = mSongs.get(mCurrentPossition);
//        if (song.getAvatarUrl() != null) {
//            remoteViews.setImageViewUri(R.id.image_avatar_song_notifi, Uri.parse(Utils.getUrlDownload(song.getUri())));
//        }
        remoteViews.setTextViewText(R.id.text_song_name_notifi, song.getName());
        remoteViews.setTextViewText(R.id.text_artist_notifi, song.getNameArtist());
        remoteViews.setImageViewResource(R.id.image_play_notifi, iconPauseStart);

        remoteViews.setOnClickPendingIntent(R.id.image_previous_notifi, ppreviousIntent);
        remoteViews.setOnClickPendingIntent(R.id.image_play_notifi, pplayIntent);
        remoteViews.setOnClickPendingIntent(R.id.image_next_notifi, pnextIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext()
                , Constants.CHANNEL_ID);

        builder.setContentIntent(pendingIntent)
                .setContent(remoteViews)
                .setSmallIcon(R.drawable.image5)
                .setPriority(1);

        Notification notification = builder.build();
        startForeground(Constants.ID_NOTIFICATION_SERVICE, notification);
    }

    private void handlerIntent(Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_PLAY:
                if (isPlay()) {
                    pauseSong();
                } else {
                    continuesSong();
                }
                break;
            case ACTION_NEXT:
                next();
                postNotification();
                break;
            case ACTION_PREVIOUS:
                previous();
                postNotification();
                break;
        }
    }

    private void postTitle(Song song) {
        mServiceCallback.postName(song.getName(), song.getNameArtist());
    }

    private void postCurrentTime() {
        mHandler.postDelayed(mTimeRunnable, Constants.DELAY_MILLIS);
    }
}
