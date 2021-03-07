package com.emreeran.android.roomsample.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.dao.FeedDao;
import com.emreeran.android.roomsample.db.dao.LikeDao;
import com.emreeran.android.roomsample.db.entity.Like;
import com.emreeran.android.roomsample.di.Injectable;

import java.util.Objects;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emre Eran on 26.05.2018.
 */
public class FeedFragment extends Fragment implements Injectable {
    public static FeedFragment create() {
        return new FeedFragment();
    }

    @Inject
    FeedDao mFeedDao;

    @Inject
    LikeDao mLikeDao;

    @Inject
    NavigationController mNavigationController;

    private CompositeDisposable mDisposables;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getContext();
        if (context == null) {
            return;
        }

        mDisposables = new CompositeDisposable();
        SharedPreferences prefs =
                context.getSharedPreferences("android-room-sample", Context.MODE_PRIVATE);
        int currentUserId = prefs.getInt("logged_in_as", -1);

        RecyclerView feedView = Objects.requireNonNull(getView()).findViewById(R.id.feed);

        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        Drawable divider = getResources().getDrawable(R.drawable.feed_divider);
        itemDecoration.setDrawable(divider);
        feedView.addItemDecoration(itemDecoration);

        FeedAdapter adapter = new FeedAdapter();
        mDisposables.add(adapter.getItemClickEvent()
                .subscribe(postId -> mNavigationController.navigateToPostDetail(postId)));
        mDisposables.add(adapter.getLikeClickEvent().subscribe(pair -> {
                    int postId = pair.first;
                    boolean isLiked = pair.second;

                    if (isLiked) {
                        Like like = new Like(currentUserId, postId);
                        mDisposables.add(Completable.fromCallable((Callable<Void>) () -> {
                                    mLikeDao.insert(like);
                                    return null;
                                }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe()
                        );
                    } else {
                        mDisposables.add(Completable.fromCallable((Callable<Void>) () -> {
                                    mLikeDao.deleteByUserIdAndPostId(currentUserId, postId);
                                    return null;
                                }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe()
                        );
                    }
                })
        );

        feedView.setAdapter(adapter);

        mDisposables.add(
                Flowable.fromCallable(() -> mFeedDao.listFeedItems(currentUserId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adapter::replace)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }
}
