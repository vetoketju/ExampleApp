package com.villevalta.exampleapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.villevalta.exampleapp.ExampleApplication;
import com.villevalta.exampleapp.R;
import com.villevalta.exampleapp.model.Image;
import com.villevalta.exampleapp.model.Page;
import com.villevalta.exampleapp.network.service.ImgurApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    ImgurApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ExampleApplication.getInstance().getImgurApiService();

        apiService.getTopFunnyImagesPage(0).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {

                if (response.body().isSuccess()) {
                    for (Image image : response.body().getData()) {
                        Log.d(TAG, "image: " + image.getTitle());
                    }
                }else{
                    Log.e(TAG, "onResponse: NO SUCCESS :(" );
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
