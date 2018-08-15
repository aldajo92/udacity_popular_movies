package com.android.aldajo92.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.aldajo92.popularmovies.db.MovieDatabase;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<FavoriteMovieEntry>> tasks;

    public MainViewModel(@NonNull Application application) {
        super(application);

        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        Log.d(TAG, "MainViewModel: Actively retriving the task from database");
        tasks = database.favoriteMovieDao().getFavoritesMovies();
    }

    public LiveData<List<FavoriteMovieEntry>> getTasks() {
        return tasks;
    }
}
