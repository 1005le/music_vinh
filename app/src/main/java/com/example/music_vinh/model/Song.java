package com.example.music_vinh.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Song implements Parcelable {
    private Long id;
    private String name;
    private String nameArtist;
    private String nameAlbum;
    private String path;
    private Long duration;

    public Song() {
    }

    public Song(Long id, String name, String nameArtist, String nameAlbum) {
        this.id = id;
        this.name = name;
        this.nameArtist = nameArtist;
        this.nameAlbum = nameAlbum;
    }

    public Song(Long id, String name, String nameArtist, String nameAlbum, String path) {
        this.id = id;
        this.name = name;
        this.nameArtist = nameArtist;
        this.nameAlbum = nameAlbum;
        this.path = path;
    }

    public Song(Long id, String name, String nameArtist, String nameAlbum, String path, Long duration) {
        this.id = id;
        this.name = name;
        this.nameArtist = nameArtist;
        this.nameAlbum = nameAlbum;
        this.path = path;
        this.duration = duration;
    }

    protected Song(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        nameArtist = in.readString();
        nameAlbum = in.readString();
        path = in.readString();
        if (in.readByte() == 0) {
            duration = null;
        } else {
            duration = in.readLong();
        }
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public Long getDuration() {
        return duration;
    }
    public void setDuration(Long duration) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(name);
        parcel.writeString(nameArtist);
        parcel.writeString(nameAlbum);
        parcel.writeString(path);
        if (duration == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(duration);
        }
    }
}
