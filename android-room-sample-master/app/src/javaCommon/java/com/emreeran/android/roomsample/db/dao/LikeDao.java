package com.emreeran.android.roomsample.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.emreeran.android.roomsample.db.entity.Like;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Dao
public interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Like like);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Like> likes);

    @Query("DELETE FROM likes WHERE user_id = :userId AND post_id = :postId")
    void deleteByUserIdAndPostId(int userId, int postId);

    @Query("SELECT * FROM likes")
    List<Like> list();

    @Query("SELECT COUNT(*) FROM likes WHERE post_id = :postId")
    Single<Integer> countByPostId(int postId);

    @Query("SELECT COUNT(*) FROM likes WHERE user_id = :userId AND post_id = :postId")
    Single<Boolean> checkIfLikedByUser(int userId, int postId);

    @Query("DELETE FROM likes")
    void purge();
}
