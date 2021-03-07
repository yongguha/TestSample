package com.emreeran.android.roomsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.emreeran.android.roomsample.ui.FeedFragment;
import com.emreeran.android.roomsample.ui.InitializerFragment;
import com.emreeran.android.roomsample.ui.PostDetailFragment;

/**
 * Created by Emre Eran on 25.04.2018.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, InitializerFragment.create())
                .commitAllowingStateLoss();
    }

    public void navigateToFeed() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.frame, FeedFragment.create())
                .commitAllowingStateLoss();
    }

    public void navigateToPostDetail(int postId) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(PostDetailFragment.class.getSimpleName())
                .setCustomAnimations(
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.frame, PostDetailFragment.create(postId))
                .commitAllowingStateLoss();
    }
}
