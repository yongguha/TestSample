package com.emreeran.android.roomsample.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Entity(
        tableName = "likes",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Post.class, parentColumns = "id", childColumns = "post_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "post_id", name = "LikeUserIndex"),
                @Index(value = {"user_id", "post_id"}, name = "UserPostIndex", unique = true)
        }
)
public class Like {
    @PrimaryKey(autoGenerate = true)
    public final int id;

    @ColumnInfo(name = "user_id", index = true)
    public final int userId;

    @ColumnInfo(name = "post_id")
    public final int postId;

    @ColumnInfo(name = "created_at")
    public final Date createdAt;

    @Ignore
    public Like(int userId, int postId) {
        this.id = 0;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = new Date();
    }

    public Like(int id, int userId, int postId, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                ", createdAt=" + createdAt +
                '}';
    }
}
