package com.emreeran.android.roomsample.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.db.DataInitializer
import com.emreeran.android.roomsample.db.dao.UserDao
import com.emreeran.android.roomsample.db.entity.User
import com.emreeran.android.roomsample.di.Injectable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Emre Eran on 20.04.2018.
 */
class InitializerFragment : Fragment(), Injectable {

    @Inject
    lateinit var dataInitializer: DataInitializer

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var navigationController: NavigationController

    private val disposables: CompositeDisposable = CompositeDisposable()

    companion object {
        fun create(): InitializerFragment {
            return InitializerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.initializer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.let { view ->
            context?.let { context ->
                val continueButton = view.findViewById<Button>(R.id.continue_button)
                val initializeTitleTextView = view.findViewById<TextView>(R.id.initialize_data_title)
                val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
                val nameEditText = view.findViewById<EditText>(R.id.user_name_input)

                continueButton.setOnClickListener { v ->
                    val name = nameEditText.text.toString()
                    val user = User(name = name)
                    disposables.add(
                            Completable.fromCallable({
                                userDao.insert(user)
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe {
                                        val prefs = context.getSharedPreferences(
                                                "android-room-sample", Context.MODE_PRIVATE
                                        )
                                        prefs.edit().putInt("logged_in_as", user.id).apply()
                                        navigationController.navigateToFeed()
                                    }
                    )
                }

                dataInitializer.initialize(context, {
                    initializeTitleTextView.setText(R.string.initializing_complete)
                    progressBar.visibility = View.INVISIBLE
                    continueButton.isClickable = true
                })
            }
        }
    }
}