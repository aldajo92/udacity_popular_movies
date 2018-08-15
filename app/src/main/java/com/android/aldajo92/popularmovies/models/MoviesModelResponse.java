package com.android.aldajo92.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesModelResponse {
    @SerializedName("results")
    List<MovieModel> movies = new ArrayList<>();
}