package com.example.music_vinh.model;

public class Album {
    private String id;
    private String name;
    private String nameArtist;
    private String images;

    public Album(String id, String name, String nameArtist, String images) {
        this.id = id;
        this.name = name;
        this.nameArtist = nameArtist;
        this.images = images;
    }

    public Album(String name, String nameArtist, String images) {
        this.name = name;
        this.nameArtist = nameArtist;
        this.images = images;
    }

    public void setId(String id) {
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
