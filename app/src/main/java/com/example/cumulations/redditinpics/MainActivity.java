package com.example.cumulations.redditinpics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.subreddit)
    EditText mSubreddit;
    @Bind(R.id.find)
    Button mSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.find)
    void onFindClicked() {
        MainFragment mainFragment = MainFragment.getInstance(mSubreddit.getText().toString());

        getSupportFragmentManager().beginTransaction().add(R.id.container, mainFragment).commit();
    }
}
