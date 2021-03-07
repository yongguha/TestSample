package com.emreeran.android.roomsample.db.vo

import android.arch.persistence.room.Embedded
import com.emreeran.android.roomsample.db.entity.Comment
import com.emreeran.android.roomsample.db.entity.User

/**
 * Created by Emre Eran on 25.04.2018.
 */
data class CommentWithUser(
        @Embedded val comment: Comment,
        @Embedded(prefix = "user_") val user: User
)