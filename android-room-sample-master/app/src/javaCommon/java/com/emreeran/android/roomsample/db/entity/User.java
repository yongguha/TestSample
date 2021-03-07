package com.emreeran.android.roomsample.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final String name;
    @Nullable
    public final String image;
    public final Date createdAt;

    public User(int id, String name, @Nullable String image, Date createdAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.createdAt = createdAt;
    }

    @Ignore
    public User(String name, @Nullable String image) {
        this(0, name, image, new Date());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(image, user.image) &&
                Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, createdAt);
    }
}
