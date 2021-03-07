package com.emreeran.android.roomsample.di;

import android.app.Application;

import com.emreeran.android.roomsample.SampleApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Emre Eran on 26.05.2018.
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ActivityModule.class,
        DbModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(SampleApplication application);
}
