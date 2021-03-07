package com.emreeran.android.roomsample.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Entity(tableName = "comments", foreignKeys = [
    (ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])),
    (ForeignKey(entity = Post::class, parentColumns = ["id"], childColumns = ["postId"]))
], indices = [
    (Index(value = ["userId"], name = "CommentUserIndex")),
    (Index(value = ["postId"], name = "CommentPostIndex"))
])
data class Comment(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val userId: Int,
        val postId: Int,
        val content: String,
        val createdAt: Date = Date()
)