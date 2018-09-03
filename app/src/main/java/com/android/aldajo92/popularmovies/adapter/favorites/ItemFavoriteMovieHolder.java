package com.android.aldajo92.popularmovies.adapter.favorites;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.models.FavoriteMovieModel;

import butterknife.BindView;
import butterknife.ButterKnife;

class ItemFavoriteMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.text_view_name)
    TextView textViewName;

    @BindView(R.id.imageView)
    ImageView imageView;

    private FavoriteMovieModel model;
    private ItemClickedListener<FavoriteMovieModel> listener;

    public ItemFavoriteMovieHolder(View itemView, ItemClickedListener<FavoriteMovieModel> listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.listener = listener;
    }

    public void bindData(FavoriteMovieModel model) {
        this.model = model;
        textViewName.setText(model.getName());
    }

    @Override
    public void onClick(View view) {
        listener.itemClicked(model, getAdapterPosition(), imageView);
    }
}