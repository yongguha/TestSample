package com.emreeran.android.roomsample.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.db.vo.CommentWithUser

/**
 * Created by Emre Eran on 28.05.2018.
 */
class CommentItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    fun setItem(commentWithUser: CommentWithUser?) {
        if (commentWithUser != null) {
            val nameTextView = itemView.findViewById<TextView>(R.id.user_name)
            val commentTextView = itemView.findViewById<TextView>(R.id.comment)
            val userImageView = itemView.findViewById<ImageView>(R.id.user_image)

            nameTextView.text = commentWithUser.user.name
            commentTextView.text = commentWithUser.comment.content

            Glide.with(itemView)
                    .load(commentWithUser.user.image)
                    .apply(RequestOptions().circleCrop())
                    .into(userImageView)
        }
    }
}