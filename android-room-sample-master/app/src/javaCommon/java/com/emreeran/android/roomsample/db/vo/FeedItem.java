package com.emreeran.android.roomsample.db.vo;

import java.util.List;
import java.util.Objects;

/**
 * Created by Emre Eran on 9.05.2018.
 */
public class FeedItem {
    public final PostWithUser postWithUser;
    public final List<CommentWithUser> comments;
    public final List<LikeWithUser> likes;

    public FeedItem(PostWithUser postWithUser, List<CommentWithUser> comments, List<LikeWithUser> likes) {
        this.postWithUser = postWithUser;
        this.comments = comments;
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "FeedItemViewHolder{" +
                "postWithUser=" + postWithUser +
                ", comments=" + comments +
                ", likes=" + likes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedItem feedItem = (FeedItem) o;
        return Objects.equals(postWithUser, feedItem.postWithUser) &&
                Objects.equals(comments, feedItem.comments) &&
                Objects.equals(likes, feedItem.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postWithUser, comments, likes);
    }
}
