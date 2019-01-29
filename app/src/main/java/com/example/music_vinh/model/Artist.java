package com.example.music_vinh.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Artist implements Parcelable {
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

    public Artist(Long id, String name,int amountAlbum, int amountSong, String images) {
        this.id = id;
        this.name = name;
        this.amountAlbum = amountAlbum;
        this.amountSong = amountSong;
        this.images = images;
    }

    protected Artist(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        amountAlbum = in.readInt();
        amountSong = in.readInt();
        images = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

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
        parcel.writeInt(amountAlbum);
        parcel.writeInt(amountSong);
        parcel.writeString(images);
    }
}
