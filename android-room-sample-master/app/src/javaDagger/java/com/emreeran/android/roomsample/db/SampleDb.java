package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

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
 * Created by Emre Eran on 26.05.2018.
 */
@Database(
        entities = {
                User.class,
                Post.class,
                Comment.class,
                Like.class
        },
        version = 1
)
@TypeConverters(Converters.class)
public abstract class SampleDb extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract PostDao postDao();

    public abstract CommentDao commentDao();

    public abstract LikeDao likeDao();

    public abstract FeedDao feedDao();
}
