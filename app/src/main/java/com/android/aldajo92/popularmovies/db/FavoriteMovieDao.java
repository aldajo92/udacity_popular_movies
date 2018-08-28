package com.android.aldajo92.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static com.android.aldajo92.popularmovies.db.DBConstants.FAVORITES_MOVIE_TABLE;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM " + FAVORITES_MOVIE_TABLE)
    LiveData<List<FavoriteMovieEntry>> getFavoritesMovies();

    @Insert
    void insertFavoriteMovie(FavoriteMovieEntry favoriteMovieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(FavoriteMovieEntry taskEntry);

    @Delete
    void deleteFavoriteMovie(FavoriteMovieEntry favoriteMovieEntry);

    @Query("SELECT * FROM " + FAVORITES_MOVIE_TABLE + " WHERE id = :id")
    LiveData<FavoriteMovieEntry> getFavoriteMovieById(long id);

}
