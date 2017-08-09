package com.villevalta.exampleapp.network.service;

import com.villevalta.exampleapp.model.Page;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by villevalta on 8/9/17.
 */

public interface ImgurApiServiceInterface {

    @GET("/gallery/r/{subreddit}/{sort}/{page}")
    Call<Page> getPage(@Path("subreddit") String subreddit, @Path("sort") String sort, @Path("page") int page);

}
