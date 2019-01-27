package com.example.music_vinh.model;

import android.os.Parcelable;

import java.io.Serializable;

public class Album implements Serializable {
    private Long id;
    private String name;
    private String nameArtist;
    private String images;
    private int amountSong;

    public Album(Long id, String name, String nameArtist, String images,int amountSong) {
        this.id = id;
        this.name = name;
        this.nameArtist = nameArtist;
        this.images = images;
        this.amountSong = amountSong;
    }

    public Album(String name, String nameArtist, String images, int amountSong) {
        this.name = name;
        this.nameArtist = nameArtist;
        this.images = images;
        this.amountSong = amountSong;
    }

    public int getAmountSong() {
        return amountSong;
    }
    public void setAmountSong(int amountSong) {
        this.amountSong = amountSong;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
