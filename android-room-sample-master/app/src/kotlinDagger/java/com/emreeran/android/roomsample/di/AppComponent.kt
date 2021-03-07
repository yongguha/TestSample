package com.emreeran.android.roomsample.di

import android.app.Application
import com.emreeran.android.roomsample.SampleApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 *  Created by Emre Eran on 20.04.2018.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    DbModule::class,
    ActivityModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: SampleApplication)
}