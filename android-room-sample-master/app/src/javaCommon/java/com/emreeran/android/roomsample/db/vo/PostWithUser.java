package com.emreeran.android.roomsample.db.vo;

import android.arch.persistence.room.Embedded;

import com.emreeran.android.roomsample.db.entity.Post;
import com.emreeran.android.roomsample.db.entity.User;

import java.util.Objects;

/**
 * Created by Emre Eran on 9.05.2018.
 */
public class PostWithUser {
    @Embedded
    public final Post post;

    @Embedded(prefix = "user_")
    public final User user;

    public PostWithUser(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    @Override
    public String toString() {
        return "PostWithUser{" +
                "postWithUser=" + post +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostWithUser that = (PostWithUser) o;
        return Objects.equals(post, that.post) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user);
    }
}
