package com.android.aldajo92.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.adapter.movies.MoviesAdapter;
import com.android.aldajo92.popularmovies.adapter.PaginationMoviesScrollListener;
import com.android.aldajo92.popularmovies.models.MovieModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogMoviesFragment extends Fragment implements ItemClickedListener<MovieModel> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MoviesAdapter adapter;
    private List<MovieModel> movieModelList = new ArrayList<>();

    private PaginationMoviesScrollListener scrollListener;

    private MoviesFragmentListener moviesFragmentViewListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_movies, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new MoviesAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new PaginationMoviesScrollListener(gridLayoutManager, 6) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount) {
                moviesFragmentViewListener.getMoviesListByPage(currentPage + 1);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    public void addItems(List<MovieModel> movies){
        movieModelList.addAll(movies);
        adapter.addItems(movieModelList);
    }

    public void clearList(){
        movieModelList.clear();
        if(scrollListener != null){
            scrollListener.resetPagination();
        }
    }

    @Override
    public void itemClicked(MovieModel movieModel, int position, View view) {
        if(moviesFragmentViewListener != null){
            moviesFragmentViewListener.itemClicked(movieModel, view);
        }
    }

    private void setMoviesFragmentViewListener(MoviesFragmentListener listener) {
        this.moviesFragmentViewListener = listener;
    }

    public static CatalogMoviesFragment getInstance(MoviesFragmentListener listener){
        CatalogMoviesFragment fragment = new CatalogMoviesFragment();
        fragment.setMoviesFragmentViewListener(listener);
        return fragment;
    }
}