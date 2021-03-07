package com.emreeran.android.roomsample.di

import com.emreeran.android.roomsample.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [(MainFragmentBuildersModule::class)])
    internal abstract fun contributeMainActivity(): MainActivity
}