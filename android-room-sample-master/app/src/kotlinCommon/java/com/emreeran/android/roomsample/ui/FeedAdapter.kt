package com.emreeran.android.roomsample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.ui.common.ListAdapter
import com.emreeran.android.roomsample.db.vo.FeedItem

/**
 * Created by Emre Eran on 28.05.2018.
 */
class FeedAdapter : ListAdapter<FeedItem, FeedItemViewHolder>() {
    private var onItemClickListener: View.OnClickListener? = null

    fun setOnItemClickListener(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem.postWithUser.post.id == newItem.postWithUser.post.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        view.setOnClickListener(onItemClickListener)
        return FeedItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        items?.let {
            holder.setItem(it[position])
        }
    }
}