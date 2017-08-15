package com.villevalta.exampleapp;

import android.support.multidex.MultiDexApplication;

import com.villevalta.exampleapp.network.service.ImgurApiService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

        // Realm initialisointi
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);


        imgurApiService = new ImgurApiService();
    }

    public static ExampleApplication getInstance(){
        return singleton;
    }

    public ImgurApiService getImgurApiService(){
        return imgurApiService;
    }

}
