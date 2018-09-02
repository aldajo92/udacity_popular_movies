package com.android.aldajo92.popularmovies.models;

public class FavoriteMovieModel {

    private long id;
    private String name;

    public FavoriteMovieModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
