package com.emreeran.android.roomsample.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.db.dao.CommentDao
import com.emreeran.android.roomsample.db.dao.PostDao
import com.emreeran.android.roomsample.db.vo.PostWithUser
import com.emreeran.android.roomsample.di.Injectable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Emre Eran on 28.05.2018.
 */
class PostDetailFragment : Fragment(), Injectable {
    @Inject
    lateinit var postDao: PostDao

    @Inject
    lateinit var commentDao: CommentDao

    private val disposables: CompositeDisposable = CompositeDisposable()

    companion object {
        private const val argsPostId = "postId"

        fun create(postId: Int): PostDetailFragment {
            val fragment = PostDetailFragment()
            val args = Bundle()
            args.putInt(argsPostId, postId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.post_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val view = view

        if (arguments != null && view != null) {
            val postId = arguments!!.getInt(argsPostId)

            val commentAdapter = CommentAdapter()
            val commentList = view.findViewById<RecyclerView>(R.id.comment_list)
            commentList.adapter = commentAdapter

            val nameTextView = view.findViewById<TextView>(R.id.name)
            val contentTextView = view.findViewById<TextView>(R.id.content)
            val likeCountTextView = view.findViewById<TextView>(R.id.like_count)
            val commentCountTextView = view.findViewById<TextView>(R.id.comment_count)
            val userImageView = view.findViewById<ImageView>(R.id.user_image)
            val likeButton = view.findViewById<ImageButton>(R.id.like_button)
            val commentButton = view.findViewById<ImageButton>(R.id.comment_button)

            disposables.add(Single.fromCallable<PostWithUser> { postDao.findByIdWithUser(postId) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { postWithUser ->
                        nameTextView.text = postWithUser.user.name
                        contentTextView.text = postWithUser.post.content
                        Glide.with(view)
                                .load(postWithUser.user.image)
                                .apply(RequestOptions().circleCrop())
                                .into(userImageView)

                        disposables.add(commentDao.listByPostId(postWithUser.post.id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ comments ->
                                    val commentCountText = StringBuilder(Integer.toString(comments.size))
                                    if (comments.size > 1 || comments.isEmpty()) {
                                        commentCountText.append(" comments")
                                    } else {
                                        commentCountText.append(" comment")
                                    }
                                    commentCountTextView.text = commentCountText
                                    commentAdapter.replace(comments)
                                })
                        )
                    }
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }
}