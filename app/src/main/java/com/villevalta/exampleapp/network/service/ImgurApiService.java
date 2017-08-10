package com.villevalta.exampleapp.network.service;

import com.villevalta.exampleapp.BuildConfig;
import com.villevalta.exampleapp.model.Page;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by villevalta on 8/9/17.
 */

public class ImgurApiService {

    private static final String IMGUR_API_BASE_URL = "https://api.imgur.com/3/";

    OkHttpClient httpClient;
    ImgurApiServiceInterface service;

    public ImgurApiService() {

        // Luodaan OkHttpClient instanssi. Imgur tapauksessa jokaiseen http kutsuun pitää lisätä authorization header. Sitä varten tehdään Interceptor, joka laittaa sen jokaiseen kutsuun.
        httpClient = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder().addHeader("Authorization", "Client-ID " + BuildConfig.IMGUR_CLIENT_ID).build();
                return chain.proceed(request);
            }
        })
                // Säädetään timeoutit isommiksi. Oletukset eivät yleensä riitä, koska julkiset rajapinnat ovat hitaita:
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            httpClient = httpClient.newBuilder().addInterceptor(loggingInterceptor).build();
        }

        service = new Retrofit.Builder()
                .baseUrl(IMGUR_API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(ImgurApiServiceInterface.class);
    }


    public Call<Page> getTopFunnyImagesPage(int page) {
        return service.getPage("funny", "top", page);
    }

    public Call<Page> getImagesPage(String subreddit, String sort, int page) {
        return service.getPage(subreddit, sort, page);
    }


}
