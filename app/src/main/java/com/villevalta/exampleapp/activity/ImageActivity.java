package com.villevalta.exampleapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.villevalta.exampleapp.R;
import com.villevalta.exampleapp.databinding.ActivityImageBinding;
import com.villevalta.exampleapp.model.Image;

import io.realm.Realm;

/**
 * Created by villevalta on 8/13/17.
 */

public class ImageActivity extends AppCompatActivity {

    public static final String KEY_EXTRA_ID = "id";

    ActivityImageBinding binding;
    Image image;
    Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();

        if(getIntent().hasExtra(KEY_EXTRA_ID)){

            String id = getIntent().getStringExtra(KEY_EXTRA_ID);
            image = realm.where(Image.class).equalTo("id", id).findFirst();

            binding.setItem(image);
            setTitle(image.getId());
        }
    }

    @Override
    protected void onPause() {
        if(realm != null && !realm.isClosed()){
            realm.close();
        }
        super.onPause();
    }
}
