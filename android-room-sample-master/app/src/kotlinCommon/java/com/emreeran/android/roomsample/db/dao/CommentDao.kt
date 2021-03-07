package com.emreeran.android.roomsample.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.emreeran.android.roomsample.db.entity.Comment
import com.emreeran.android.roomsample.db.vo.CommentWithUser
import io.reactivex.Single

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(users: List<Comment>)

    @Query("SELECT * FROM comments")
    fun list(): Single<List<Comment>>

    @Query("SELECT c.*, u.id as user_id, u.name as user_name, u.image as user_image, u.createdAt as user_createdAt " + "FROM comments as c JOIN users as u on c.userId = u.id WHERE postId = :postId")
    fun listByPostId(postId: Int): Single<List<CommentWithUser>>


    @Query("DELETE FROM comments")
    fun purge()
}
