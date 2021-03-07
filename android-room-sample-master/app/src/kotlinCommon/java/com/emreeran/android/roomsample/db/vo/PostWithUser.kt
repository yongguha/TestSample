package com.emreeran.android.roomsample.db.vo

import android.arch.persistence.room.Embedded
import com.emreeran.android.roomsample.db.entity.Post
import com.emreeran.android.roomsample.db.entity.User

/**
 * Created by Emre Eran on 26.05.2018.
 */
data class PostWithUser(
        @Embedded val post: Post,
        @Embedded(prefix = "user_") val user: User
)