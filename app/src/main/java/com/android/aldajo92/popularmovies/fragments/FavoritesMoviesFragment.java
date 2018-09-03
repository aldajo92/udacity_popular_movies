package com.android.aldajo92.popularmovies.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aldajo92.popularmovies.AppExecutors;
import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.adapter.favorites.FavoritesAdapter;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.db.MovieDatabase;
import com.android.aldajo92.popularmovies.models.FavoriteMovieModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesMoviesFragment extends Fragment implements ItemClickedListener<FavoriteMovieModel> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private FavoritesAdapter adapter;

    private MoviesFragmentListener moviesFragmentViewListener;

    private LiveData<List<FavoriteMovieEntry>> favoriteMoviesEntries;

    private Map<FavoriteMovieModel, FavoriteMovieEntry> mapFavorites;

    private MovieDatabase database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_movies, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();

        database = MovieDatabase.getInstance(getContext());
        favoriteMoviesEntries = database.favoriteMovieDao().getFavoritesMovies();

        favoriteMoviesEntries.observe(getActivity(), new Observer<List<FavoriteMovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovieEntries) {
                if(favoriteMovieEntries != null){
                    setMovies(favoriteMovieEntries);
                }
            }
        });
    }

    private void initRecyclerView() {
        adapter = new FavoritesAdapter(this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<FavoriteMovieModel> favoriteMovies = adapter.getFavoriteMovieModels();
                        FavoriteMovieModel movieModel = favoriteMovies.get(position);
                        FavoriteMovieEntry entry = mapFavorites.get(movieModel);
                        database.favoriteMovieDao().deleteFavoriteMovie(entry);
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setMoviesFragmentViewListener(MoviesFragmentListener listener) {
        this.moviesFragmentViewListener = listener;
    }

    public static FavoritesMoviesFragment getInstance(MoviesFragmentListener listener) {
        FavoritesMoviesFragment fragment = new FavoritesMoviesFragment();
        fragment.setMoviesFragmentViewListener(listener);
        return fragment;
    }

    @Override
    public void itemClicked(FavoriteMovieModel data, int position, View imageView) {
        moviesFragmentViewListener.favoriteMovieClicked(data);
    }

    private void setMovies(List<FavoriteMovieEntry> movies) {
        mapFavorites = new HashMap<>();

        for (FavoriteMovieEntry movieEntry : movies) {
            FavoriteMovieModel model = new FavoriteMovieModel(movieEntry.getId(), movieEntry.getTitle());
            mapFavorites.put(model, movieEntry);
        }

        List<FavoriteMovieModel> list =  new ArrayList<>(mapFavorites.keySet());

        Collections.sort(list, new Comparator<FavoriteMovieModel>() {
            @Override
            public int compare(FavoriteMovieModel t1, FavoriteMovieModel t2) {
                return t1.getName().compareTo(t2.getName());
            }
        });

        adapter.addItems(list);
    }
}
