package com.android.aldajo92.popularmovies.adapter.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.aldajo92.popularmovies.R;
import com.android.aldajo92.popularmovies.adapter.ItemClickedListener;
import com.android.aldajo92.popularmovies.models.VideoModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.text_view_name)
    TextView textViewName;

    private VideoModel videoModel;
    private ItemClickedListener<VideoModel> listener;

    public ItemVideoHolder(View itemView, ItemClickedListener<VideoModel> listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.itemClicked(videoModel, getAdapterPosition(), itemView);
    }

    public void bindData(VideoModel videoModel) {
        this.videoModel = videoModel;
        textViewName.setText(videoModel.getName());
    }
}