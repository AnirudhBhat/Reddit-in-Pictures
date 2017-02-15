package com.example.cumulations.redditinpics.network;

import com.example.cumulations.redditinpics.model.Reddit;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cumulations on 3/2/17.
 */

public interface ApiService {

    @GET("/r/{subreddit}/.json")
    Observable<Reddit> getRedditData(@Path("subreddit") String subreddit, @Query("after") String after);
}
