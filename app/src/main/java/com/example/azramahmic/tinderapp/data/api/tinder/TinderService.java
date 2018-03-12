package com.example.azramahmic.tinderapp.data.api.tinder;

import com.example.azramahmic.tinderapp.data.api.tinder.model.FeedResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TinderService {
    @GET("statements")
    Single<FeedResponse> fetchFeed(@Query("page") int page);
}
