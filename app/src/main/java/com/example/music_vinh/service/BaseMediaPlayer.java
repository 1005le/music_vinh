package com.example.music_vinh.service;

public interface BaseMediaPlayer {
    void startSong();

    void playSong();

    void stopSong();

    void pauseSong();

    void release();

    void seekTo(int progess);

    void next();

    void previous();

    long getCurrentPosition();

    long getDuration();

    boolean isPlay();
}
