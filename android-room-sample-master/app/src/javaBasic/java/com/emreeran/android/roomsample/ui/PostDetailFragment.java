package com.emreeran.android.roomsample.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.dao.CommentDao;
import com.emreeran.android.roomsample.db.dao.LikeDao;
import com.emreeran.android.roomsample.db.dao.PostDao;
import com.emreeran.android.roomsample.db.entity.Like;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emre Eran on 11.05.2018.
 */
public class PostDetailFragment extends Fragment {

    private static final String ARGS_POST_ID = "postId";

    private CompositeDisposable mDisposables;

    private boolean mIsLiked;
    private int mLikeCount;
    private int mCurrentUserId;

    public static PostDetailFragment create(int postId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_detail_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        Context context = getContext();
        Bundle arguments = getArguments();

        if (context == null || view == null || arguments == null) {
            return;
        }

        SharedPreferences prefs =
                context.getSharedPreferences("android-room-sample", Context.MODE_PRIVATE);
        mCurrentUserId = prefs.getInt("logged_in_as", -1);

        mDisposables = new CompositeDisposable();
        int postId = arguments.getInt(ARGS_POST_ID);

        SampleDb db = SampleDb.getInstance(getContext());
        PostDao postDao = db.postDao();
        CommentDao commentDao = db.commentDao();
        LikeDao likeDao = db.likeDao();

        CommentAdapter commentAdapter = new CommentAdapter();
        RecyclerView commentList = view.findViewById(R.id.comment_list);
        commentList.setAdapter(commentAdapter);

        TextView nameTextView = view.findViewById(R.id.name);
        TextView contentTextView = view.findViewById(R.id.content);
        TextView commentCountTextView = view.findViewById(R.id.comment_count);
        ImageView userImageView = view.findViewById(R.id.user_image);
        ImageButton likeButton = view.findViewById(R.id.like_button);
        ImageButton commentButton = view.findViewById(R.id.comment_button);

        mDisposables.add(Single.fromCallable(() -> postDao.findByIdWithUser(postId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postWithUser -> {
                    nameTextView.setText(postWithUser.user.name);
                    contentTextView.setText(postWithUser.post.content);
                    Glide.with(view)
                            .load(postWithUser.user.image)
                            .apply(new RequestOptions().circleCrop())
                            .into(userImageView);

                    mDisposables.add(commentDao.listByPostId(postWithUser.post.id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(comments -> {
                                StringBuilder commentCountText = new StringBuilder(Integer.toString(comments.size()));
                                if (comments.size() > 1 || comments.size() == 0) {
                                    commentCountText.append(" comments");
                                } else {
                                    commentCountText.append(" comment");
                                }
                                commentCountTextView.setText(commentCountText);
                                commentAdapter.replace(comments);
                            })
                    );

                    mDisposables.add(likeDao.countByPostId(postWithUser.post.id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(count -> {
                                mLikeCount = count;
                                setLikeCountText();
                            })
                    );

                    mDisposables.add(likeDao.checkIfLikedByUser(mCurrentUserId, postWithUser.post.id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(isLiked -> {
                                mIsLiked = isLiked;
                                if (mIsLiked) {
                                    likeButton.setImageResource(R.drawable.ic_heart);
                                }
                            })
                    );

                    likeButton.setOnClickListener(v -> likeButtonPressed(postWithUser.post.id));
                })
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }

    private void likeButtonPressed(int postId) {
        View view = getView();
        if (view != null) {
            LikeDao likeDao = SampleDb.getInstance(getContext()).likeDao();
            ImageButton likeButton = view.findViewById(R.id.like_button);

            if (mIsLiked) {
                mDisposables.add(Completable.fromCallable((Callable<Void>) () -> {
                            likeDao.deleteByUserIdAndPostId(mCurrentUserId, postId);
                            return null;
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    mIsLiked = !mIsLiked;
                                    mLikeCount--;
                                    setLikeCountText();
                                    likeButton.setImageResource(R.drawable.ic_heart_outline);
                                })
                );
            } else {
                Like like = new Like(mCurrentUserId, postId);
                mDisposables.add(Completable.fromCallable((Callable<Void>) () -> {
                            likeDao.insert(like);
                            return null;
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    mIsLiked = !mIsLiked;
                                    mLikeCount++;
                                    setLikeCountText();
                                    likeButton.setImageResource(R.drawable.ic_heart);
                                })
                );
            }
        }
    }

    private void setLikeCountText() {
        View view = getView();
        if (view == null) {
            return;
        }

        TextView likeCountTextView = view.findViewById(R.id.like_count);
        StringBuilder likeCountText = new StringBuilder(Integer.toString(mLikeCount));
        if (mLikeCount > 1 || mLikeCount == 0) {
            likeCountText.append(" likes");
        } else {
            likeCountText.append(" like");
        }
        likeCountTextView.setText(likeCountText);
    }
}
