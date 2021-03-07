package com.emreeran.android.roomsample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.ui.common.ListAdapter
import com.emreeran.android.roomsample.db.vo.CommentWithUser

/**
 * Created by Emre Eran on 28.05.2018.
 */
class CommentAdapter : ListAdapter<CommentWithUser, CommentItemViewHolder>() {
    override fun areItemsTheSame(oldItem: CommentWithUser, newItem: CommentWithUser): Boolean {
        return oldItem.comment.id == newItem.comment.id
    }

    override fun areContentsTheSame(oldItem: CommentWithUser, newItem: CommentWithUser): Boolean {
        return oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentItemViewHolder, position: Int) {
        items?.let {
            holder.setItem(it[position])
        }
    }
}