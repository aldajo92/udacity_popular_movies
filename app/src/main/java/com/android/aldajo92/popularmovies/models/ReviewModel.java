package com.android.aldajo92.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class ReviewModel {
    @SerializedName("id")
    String id = "";

    @SerializedName("content")
    String content = "";

    @SerializedName("author")
    String author = "";

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
