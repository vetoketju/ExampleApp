package com.villevalta.exampleapp;

import android.support.multidex.MultiDexApplication;

/**
 * Created by villevalta on 8/7/17.
 */

public class ExampleApplication extends MultiDexApplication {

    private static ExampleApplication singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public static ExampleApplication getInstance(){
        return singleton;
    }

}
