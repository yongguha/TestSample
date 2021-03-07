package com.emreeran.android.roomsample.db.vo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.emreeran.android.roomsample.db.entity.Comment;
import com.emreeran.android.roomsample.db.entity.Like;
import com.emreeran.android.roomsample.db.entity.Post;
import com.emreeran.android.roomsample.db.entity.User;

import java.util.List;

/**
 * Created by Emre Eran on 25.04.2018.
 */
public class PostWithUserCommentsLikes {
    @Embedded
    public final Post post;

    @Embedded(prefix = "user_")
    public final User user;

    @Relation(parentColumn = "id", entityColumn = "postId", entity = Comment.class)
    public List<Comment> comments;

    @Relation(parentColumn = "id", entityColumn = "post_id", entity = Like.class)
    public List<Like> likes;

    public PostWithUserCommentsLikes(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    @Override
    public String toString() {
        return "PostWithUserCommentsLikes{" +
                "postWithUser=" + post.toString() +
                ", user=" + user.toString() +
                ", comments=" + comments +
                ", likes=" + likes +
                '}';
    }
}
