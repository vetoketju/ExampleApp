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

    OkHttpClient httpClient;
    ImgurApiServiceInterface service;


    public ImgurApiService() {


        Interceptor authInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder().addHeader("Authorization", "Client-ID " + BuildConfig.IMGUR_CLIENT_ID).build();
                return chain.proceed(request);
            }
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);


        // Luodaan OkHttpClient instanssi. Imgur tapauksessa jokaiseen http kutsuun pitää lisätä authorization header. Sitä varten tehdään Interceptor, joka laittaa sen jokaiseen kutsuun.
        httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(authInterceptor)
                .addNetworkInterceptor(loggingInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        service = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
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
