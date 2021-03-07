package com.emreeran.android.roomsample.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.vo.FeedItem;
import com.emreeran.android.roomsample.db.vo.LikeWithUser;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by Emre Eran on 9.05.2018.
 */
public class FeedItemViewHolder extends RecyclerView.ViewHolder {

    private TextView mLikeCountTextView;

    private int mLikeCount;

    FeedItemViewHolder(View itemView) {
        super(itemView);
    }

    public void setItem(FeedItem item,
                        PublishSubject<Integer> itemClickSubject,
                        PublishSubject<Pair<Integer, Boolean>> likeClickSubject
    ) {
        mLikeCountTextView = itemView.findViewById(R.id.like_count);

        TextView nameTextView = itemView.findViewById(R.id.name);
        TextView contentTextView = itemView.findViewById(R.id.content);
        TextView commentCountTextView = itemView.findViewById(R.id.comment_count);
        ImageView userImageView = itemView.findViewById(R.id.user_image);
        ImageButton likeButton = itemView.findViewById(R.id.like_button);
        ImageButton commentButton = itemView.findViewById(R.id.comment_button);

        nameTextView.setText(item.postWithUser.user.name);
        contentTextView.setText(item.postWithUser.post.content);

        mLikeCount = item.likes.size();
        setLikeCountText();

        int commentCount = item.comments.size();
        String commentCountText = itemView.getResources().getString(R.string.comment_count, commentCount);

        commentCountTextView.setText(commentCountText);
        likeButton.setImageResource(R.drawable.ic_heart_outline);


        for (LikeWithUser likeWithUser : item.likes) {
            if (likeWithUser.isLiked) {
                likeButton.setTag(true);
                likeButton.setImageResource(R.drawable.ic_heart);
                break;
            }
        }

        Glide.with(itemView)
                .load(item.postWithUser.user.image)
                .apply(new RequestOptions().circleCrop())
                .into(userImageView);

        itemView.setOnClickListener(v -> itemClickSubject.onNext(item.postWithUser.post.id));

        likeButton.setOnClickListener(v -> {
            boolean isLiked = v.getTag() != null && (boolean) v.getTag();
            likeClickSubject.onNext(Pair.create(item.postWithUser.post.id, !isLiked));

            if (isLiked) {
                ((ImageButton) v).setImageResource(R.drawable.ic_heart_outline);
                mLikeCount--;
            } else {
                ((ImageButton) v).setImageResource(R.drawable.ic_heart);
                mLikeCount++;
            }
            setLikeCountText();
            v.setTag(!isLiked);
        });
    }

    private void setLikeCountText() {
        String likeCountText = itemView.getResources().getString(R.string.like_count, mLikeCount);
        mLikeCountTextView.setText(likeCountText);
    }
}
