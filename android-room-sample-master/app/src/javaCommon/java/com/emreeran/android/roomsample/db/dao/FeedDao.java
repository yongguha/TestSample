package com.emreeran.android.roomsample.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.emreeran.android.roomsample.db.vo.CommentWithUser;
import com.emreeran.android.roomsample.db.vo.FeedItem;
import com.emreeran.android.roomsample.db.vo.LikeWithUser;
import com.emreeran.android.roomsample.db.vo.PostWithUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emre Eran on 9.05.2018.
 */
@Dao
public abstract class FeedDao {
    @Query("SELECT c.*, " +
            "u.id as user_id, u.name as user_name, u.image as user_image, u.createdAt as user_createdAt " +
            "FROM comments as c JOIN users as u on u.id = userId WHERE postId = :postId")
    abstract List<CommentWithUser> listCommentsWithUserSync(int postId);

    @Query("SELECT l.*, " +
            "u.id as user_id, u.name as user_name, u.image as user_image, u.createdAt as user_createdAt, " +
            "CAST(CASE WHEN u.id = :userId THEN 1 ELSE 0 END as bit) as isLiked " +
            "FROM likes as l JOIN users as u on u.id = user_id WHERE post_id = :postId")
    abstract List<LikeWithUser> listLikeWithUserSync(int postId, int userId);

    @Query("SELECT p.*, " +
            "u.id as user_id, u.name as user_name, u.image as user_image, u.createdAt as user_createdAt " +
            "FROM posts as p JOIN users as u on u.id = p.userId ORDER BY p.createdAt DESC")
    abstract List<PostWithUser> listPostWithUserSync();

    @Transaction
    public List<FeedItem> listFeedItems(int userId) {
        ArrayList<FeedItem> feedItems = new ArrayList<>();
        List<PostWithUser> posts = listPostWithUserSync();
        for (PostWithUser postWithUser : posts) {
            List<CommentWithUser> comments = listCommentsWithUserSync(postWithUser.post.id);
            List<LikeWithUser> likes = listLikeWithUserSync(postWithUser.post.id, userId);
            FeedItem feedItem = new FeedItem(postWithUser, comments, likes);
            feedItems.add(feedItem);
        }

        return feedItems;
    }
}
