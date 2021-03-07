package com.emreeran.android.roomsample.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Entity(tableName = "likes",
        foreignKeys = [
            (ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = CASCADE)),
            (ForeignKey(entity = Post::class, parentColumns = ["id"], childColumns = ["postId"]))],
        indices = [
            (Index(value = ["postId"], name = "LikePostIndex")),
            (Index(value = ["userId", "postId"], name = "UserPostIndex", unique = true))
        ]
)
data class Like(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val userId: Int,
        val postId: Int,
        val createdAt: Date = Date()
)