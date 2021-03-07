package com.emreeran.android.roomsample.ui;

import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.vo.FeedItem;
import com.emreeran.android.roomsample.ui.common.ListAdapter;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Emre Eran on 9.05.2018.
 */
public class FeedAdapter extends ListAdapter<FeedItem, FeedItemViewHolder> {

    private PublishSubject<Integer> mItemClickSubject = PublishSubject.create();
    private PublishSubject<Pair<Integer, Boolean>> mLikeClickSubject = PublishSubject.create();
    private Observable<Integer> mItemClickEvent = mItemClickSubject;
    private Observable<Pair<Integer, Boolean>> mLikeClickEvent = mLikeClickSubject;

    FeedAdapter() {
    }

    public Observable<Integer> getItemClickEvent() {
        return mItemClickEvent;
    }

    public Observable<Pair<Integer, Boolean>> getLikeClickEvent() {
        return mLikeClickEvent;
    }

    @NonNull
    @Override
    public FeedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeedItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.feed_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemViewHolder holder, int position) {
        holder.setItem(mItems.get(position), mItemClickSubject, mLikeClickSubject);
    }

    @Override
    protected boolean areItemsTheSame(FeedItem oldItem, FeedItem newItem) {
        return oldItem.postWithUser.post.id == newItem.postWithUser.post.id;
    }

    @Override
    protected boolean areContentsTheSame(FeedItem oldItem, FeedItem newItem) {
        return oldItem.equals(newItem);
    }
}
