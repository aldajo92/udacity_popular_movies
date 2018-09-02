package com.android.aldajo92.popularmovies.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.adapter.favorites.FavoritesAdapter;
import com.android.aldajo92.popularmovies.db.FavoriteMovieEntry;
import com.android.aldajo92.popularmovies.db.MovieDatabase;
import com.android.aldajo92.popularmovies.models.FavoriteMovieModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesMoviesFragment extends Fragment implements ItemClickedListener<FavoriteMovieModel> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private FavoritesAdapter adapter;

    private MoviesFragmentListener moviesFragmentViewListener;

    LiveData<List<FavoriteMovieEntry>> favoriteMoviesEntries;

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

        MovieDatabase database = MovieDatabase.getInstance(getContext());
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

    }

    private void setMovies(List<FavoriteMovieEntry> movies) {
        List<FavoriteMovieModel> list = new ArrayList<>();
        for (FavoriteMovieEntry movieEntry : movies) {
            list.add(new FavoriteMovieModel(movieEntry.getId(), movieEntry.getTitle()));
        }

        adapter.addItems(list);
    }
}
