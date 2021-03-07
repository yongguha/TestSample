package com.emreeran.android.roomsample.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.vo.CommentWithUser;

/**
 * Created by Emre Eran on 26.05.2018.
 */
public class CommentItemViewHolder extends RecyclerView.ViewHolder {
    CommentItemViewHolder(View itemView) {
        super(itemView);
    }

    public void setItem(CommentWithUser commentWithUser) {
        if (commentWithUser != null) {
            TextView nameTextView = itemView.findViewById(R.id.user_name);
            TextView commentTextView = itemView.findViewById(R.id.comment);
            ImageView userImageView = itemView.findViewById(R.id.user_image);

            nameTextView.setText(commentWithUser.user.name);
            commentTextView.setText(commentWithUser.comment.content);

            Glide.with(itemView)
                    .load(commentWithUser.user.image)
                    .apply(new RequestOptions().circleCrop())
                    .into(userImageView);
        }
    }
}
