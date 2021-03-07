package com.emreeran.android.roomsample.di

import android.app.Application
import android.arch.persistence.room.Room
import com.emreeran.android.roomsample.db.SampleDb
import com.emreeran.android.roomsample.db.dao.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *  Created by Emre Eran on 20.04.2018.
 */
@Module
class DbModule {
    private val _dbName = "sample.db"

    @Singleton
    @Provides
    fun provideDb(app: Application): SampleDb {
        return Room.databaseBuilder(app, SampleDb::class.java, _dbName).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: SampleDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun providePostDao(db: SampleDb): PostDao {
        return db.postDao()
    }

    @Singleton
    @Provides
    fun provideCommentDao(db: SampleDb): CommentDao {
        return db.commentDao()
    }

    @Singleton
    @Provides
    fun provideLikeDao(db: SampleDb): LikeDao {
        return db.likeDao()
    }

    @Singleton
    @Provides
    fun provideFeedDao(db: SampleDb): FeedDao {
        return db.feedDao()
    }
}
