package com.emreeran.android.roomsample.db.vo;

import android.arch.persistence.room.Embedded;

import com.emreeran.android.roomsample.db.entity.User;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Emre Eran on 25.04.2018.
 */
public class LikeWithUser {
    public final int id;
    public final Date createdAt;
    @Embedded(prefix = "user_")
    public final User user;
    public boolean isLiked;


    public LikeWithUser(int id, Date createdAt, User user, boolean isLiked) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
        this.isLiked = isLiked;
    }

    @Override
    public String toString() {
        return "LikeWithUser{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", isLiked=" + isLiked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeWithUser that = (LikeWithUser) o;
        return id == that.id &&
                isLiked == that.isLiked &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, user, isLiked);
    }
}
