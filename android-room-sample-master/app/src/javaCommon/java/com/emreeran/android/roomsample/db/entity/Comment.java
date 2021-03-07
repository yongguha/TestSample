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
        tableName = "comments",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"),
                @ForeignKey(entity = Post.class, parentColumns = "id", childColumns = "postId")
        },
        indices = {
                @Index(value = "userId", name = "CommentUserIndex"),
                @Index(value = "postId", name = "CommentPostIndex")
        }
)
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final int userId;
    public final int postId;
    public final String content;
    public final Date createdAt;

    @Ignore
    public Comment(int userId, int postId, String content) {
        this.id = 0;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.createdAt = new Date();
    }

    public Comment(int id, int userId, int postId, String content, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id &&
                userId == comment.userId &&
                postId == comment.postId &&
                Objects.equals(content, comment.content) &&
                Objects.equals(createdAt, comment.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, postId, content, createdAt);
    }
}
