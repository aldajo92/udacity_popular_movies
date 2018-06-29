package com.android.aldajo92.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable {

    private String name = "";
    private String average = "";
    private String imageUrl = "";
    private String overview = "";
    private String releaseDate = "";

    public MovieModel(String name, String average, String imageUrl, String overview, String releaseDate) {
        this.name = name;
        this.average = average;
        this.imageUrl = imageUrl;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    protected MovieModel(Parcel in) {
        name = in.readString();
        average = in.readString();
        imageUrl = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getAverage() {
        return average;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(average);
        dest.writeString(imageUrl);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }
}
