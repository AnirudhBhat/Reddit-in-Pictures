package com.example.cumulations.redditinpics;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
        getRedditData()
                //.skip(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Reddit>() {
                    @Override
                    public void call(Reddit reddit) {
                        mCustomAdapter = new CustomAdapter(reddit.getData().getChildren(), getActivity());
                        mRecyclerView.setAdapter(mCustomAdapter);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

        return v;
    }

    private Observable<Reddit> getRedditData() {
        return RestClient.getDataForReddit().getRedditData(mName);
    }

}
