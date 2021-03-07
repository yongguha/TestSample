package com.emreeran.android.roomsample.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emreeran.android.roomsample.MainActivity
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.db.SampleDb
import com.emreeran.android.roomsample.db.ui.FeedAdapter
import com.emreeran.android.roomsample.db.vo.FeedItem
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by Emre Eran on 26.05.2018.
 */
class FeedFragment : Fragment() {

    private var mDisposables: CompositeDisposable = CompositeDisposable()

    companion object {
        fun create(): FeedFragment {
            return FeedFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mDisposables = CompositeDisposable()

        val feedView = Objects.requireNonNull<View>(view).findViewById<RecyclerView>(R.id.feed)

        val itemDecoration = DividerItemDecoration(Objects.requireNonNull<Context>(context), DividerItemDecoration.VERTICAL)
        val divider = resources.getDrawable(R.drawable.feed_divider)
        itemDecoration.setDrawable(divider)
        feedView.addItemDecoration(itemDecoration)

        val adapter = FeedAdapter()
        adapter.setOnItemClickListener(View.OnClickListener {
            if (activity != null) {
                val itemPosition = feedView.getChildLayoutPosition(it)
                val item = adapter.getListItem(itemPosition)
                (activity as MainActivity).navigateToPostDetail(item!!.postWithUser.post.id)
            }
        })

        feedView.adapter = adapter

        val db = SampleDb.getInstance(context!!)
        val feedDao = db!!.feedDao()

        mDisposables.add(
                Flowable.fromCallable<List<FeedItem>>({ feedDao.listFeedItems() })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ adapter.replace(it) })
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDisposables.clear()
    }
}