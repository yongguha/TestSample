package com.emreeran.android.roomsample.db.vo

/**
 * Created by Emre Eran on 26.05.2018.
 */
data class FeedItem(
        val postWithUser: PostWithUser,
        val comments: List<CommentWithUser>,
        val likes: List<LikeWithUser>
)