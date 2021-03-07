package com.emreeran.android.roomsample.di;

import com.emreeran.android.roomsample.ui.FeedFragment;
import com.emreeran.android.roomsample.ui.InitializerFragment;
import com.emreeran.android.roomsample.ui.PostDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Emre Eran on 26.05.2018.
 */
@Module
public abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    public abstract InitializerFragment contributeInitializerFragment();

    @ContributesAndroidInjector
    public abstract FeedFragment contributeFeedFragment();

    @ContributesAndroidInjector
    public abstract PostDetailFragment contributePostDetailFragment();
}
