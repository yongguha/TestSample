package com.emreeran.android.roomsample.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.emreeran.android.roomsample.db.entity.User
import io.reactivex.Single

/**
 *  Created by Emre Eran on 20.04.2018.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(users: List<User>)

    @Query("SELECT * FROM users")
    fun list(): Single<List<User>>

    @Query("DELETE FROM users")
    fun purge()
}
