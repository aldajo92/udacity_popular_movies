package com.android.aldajo92.popularmovies.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorite_movie")
public class FavoriteMovieEntry {

    @PrimaryKey()
    private long id;

    @ColumnInfo
    private String title;

    public FavoriteMovieEntry(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }
}
