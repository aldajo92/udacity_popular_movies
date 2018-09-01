package com.android.aldajo92.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesReviewResponse {
    @SerializedName("results")
    List<ReviewModel> reviews = new ArrayList<>();

    public List<ReviewModel> getReviews() {
        return reviews;
    }
}