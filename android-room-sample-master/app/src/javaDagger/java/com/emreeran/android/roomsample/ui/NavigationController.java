package com.emreeran.android.roomsample.ui;

import android.support.v4.app.FragmentManager;

import com.emreeran.android.roomsample.MainActivity;
import com.emreeran.android.roomsample.R;

import javax.inject.Inject;

/**
 * Created by Emre Eran on 26.05.2018.
 */
public class NavigationController {
    private FragmentManager mFragmentManager;
    private int mContainerId = R.id.frame;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        mFragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToInitializer() {
        mFragmentManager.beginTransaction()
                .replace(mContainerId, InitializerFragment.create())
                .commitAllowingStateLoss();
    }

    public void navigateToFeed() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(mContainerId, FeedFragment.create())
                .commitAllowingStateLoss();
    }

    public void navigateToPostDetail(int postId) {
        mFragmentManager.beginTransaction()
                .addToBackStack(PostDetailFragment.class.getSimpleName())
                .setCustomAnimations(
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(mContainerId, PostDetailFragment.create(postId))
                .commitAllowingStateLoss();
    }
}
