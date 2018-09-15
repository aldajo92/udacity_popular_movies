package com.android.aldajo92.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteMovieModel implements Parcelable {

    private long id;
    private String name;
    private String imageUrl;

    public FavoriteMovieModel(long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    protected FavoriteMovieModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<FavoriteMovieModel> CREATOR = new Creator<FavoriteMovieModel>() {
        @Override
        public FavoriteMovieModel createFromParcel(Parcel in) {
            return new FavoriteMovieModel(in);
        }

        @Override
        public FavoriteMovieModel[] newArray(int size) {
            return new FavoriteMovieModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
    }
}
