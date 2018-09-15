package com.android.aldajo92.popularmovies.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

public abstract class PaginationMoviesScrollListener extends RecyclerView.OnScrollListener {

    private LayoutManager layoutManager;
    private int visibleThreshold;

    private int currentPage = 0;
    private boolean isLoading = true;
    private int previousTotalItemCount = 0;

    public PaginationMoviesScrollListener(LayoutManager layoutManager, int visibleThreshold){
        this.layoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();

        if(isLoading && totalItemCount > previousTotalItemCount){
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        if(!isLoading && lastVisibleItemPosition + visibleThreshold > totalItemCount){
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            isLoading = true;
        }
    }

    public abstract void onLoadMore(int currentPage, int totalItemCount);

    public void resetPagination(){
        currentPage = 0;
        isLoading = true;
        previousTotalItemCount = 0;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
