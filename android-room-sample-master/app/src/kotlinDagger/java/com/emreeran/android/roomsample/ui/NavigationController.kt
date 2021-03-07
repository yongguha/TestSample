package com.emreeran.android.roomsample.ui

import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.MainActivity
import javax.inject.Inject


/**
 * Created by Emre Eran on 21.04.2018.
 */
class NavigationController @Inject constructor(activity: MainActivity) {
    private val fragmentManager = activity.supportFragmentManager!!
    private val containerId = R.id.frame

    fun navigateToInitializer() {
        fragmentManager.beginTransaction()
                .replace(containerId, InitializerFragment.create())
                .commitAllowingStateLoss()
    }

    fun navigateToFeed() {
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(containerId, FeedFragment.create())
                .commitAllowingStateLoss()
    }

    fun navigateToPostDetail(postId: Int) {
        fragmentManager.beginTransaction()
                .addToBackStack(PostDetailFragment::class.java.simpleName)
                .setCustomAnimations(
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(containerId, PostDetailFragment.create(postId))
                .commitAllowingStateLoss()
    }
}