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

    private List<FavoriteMovieModel> favoriteMovieModels = new ArrayList<>();
    private ItemClickedListener<FavoriteMovieModel> listener;

    public FavoritesAdapter(ItemClickedListener<FavoriteMovieModel> listener) {
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
        holder.bindData(favoriteMovieModels.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteMovieModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addItem(FavoriteMovieModel movieModel) {
        favoriteMovieModels.add(movieModel);
    }

    public void addItems(List<FavoriteMovieModel> movieModelList) {
        favoriteMovieModels = movieModelList;
        notifyDataSetChanged();
    }

    public List<FavoriteMovieModel> getFavoriteMovieModels() {
        return favoriteMovieModels;
    }

}
