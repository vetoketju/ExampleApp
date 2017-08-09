package com.villevalta.exampleapp.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by villevalta on 8/9/17.
 */

public class Images extends RealmObject {

    @PrimaryKey
    private String id;
    private int pagesLoaded = 0;
    private RealmList<Image> images = new RealmList<>();
    private long lastUpdated = 0;

    /* Omat metodit */

    public void incrementPagesLoaded(){
        pagesLoaded++;
    }

    public void addImages(List<Image> images){
        if(images != null){
            this.images.addAll(images);
        }
    }

    /* Setterit ja Getterit */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPagesLoaded() {
        return pagesLoaded;
    }

    public void setPagesLoaded(int pagesLoaded) {
        this.pagesLoaded = pagesLoaded;
    }

    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
