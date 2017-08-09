package com.villevalta.exampleapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by villevalta on 8/9/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {

    int status;
    boolean success;
    List<Image> data;

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Image> getData() {
        return data != null ? data : new ArrayList<Image>();
    }
}
