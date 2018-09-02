package com.android.aldajo92.popularmovies.adapter.favorites;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.models.FavoriteMovieModel;

import butterknife.BindView;
import butterknife.ButterKnife;

class ItemFavoriteMovieHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_name)
    TextView textViewName;

    private FavoriteMovieModel model;
    private ItemClickedListener listener;

    public ItemFavoriteMovieHolder(View itemView, ItemClickedListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void bindData(FavoriteMovieModel model) {
        this.model = model;
        textViewName.setText(model.getName());
    }
}