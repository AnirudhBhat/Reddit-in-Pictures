package com.example.cumulations.redditinpics.model;

/**
 * Created by cumulations on 3/2/17.
 */

public class data {
    private String title;
    private String subreddit;

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    private Preview preview;

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
