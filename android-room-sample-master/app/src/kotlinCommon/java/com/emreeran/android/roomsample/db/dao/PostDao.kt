package com.emreeran.android.roomsample.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.emreeran.android.roomsample.db.entity.Post
import com.emreeran.android.roomsample.db.vo.PostWithUser
import com.emreeran.android.roomsample.db.vo.PostWithUserCommentsLikes
import io.reactivex.Single

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertAll(posts: List<Post>)

    @Query("SELECT p.*, " +
            "u.id as user_id, u.name as user_name, u.createdAt as user_createdAt, u.image as user_image " +
            "FROM posts as p JOIN users as u on p.userId = u.id WHERE p.id = :id ")
    abstract fun findByIdWithUser(id: Int): PostWithUser

    @Query("SELECT * FROM posts")
    abstract fun list(): Single<List<Post>>

    @Query("DELETE FROM posts")
    abstract fun purge()

    @Transaction
    @Query("SELECT p.*, " +
            "c.id, c.content, c.userId, c.createdAt, " +
            "l.id, l.userId, l.createdAt, " +
            "p_u.id as user_id, p_u.name as user_name, p_u.createdAt as user_createdAt " +
            "FROM posts as p " +
            "LEFT JOIN comments as c on p.id = c.postId " +
            "LEFT JOIN likes as l on p.id = l.postId " +
            "LEFT JOIN users as p_u on p.userId = p_u.id " +
            "GROUP BY p.id ORDER BY p.createdAt DESC")
    abstract fun listWithUserCommentsLikes(): LiveData<List<PostWithUserCommentsLikes>>
}
