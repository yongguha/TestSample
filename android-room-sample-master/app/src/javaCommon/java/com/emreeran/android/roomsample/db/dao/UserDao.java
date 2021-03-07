package com.emreeran.android.roomsample.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.emreeran.android.roomsample.db.entity.User;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Query("SELECT * FROM users")
    Single<List<User>> list();

    @Query("DELETE FROM users")
    void purge();
}
