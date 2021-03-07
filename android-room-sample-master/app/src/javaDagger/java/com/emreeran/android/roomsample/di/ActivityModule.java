package com.emreeran.android.roomsample.di;

import com.emreeran.android.roomsample.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Emre Eran on 26.05.2018.
 */
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = {MainFragmentBuildersModule.class})
    abstract MainActivity contributeMainActivity();
}
