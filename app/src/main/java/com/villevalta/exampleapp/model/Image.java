package com.villevalta.exampleapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by villevalta on 8/9/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

    private String title;
    private String link;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
