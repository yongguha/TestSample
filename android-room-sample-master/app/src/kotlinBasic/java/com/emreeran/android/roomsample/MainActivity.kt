package com.emreeran.android.roomsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.emreeran.android.roomsample.ui.FeedFragment
import com.emreeran.android.roomsample.ui.InitializerFragment
import com.emreeran.android.roomsample.ui.PostDetailFragment

/**
 * Created by Emre Eran on 26.05.2018.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportFragmentManager.beginTransaction()
                .replace(R.id.frame, InitializerFragment.create())
                .commitAllowingStateLoss()
    }

    fun navigateToFeed() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.frame, FeedFragment.create())
                .commitAllowingStateLoss()
    }

    fun navigateToPostDetail(postId: Int) {
        supportFragmentManager.beginTransaction()
                .addToBackStack(PostDetailFragment::class.java.simpleName)
                .setCustomAnimations(
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.frame, PostDetailFragment.create(postId))
                .commitAllowingStateLoss()
    }
}