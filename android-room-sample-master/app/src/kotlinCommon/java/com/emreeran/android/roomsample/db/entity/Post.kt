package com.emreeran.android.roomsample.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Entity(tableName = "posts", foreignKeys = [
    (ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"]))
], indices = [(Index(value = ["userId"], name = "PostUserIndex"))])
data class Post(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val userId: Int,
        val content: String,
        val createdAt: Date = Date()
)