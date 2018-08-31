package com.android.aldajo92.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.db.MovieDatabase;

public class DetailMovieViewModel extends AndroidViewModel {

    private LiveData<FavoriteMovieEntry> tasks;

    MovieDatabase database;

    public DetailMovieViewModel(Application application) {
        super(application);
        database = MovieDatabase.getInstance(this.getApplication());
    }

    LiveData<FavoriteMovieEntry> getFavoriteMovie(long id) {
        return tasks = database.favoriteMovieDao().getFavoriteMovieById(id);
    }

}
