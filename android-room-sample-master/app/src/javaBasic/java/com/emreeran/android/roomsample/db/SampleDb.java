package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.emreeran.android.roomsample.db.dao.CommentDao;
import com.emreeran.android.roomsample.db.dao.FeedDao;
import com.emreeran.android.roomsample.db.dao.LikeDao;
import com.emreeran.android.roomsample.db.dao.PostDao;
import com.emreeran.android.roomsample.db.dao.UserDao;
import com.emreeran.android.roomsample.db.entity.Comment;
import com.emreeran.android.roomsample.db.entity.Like;
import com.emreeran.android.roomsample.db.entity.Post;
import com.emreeran.android.roomsample.db.entity.User;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Database(version = 1, entities = {User.class, Post.class, Comment.class, Like.class})
@TypeConverters(Converters.class)
abstract public class SampleDb extends RoomDatabase {
    private static volatile SampleDb INSTANCE;

    public static SampleDb getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SampleDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, SampleDb.class, "sample.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    abstract public UserDao userDao();

    abstract public PostDao postDao();

    abstract public CommentDao commentDao();

    abstract public LikeDao likeDao();

    abstract public FeedDao feedDao();
}
