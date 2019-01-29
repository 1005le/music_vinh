package com.example.music_vinh.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Album implements Parcelable {
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

    protected Album(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        nameArtist = in.readString();
        images = in.readString();
        amountSong = in.readInt();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

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
        parcel.writeString(images);
        parcel.writeInt(amountSong);
    }
}
