package com.android.aldajo92.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.aldajo92.popularmovies.db.MovieDatabase;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.models.MoviesModelResponse;
import com.android.aldajo92.popularmovies.newnetwork.MoviesAPI;
import com.android.aldajo92.popularmovies.newnetwork.MoviesService;

import java.util.List;

import retrofit2.Call;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<FavoriteMovieEntry>> tasks;

    private MoviesAPI moviesService;

    private MovieDatabase mDb;

    MainViewModel(@NonNull Application application) {
        super(application);

        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
//        Log.d(TAG, "MainViewModel: Actively retriving the task from database");
        tasks = database.favoriteMovieDao().getFavoritesMovies();

        mDb = MovieDatabase.getInstance(application);
        moviesService = MoviesService.getClient().create(MoviesAPI.class);
    }

    public LiveData<List<FavoriteMovieEntry>> getFavoritesMoviesFromDb() {
        return mDb.favoriteMovieDao().getFavoritesMovies();
    }

    public LiveData<List<FavoriteMovieEntry>> getTasks() {
        return tasks;
    }

    public Call<MoviesModelResponse> getMovies(String filter) {
        return moviesService.getMovies(filter, 1);
    }

    public Call<MoviesModelResponse> getMoviesByPagination(String filter, int page) {
        return moviesService.getMovies(filter, page);
    }
}
