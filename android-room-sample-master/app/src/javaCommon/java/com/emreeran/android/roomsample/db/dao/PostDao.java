package com.emreeran.android.roomsample.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.emreeran.android.roomsample.db.entity.Post;
import com.emreeran.android.roomsample.db.vo.PostWithUser;
import com.emreeran.android.roomsample.db.vo.PostWithUserCommentsLikes;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Post post);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Post> posts);

    @Query("SELECT p.*, " +
            "u.id as user_id, u.name as user_name, u.createdAt as user_createdAt, u.image as user_image " +
            "FROM posts as p JOIN users as u on p.userId = u.id WHERE p.id = :id ")
    PostWithUser findByIdWithUser(int id);

    @Query("SELECT * FROM posts")
    Single<List<Post>> list();

    @Query("DELETE FROM posts")
    void purge();

    @Transaction
    @Query("SELECT p.*, " +
            "c.id, c.content, c.userId, c.createdAt, " +
            "l.id, l.user_id, l.created_at, " +
            "p_u.id as user_id, p_u.name as user_name, p_u.createdAt as user_createdAt " +
            "FROM posts as p " +
            "LEFT JOIN comments as c on p.id = c.postId " +
            "LEFT JOIN likes as l on p.id = l.post_id " +
            "LEFT JOIN users as p_u on p.userId = p_u.id " +
            "GROUP BY p.id ORDER BY p.createdAt DESC")
    LiveData<List<PostWithUserCommentsLikes>> listWithUserCommentsLikes();
}
