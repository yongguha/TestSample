package com.emreeran.android.roomsample.db.vo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.emreeran.android.roomsample.db.entity.Comment
import com.emreeran.android.roomsample.db.entity.Like
import com.emreeran.android.roomsample.db.entity.Post
import com.emreeran.android.roomsample.db.entity.User

/**
 * Created by Emre Eran on 26.05.2018.
 */
data class PostWithUserCommentsLikes(
        @Embedded val post: Post,
        @Embedded(prefix = "user_") val user: User
) {
    @Relation(parentColumn = "id", entityColumn = "postId", entity = Comment::class)
    var comments: List<Comment>? = null

    @Relation(parentColumn = "id", entityColumn = "postId", entity = Like::class)
    var likes: List<Like>? = null
}