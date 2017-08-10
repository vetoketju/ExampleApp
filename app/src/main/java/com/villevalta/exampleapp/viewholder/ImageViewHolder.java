package com.villevalta.exampleapp.viewholder;

import android.view.View;
import android.widget.TextView;

import com.villevalta.exampleapp.R;
import com.villevalta.exampleapp.model.Image;

/**
 * Created by villevalta on 8/10/17.
 */

public class ImageViewHolder extends AViewHolder<Image> {

    TextView title;

    public ImageViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
    }

    @Override
    public void bind(Image object) {
        title.setText(object.getTitle());
    }
}
