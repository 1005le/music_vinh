package com.example.music_vinh.model;

import java.io.Serializable;

public class Artist implements Serializable {
    private Long id;
    private String name;
    private int amountAlbum;
    private int amountSong;
    private String images;

    public Artist(String name, int amountAlbum, int amountSong, String images) {
        this.name = name;
        this.amountAlbum = amountAlbum;
        this.amountSong = amountSong;
        this.images = images;
    }

    public Artist(Long id, String name, int amountAlbum, int amountSong, String images) {
        this.id = id;
        this.name = name;
        this.amountAlbum = amountAlbum;
        this.amountSong = amountSong;
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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

    public int getAmountAlbum() {
        return amountAlbum;
    }

    public void setAmountAlbum(int amountAlbum) {
        this.amountAlbum = amountAlbum;
    }

    public int getAmountSong() {
        return amountSong;
    }

    public void setAmountSong(int amountSong) {
        this.amountSong = amountSong;
    }
}
