package com.emreeran.android.roomsample.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.db.vo.FeedItem

/**
 * Created by Emre Eran on 28.05.2018.
 */
class FeedItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun setItem(item: FeedItem) {
        val nameTextView = itemView.findViewById<TextView>(R.id.name)
        val contentTextView = itemView.findViewById<TextView>(R.id.content)
        val likeCountTextView = itemView.findViewById<TextView>(R.id.like_count)
        val commentCountTextView = itemView.findViewById<TextView>(R.id.comment_count)
        val userImageView = itemView.findViewById<ImageView>(R.id.user_image)
        val likeButton = itemView.findViewById<ImageButton>(R.id.like_button)
        val commentButton = itemView.findViewById<ImageButton>(R.id.comment_button)

        nameTextView.text = item.postWithUser.user.name
        contentTextView.text = item.postWithUser.post.content

        val likeCount = item.likes.size
        val commentCount = item.comments.size
        val likeCountText = itemView.resources.getString(R.string.like_count, likeCount)
        val commentCountText = itemView.resources.getString(R.string.comment_count, commentCount)
        likeCountTextView.text = likeCountText
        commentCountTextView.text = commentCountText

        Glide.with(itemView)
                .load(item.postWithUser.user.image)
                .apply(RequestOptions().circleCrop())
                .into(userImageView)

        likeButton.setOnClickListener { v ->
            val isLiked = v.tag != null && v.tag as Boolean
            if (isLiked) {
                (v as ImageButton).setImageResource(R.drawable.ic_heart_outline)
            } else {
                (v as ImageButton).setImageResource(R.drawable.ic_heart)
            }
            v.setTag(!isLiked)
        }
    }
}