package com.emreeran.android.roomsample.db.vo;

import android.arch.persistence.room.Embedded;

import com.emreeran.android.roomsample.db.entity.Comment;
import com.emreeran.android.roomsample.db.entity.User;

import java.util.Objects;

/**
 * Created by Emre Eran on 25.04.2018.
 */
public class CommentWithUser {
    @Embedded
    public final Comment comment;

    @Embedded(prefix = "user_")
    public final User user;

    public CommentWithUser(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    @Override
    public String toString() {
        return "CommentWithUser{" +
                "comment=" + comment +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentWithUser that = (CommentWithUser) o;
        return Objects.equals(comment, that.comment) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, user);
    }
}
