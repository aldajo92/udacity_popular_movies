package com.android.aldajo92.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesVideoResponse {
    @SerializedName("id")
    String id = "";

    @SerializedName("results")
    List<VideoModel> movies = new ArrayList<>();

    public String getId() {
        return id;
    }

    public List<VideoModel> getMovies() {
        return movies;
    }
}