package com.emreeran.android.roomsample.ui;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.ui.CommentItemViewHolder;
import com.emreeran.android.roomsample.ui.common.ListAdapter;
import com.emreeran.android.roomsample.db.vo.CommentWithUser;

/**
 * Created by Emre Eran on 26.05.2018.
 */
public class CommentAdapter extends ListAdapter<CommentWithUser, CommentItemViewHolder> {
    CommentAdapter() {
    }

    @Override
    protected boolean areItemsTheSame(CommentWithUser oldItem, CommentWithUser newItem) {
        return oldItem.comment.id == newItem.comment.id;
    }

    @Override
    protected boolean areContentsTheSame(CommentWithUser oldItem, CommentWithUser newItem) {
        return oldItem.equals(newItem);
    }

    @NonNull
    @Override
    public CommentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentItemViewHolder holder, int position) {
        holder.setItem(mItems.get(position));
    }
}
