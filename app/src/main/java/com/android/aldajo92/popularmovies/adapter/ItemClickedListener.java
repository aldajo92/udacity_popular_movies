package com.android.aldajo92.popularmovies.adapter;

import android.view.View;

public interface ItemClickedListener<T> {

    void itemClicked(T data, int position, View imageView);

}
