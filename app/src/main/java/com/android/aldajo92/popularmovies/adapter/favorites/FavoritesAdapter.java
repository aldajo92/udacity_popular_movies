package com.android.aldajo92.popularmovies.adapter.favorites;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.models.FavoriteMovieModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<ItemFavoriteMovieHolder> {

    private List<FavoriteMovieModel> list = new ArrayList<>();
    private ItemClickedListener listener;

    public FavoritesAdapter(ItemClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public ItemFavoriteMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_item_favorite_movie, parent, false);
        return new ItemFavoriteMovieHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ItemFavoriteMovieHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(FavoriteMovieModel movieModel) {
        list.add(movieModel);
    }

    public void addItems(List<FavoriteMovieModel> movieModelList) {
        list = movieModelList;
        notifyDataSetChanged();
    }

}
