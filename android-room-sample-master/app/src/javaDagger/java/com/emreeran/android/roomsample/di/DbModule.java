package com.emreeran.android.roomsample.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.dao.CommentDao;
import com.emreeran.android.roomsample.db.dao.FeedDao;
import com.emreeran.android.roomsample.db.dao.LikeDao;
import com.emreeran.android.roomsample.db.dao.PostDao;
import com.emreeran.android.roomsample.db.dao.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Emre Eran on 26.05.2018.
 */
@Module
public class DbModule {

    @Singleton
    @Provides
    public SampleDb provideDb(Application application) {
        return Room.databaseBuilder(application, SampleDb.class, "sample.db").build();
    }

    @Singleton
    @Provides
    public UserDao provideUserDao(SampleDb db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    public PostDao providePostDao(SampleDb db) {
        return db.postDao();
    }

    @Singleton
    @Provides
    public CommentDao provideCommentDao(SampleDb db) {
        return db.commentDao();
    }

    @Singleton
    @Provides
    public LikeDao provideLikeDao(SampleDb db) {
        return db.likeDao();
    }

    @Singleton
    @Provides
    public FeedDao provideFeedDao(SampleDb db) {
        return db.feedDao();
    }
}
