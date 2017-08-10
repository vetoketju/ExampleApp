package com.villevalta.exampleapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by villevalta on 8/10/17.
 */

public class PaginatingRecyclerView extends RecyclerView{

    private int triggerLimit = 1; // default is one

    private LoadMoreListener listener;

    public PaginatingRecyclerView(Context context) {
        super(context);
    }

    public PaginatingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PaginatingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Tällä määrätään milloin tämä recyclerview kertoo, että olisi aika ladata lisää listaitemeitä.
     * @param triggerLimit Maksimi määrä itemeitä mitä saa olla vielä näkymän alapuolella ei näkyvissä.
     */
    public void setPageLoadTriggerLimit(int triggerLimit){
        this.triggerLimit = triggerLimit;
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.listener = listener;
    }

    private final OnScrollListener scrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(getAdapter() == null || listener == null){
                return; // turha edes laskea tarvitseeko ladata lisää
            }

            int lastVisibleItemPosition = getLastVisibleItemPosition();
            if(lastVisibleItemPosition >= getAdapter().getItemCount() - triggerLimit){
                // Nyt käyttäjä on scrollannut melkein loppuun asti. Kerrotaan kuuntelijalle, että täytyisi ladata lisää tavaraa listalle!
                listener.shouldLoadMore();
            }

        }
    };

    private int getLastVisibleItemPosition(){
        if(getLayoutManager() instanceof LinearLayoutManager){
            return ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        }else if(getLayoutManager() instanceof GridLayoutManager){
            return ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        }else{
            return 0;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addOnScrollListener(scrollListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        removeOnScrollListener(scrollListener);
        super.onDetachedFromWindow();
    }

    public interface LoadMoreListener{
        void shouldLoadMore();
    }


}
