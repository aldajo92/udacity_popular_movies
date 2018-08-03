package com.android.aldajo92.popularmovies.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorite_movie")
public class FavoriteMovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;



}
