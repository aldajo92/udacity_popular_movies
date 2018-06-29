package com.android.aldajo92.popularmovies.adapter;

import android.widget.ImageView;

import com.android.aldajo92.popularmovies.models.MovieModel;

public interface MovieItemListener {

    void itemClicked(MovieModel movieModel, int position, ImageView imageView);

}
