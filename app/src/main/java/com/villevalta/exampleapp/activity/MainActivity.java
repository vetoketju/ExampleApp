package com.villevalta.exampleapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.villevalta.exampleapp.ExampleApplication;
import com.villevalta.exampleapp.R;
import com.villevalta.exampleapp.model.Image;
import com.villevalta.exampleapp.model.Images;
import com.villevalta.exampleapp.model.Page;
import com.villevalta.exampleapp.network.service.ImgurApiService;

import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    ImgurApiService apiService;

    private String subreddit = "funny";
    private String sort = "hot";

    private Images images;

    private Realm realm;

    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = ExampleApplication.getInstance().getImgurApiService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        realm = Realm.getDefaultInstance();

        // haetaan tallennetut kuvat realmista jos löytyy
        images = realm.where(Images.class).contains("id", subreddit + sort).findFirst();

        // Jos ei löytynyt, lisätään se sinne
        if(images == null){
            images = new Images();
            images.setId(subreddit + sort);

            // Kaikki realm kirjoitukset pitää tapahtua transaction sisällä
            // Näillä komennoilla voidaan tehdä transactio synkronisesti
            realm.beginTransaction();
            images = realm.copyToRealm(images); // Realm palauttaa oman kopionsa, joten luetaan se takaisin muuttujaan
            realm.commitTransaction();
        }

        // Ensimmäinen sivuhaku, jos sivuja ei ole vielä haettu
        if(images.getPagesLoaded() == 0){
            loadPage();
        }else{
            Log.d(TAG, "Images loaded from database: ");
            for (Image image : images.getImages()) {
                Log.d(TAG, "image: " + image.getTitle());
            }
        }

    }

    @Override
    protected void onPause() {
        // TODO: Cancel http request here
        if(realm != null && !realm.isClosed()){
            realm.close(); // Suljetaan realm
        }
        super.onPause();
    }

    private void loadPage(){

        int page = images.getPagesLoaded();

        loading = true;
        apiService.getImagesPage(subreddit, sort, page).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (response != null && response.body().isSuccess()) {
                    realm.beginTransaction();
                    images.incrementPagesLoaded();
                    images.addImages(response.body().getData());
                    images.setLastUpdated(new Date().getTime());
                    realm.commitTransaction();
                    Log.d(TAG, "Images loaded from web: ");
                    for (Image image : images.getImages()) {
                        Log.d(TAG, "image: " + image.getTitle());
                    }
                }else{
                    Log.e(TAG, "onResponse: NO SUCCESS :(" );
                }
                loading = false;
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                loading = false;
                Log.e(TAG, "onFailure:", t);
            }
        });
    }

}
