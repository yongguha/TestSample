package com.emreeran.android.roomsample.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Entity(tableName = "users")
data class User(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val name: String,
        val image: String? = null,
        val createdAt: Date = Date()
)
