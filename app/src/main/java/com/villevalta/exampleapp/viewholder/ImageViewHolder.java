package com.villevalta.exampleapp.viewholder;

import android.content.Intent;
import android.view.View;

import com.villevalta.exampleapp.activity.ImageActivity;
import com.villevalta.exampleapp.databinding.ListItemImageBinding;
import com.villevalta.exampleapp.model.Image;

/**
 * Created by villevalta on 8/10/17.
 */

public class ImageViewHolder extends AViewHolder<Image> {

    ListItemImageBinding binding;

    public ImageViewHolder(ListItemImageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(final Image object) {
        binding.setItem(object);
        binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ImageActivity.class);
                i.putExtra(ImageActivity.KEY_EXTRA_ID, object.getId());
                v.getContext().startActivity(i);
            }
        });
    }
}
