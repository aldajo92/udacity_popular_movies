package com.android.aldajo92.popularmovies.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static com.android.aldajo92.popularmovies.db.DBConstants.FAVORITES_MOVIE_TABLE;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM " + FAVORITES_MOVIE_TABLE)
    List<FavoriteMovieEntry> getFavoritesMovies();

    @Insert
    void insertFavoriteMovie(FavoriteMovieEntry favoriteMovieEntry);

    @Delete
    void deleteFavoriteMovie(FavoriteMovieEntry favoriteMovieEntry);

}
