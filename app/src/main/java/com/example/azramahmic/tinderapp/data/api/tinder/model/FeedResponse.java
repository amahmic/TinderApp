package com.example.azramahmic.tinderapp.data.api.tinder.model;

import com.example.azramahmic.tinderapp.data.api.base_model.TinderBaseApiResponse;
import com.example.azramahmic.tinderapp.data.local.model.feed.FeedData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedResponse extends TinderBaseApiResponse {
    @SerializedName("data")
    @Expose
    private List<FeedData> feedData;
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public List<FeedData> getFeedData() {
        return feedData;
    }

    public void setFeedData(List<FeedData> feedData) {
        this.feedData = feedData;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}