package com.android.aldajo92.popularmovies.adapter.movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.models.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<ItemMovieHolder> {

    private List<MovieModel> list = new ArrayList<>();
    private ItemClickedListener listener;

    public MoviesAdapter(ItemClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public ItemMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_item_movie, parent, false);
        return new ItemMovieHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ItemMovieHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItems(List<MovieModel> movieModelList) {
        list = movieModelList;
        notifyDataSetChanged();
    }

}
