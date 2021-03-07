package com.emreeran.android.roomsample.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.db.dao.FeedDao
import com.emreeran.android.roomsample.db.vo.FeedItem
import com.emreeran.android.roomsample.di.Injectable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Emre Eran on 24.04.2018.
 */
class FeedFragment : Fragment(), Injectable {

    private var disposables: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var feedDao: FeedDao

    @Inject
    lateinit var navigationController: NavigationController

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

        view?.let { view ->
            val feedView = view.findViewById<RecyclerView>(R.id.feed)

            val itemDecoration = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
            val divider = resources.getDrawable(R.drawable.feed_divider)
            itemDecoration.setDrawable(divider)
            feedView.addItemDecoration(itemDecoration)

            val adapter = FeedAdapter()
            adapter.setOnItemClickListener(View.OnClickListener {
                val itemPosition = feedView.getChildLayoutPosition(it)
                val item = adapter.getListItem(itemPosition)
                navigationController.navigateToPostDetail(item!!.postWithUser.post.id)
            })

            feedView.adapter = adapter

            disposables.add(
                    Flowable.fromCallable<List<FeedItem>>({ feedDao.listFeedItems() })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ adapter.replace(it) })
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }
}