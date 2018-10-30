package com.android.aldajo92.popularmovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavoriteMovieEntry.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static MovieDatabase favoriteMovieInstance;

    public static MovieDatabase getInstance(Context context) {
        if (favoriteMovieInstance == null) {
            synchronized (LOCK) {
                favoriteMovieInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, DBConstants.DATABASE_NAME)
                        .build();
            }
        }
        return favoriteMovieInstance;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();

}
