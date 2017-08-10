package com.villevalta.exampleapp.bindingadapter;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by villevalta on 8/10/17.
 */

public class ViewBindingAdapters {

    @BindingAdapter("url")
    public static void loadUrlToImageView(ImageView view, String url){
        if(!TextUtils.isEmpty(url)){
            Picasso.with(view.getContext()).load(url).into(view);
        }
    }

}
