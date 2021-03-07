package com.emreeran.android.roomsample.db.vo

import android.arch.persistence.room.Embedded
import com.emreeran.android.roomsample.db.entity.Like
import com.emreeran.android.roomsample.db.entity.User

/**
 * Created by Emre Eran on 25.04.2018.
 */
data class LikeWithUser(
        @Embedded val comment: Like,
        @Embedded(prefix = "user_") val user: User
)