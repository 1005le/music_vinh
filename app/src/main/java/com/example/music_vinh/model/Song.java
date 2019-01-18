package com.example.music_vinh.model;

public class Song {
    private String id;
    private String name;
    private String nameArtist;
    private String nameAlbum;
    private String path;

    public Song(String name, String nameArtist) {
        this.name = name;
        this.nameArtist = nameArtist;
    }
    public Song(String id, String name, String nameArtist, String nameAlbum, String path) {
        this.id = id;
        this.name = name;
        this.nameArtist = nameArtist;
        this.nameAlbum = nameAlbum;
        this.path = path;
    }

    public String getId() {
        return id;
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

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
