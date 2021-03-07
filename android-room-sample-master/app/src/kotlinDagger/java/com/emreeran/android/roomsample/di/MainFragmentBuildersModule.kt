package com.emreeran.android.roomsample.di

import com.emreeran.android.roomsample.ui.FeedFragment
import com.emreeran.android.roomsample.ui.InitializerFragment
import com.emreeran.android.roomsample.ui.PostDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Emre Eran on 21.04.2018.
 */
@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    internal abstract fun contributeInitializerFragment(): InitializerFragment

    @ContributesAndroidInjector
    internal abstract fun contributeFeedFragment(): FeedFragment

    @ContributesAndroidInjector
    internal abstract fun contributePostDetailFragment(): PostDetailFragment
}