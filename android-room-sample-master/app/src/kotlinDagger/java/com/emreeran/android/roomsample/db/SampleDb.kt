package com.emreeran.android.roomsample.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.emreeran.android.roomsample.db.dao.*
import com.emreeran.android.roomsample.db.entity.Comment
import com.emreeran.android.roomsample.db.entity.Like
import com.emreeran.android.roomsample.db.entity.Post
import com.emreeran.android.roomsample.db.entity.User

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Database(
        entities = [
            User::class,
            Post::class,
            Comment::class,
            Like::class
        ],
        version = 1
)
@TypeConverters(Converters::class)
abstract class SampleDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun likeDao(): LikeDao
    abstract fun feedDao(): FeedDao
}
