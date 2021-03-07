package com.emreeran.android.roomsample.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.emreeran.android.roomsample.db.entity.Like
import io.reactivex.Single

/**
 * Created by Emre Eran on 20.04.2018.
 */
@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(like: Like)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(likes: List<Like>)

    @Query("SELECT * FROM likes")
    fun list(): Single<List<Like>>

    @Query("DELETE FROM likes")
    fun purge()
}
