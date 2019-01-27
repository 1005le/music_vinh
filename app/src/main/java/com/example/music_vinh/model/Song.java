package com.example.music_vinh.model;

import java.io.Serializable;

public class Song implements Serializable{
    private Long id;
    private String name;
    private String nameArtist;
    private String nameAlbum;
    private String path;
    private String duration;

    public Song(Long id, String name, String nameArtist, String nameAlbum, String path,String duration) {
        this.id = id;
        this.name = name;
        this.nameArtist = nameArtist;
        this.nameAlbum = nameAlbum;
        this.path = path;
        this.duration = duration;
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
