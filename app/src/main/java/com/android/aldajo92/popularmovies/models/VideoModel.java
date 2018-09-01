package com.android.aldajo92.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class VideoModel {
    @SerializedName("id")
    String id = "";

    @SerializedName("iso_639_1")
    String iso_639_1 = "";

    @SerializedName("iso_3166_1")
    String iso_3166_1 = "";

    @SerializedName("key")
    String key = "";

    @SerializedName("name")
    String name = "";

    @SerializedName("site")
    String site = "";

    @SerializedName("size")
    int size = 0;

    @SerializedName("type")
    String type = "";

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
