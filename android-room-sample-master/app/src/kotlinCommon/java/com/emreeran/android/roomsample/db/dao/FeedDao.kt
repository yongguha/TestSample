package com.emreeran.android.roomsample.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.emreeran.android.roomsample.db.vo.CommentWithUser
import com.emreeran.android.roomsample.db.vo.FeedItem
import com.emreeran.android.roomsample.db.vo.LikeWithUser
import com.emreeran.android.roomsample.db.vo.PostWithUser
import java.util.*

/**
 * Created by Emre Eran on 26.05.2018.
 */
@Dao
abstract class FeedDao {
    @Query("SELECT c.*, " +
            "u.id as user_id, u.name as user_name, u.image as user_image, u.createdAt as user_createdAt " +
            "FROM comments as c JOIN users as u on u.id = userId WHERE postId = :postId")
    abstract fun listCommentsWithUserSync(postId: Int): List<CommentWithUser>

    @Query("SELECT l.*, " +
            "u.id as user_id, u.name as user_name, u.image as user_image, u.createdAt as user_createdAt " +
            "FROM likes as l JOIN users as u on u.id = userId WHERE postId = :postId")
    abstract fun listLikeWithUserSync(postId: Int): List<LikeWithUser>

    @Query("SELECT p.*, " +
            "u.id as user_id, u.name as user_name, u.image as user_image, u.createdAt as user_createdAt " +
            "FROM posts as p JOIN users as u on u.id = p.userId ORDER BY p.createdAt DESC")
    abstract fun listPostWithUserSync(): List<PostWithUser>

    @Transaction
    open fun listFeedItems(): List<FeedItem> {
        val feedItems = ArrayList<FeedItem>()
        val posts = listPostWithUserSync()
        for (postWithUser in posts) {
            val comments = listCommentsWithUserSync(postWithUser.post.id)
            val likes = listLikeWithUserSync(postWithUser.post.id)
            val feedItem = FeedItem(postWithUser, comments, likes)
            feedItems.add(feedItem)
        }

        return feedItems
    }
}