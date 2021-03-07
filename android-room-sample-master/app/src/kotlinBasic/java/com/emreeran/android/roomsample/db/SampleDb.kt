package com.emreeran.android.roomsample.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.emreeran.android.roomsample.db.dao.*
import com.emreeran.android.roomsample.db.entity.Comment
import com.emreeran.android.roomsample.db.entity.Like
import com.emreeran.android.roomsample.db.entity.Post
import com.emreeran.android.roomsample.db.entity.User

/**
 * Created by Emre Eran on 26.05.2018.
 */
@Database(version = 1, entities = [(User::class), (Post::class), (Comment::class), (Like::class)])
@TypeConverters(Converters::class)
abstract class SampleDb : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: SampleDb? = null

        fun getInstance(context: Context): SampleDb? {
            if (instance == null) {
                synchronized(SampleDb::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder<SampleDb>(context, SampleDb::class.java, "sample.db")
                                .build()
                    }
                }
            }
            return instance
        }
    }

    abstract fun userDao(): UserDao

    abstract fun postDao(): PostDao

    abstract fun commentDao(): CommentDao

    abstract fun likeDao(): LikeDao

    abstract fun feedDao(): FeedDao
}