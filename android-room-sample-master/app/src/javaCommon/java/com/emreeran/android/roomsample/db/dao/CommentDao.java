package com.emreeran.android.roomsample.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.emreeran.android.roomsample.db.entity.Comment;
import com.emreeran.android.roomsample.db.vo.CommentWithUser;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Comment comment);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Comment> comments);

    @Query("SELECT * FROM comments")
    List<Comment> list();

    @Query("SELECT c.*, u.id as user_id, u.name as user_name, u.image as user_image, u.createdAt as user_createdAt " +
            "FROM comments as c JOIN users as u on c.userId = u.id WHERE postId = :postId")
    Single<List<CommentWithUser>> listByPostId(int postId);

    @Query("DELETE FROM comments")
    void purge();
}
