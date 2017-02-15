package com.example.cumulations.redditinpics;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cumulations.redditinpics.adapters.CustomAdapter;
import com.example.cumulations.redditinpics.model.Children;
import com.example.cumulations.redditinpics.model.Reddit;
import com.example.cumulations.redditinpics.network.RestClient;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private String mName;
    private CustomAdapter mCustomAdapter;

    private int visibleItemCount;
    private int totalItemCount = 0;
    private boolean loading = false;
    private int pastVisibleItemCount;
    private LinearLayoutManager linearLayoutManager;
    private String after = "";

    public static MainFragment getInstance(String name) {
        MainFragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("subredditname", name);
        mainFragment.setArguments(args);
        return mainFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString("subredditname");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) (v.findViewById(R.id.recycler_view));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItemCount = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItemCount) >= totalItemCount) {
                            loading = true;
                            getRedditData(after)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Reddit>() {
                                        @Override
                                        public void call(Reddit reddit) {
                                            mCustomAdapter.setResults(reddit.getData().getChildren());
                                            mCustomAdapter.notifyDataSetChanged();
                                            after = reddit.getData().getAfter();
                                            loading = false;

                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {

                                        }
                                    });
                        }
                    }
                }
            }
        });
        getRedditData(after)
                //.skip(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Reddit>() {
                    @Override
                    public void call(Reddit reddit) {
                        mCustomAdapter = new CustomAdapter(reddit.getData().getChildren(), getActivity());
                        mRecyclerView.setAdapter(mCustomAdapter);
                        after = reddit.getData().getAfter();
                        loading = false;

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

        return v;
    }

    private Observable<Reddit> getRedditData(String after) {
        return RestClient.getDataForReddit().getRedditData(mName, after);
    }

}
