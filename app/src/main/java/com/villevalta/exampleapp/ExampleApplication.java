package com.villevalta.exampleapp;

import android.support.multidex.MultiDexApplication;

import com.villevalta.exampleapp.network.service.ImgurApiService;

/**
 * Created by villevalta on 8/7/17.
 */

public class ExampleApplication extends MultiDexApplication {

    private static ExampleApplication singleton;
    private ImgurApiService imgurApiService;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        imgurApiService = new ImgurApiService();
    }

    public static ExampleApplication getInstance(){
        return singleton;
    }

    public ImgurApiService getImgurApiService(){
        return imgurApiService;
    }

}
