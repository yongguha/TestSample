package com.emreeran.android.roomsample.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Entity(
        tableName = "posts",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId")
        },
        indices = {@Index(value = "userId", name = "PostUserIndex")}
)
public class Post {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final int userId;
    public final String content;
    public final Date createdAt;

    @Ignore
    public Post(int userId, String content) {
        this.id = 0;
        this.userId = userId;
        this.content = content;
        this.createdAt = new Date();
    }

    public Post(int id, int userId, String content, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id &&
                userId == post.userId &&
                Objects.equals(content, post.content) &&
                Objects.equals(createdAt, post.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, content, createdAt);
    }
}
