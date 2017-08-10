package com.villevalta.exampleapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.villevalta.exampleapp.R;
import com.villevalta.exampleapp.databinding.ListItemImageBinding;
import com.villevalta.exampleapp.model.Images;
import com.villevalta.exampleapp.viewholder.AViewHolder;
import com.villevalta.exampleapp.viewholder.ImageViewHolder;
import com.villevalta.exampleapp.viewholder.LoadingViewHolder;

import io.realm.RealmChangeListener;

/**
 * Created by villevalta on 8/10/17.
 */

public class ImagesAdapter extends RecyclerView.Adapter<AViewHolder> implements RealmChangeListener<Images> {

    private final static int TYPE_LOADING = 0;
    private final static int TYPE_IMAGE = 1;

    private boolean showLoading;
    private Images images;

    public void initialize(Images images){
        this.images = images;
        this.images.addChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public AViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_LOADING) {
            View view = inflater.inflate(R.layout.list_item_loading, parent, false);
            return new LoadingViewHolder(view);
        } else if (viewType == TYPE_IMAGE) {
            ListItemImageBinding binding = ListItemImageBinding.inflate(inflater, parent, false);
            return new ImageViewHolder(binding);
        } else {
            throw new IllegalArgumentException("viewType is not supported!");
        }

    }

    @Override
    public void onBindViewHolder(AViewHolder holder, int position) {
        if(holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(images.getImages().get(position));
        }

        // Tässä voisi kutsua myös loadingViewHolderin bind metodia, mutta ei siellä tehdä mitään.

    }

    @Override
    public int getItemCount() {

        int count = 0;

        if (showLoading) {
            count++;
        }

        if (images != null && images.getImages() != null) {
            count += images.getImages().size();
        }

        return count;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    private int getLoadingItemPosition() {
        if(showLoading){
            if (images != null && images.getImages() != null) {
                return images.getImages().size();
            } else {
                return 0;
            }
        }else{
            return -1; // spinneriä ei näytetä, joten palautetaan -1
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getLoadingItemPosition() == position) {
            return TYPE_LOADING;
        } else {
            return TYPE_IMAGE;
        }
    }

    @Override
    public void onChange(Images images) {
        notifyDataSetChanged();
    }
}
