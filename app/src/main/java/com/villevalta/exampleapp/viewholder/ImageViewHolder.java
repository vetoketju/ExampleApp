package com.villevalta.exampleapp.viewholder;

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
    public void bind(Image object) {
        binding.setItem(object);
    }
}
