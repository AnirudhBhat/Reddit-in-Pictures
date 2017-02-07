package com.example.cumulations.redditinpics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cumulations on 3/2/17.
 */

public class Children {

    @SerializedName("data")
    @Expose
    private data childData;

    public data getChildData() {
        return childData;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public void setChildData(data childData) {
        this.childData = childData;
    }
}
