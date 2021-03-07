package com.emreeran.android.roomsample.db

import android.content.Context
import android.util.Log
import com.emreeran.android.roomsample.R
import com.emreeran.android.roomsample.db.dao.CommentDao
import com.emreeran.android.roomsample.db.dao.LikeDao
import com.emreeran.android.roomsample.db.dao.PostDao
import com.emreeran.android.roomsample.db.dao.UserDao
import com.emreeran.android.roomsample.db.entity.Comment
import com.emreeran.android.roomsample.db.entity.Like
import com.emreeran.android.roomsample.db.entity.Post
import com.emreeran.android.roomsample.db.entity.User
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter
import javax.inject.Inject

/**
 * Created by Emre Eran on 21.04.2018.
 */
class DataInitializer @Inject constructor(
        userDao: UserDao,
        postDao: PostDao,
        commentDao: CommentDao,
        likeDao: LikeDao
) {

    private val _userDao = userDao
    private val _postDao = postDao
    private val _commentDao = commentDao
    private val _likeDao = likeDao
    private val _disposables = CompositeDisposable()

    fun initialize(context: Context, done: () -> Unit) {
        insertSampleData(context, done)
    }

    fun clearAndInitialize(context: Context, done: () -> Unit) {
        _disposables.add(Completable.fromCallable({ _likeDao.purge() })
                .andThen(Completable.fromCallable { _commentDao.purge() })
                .andThen(Completable.fromCallable({ _postDao.purge() }))
                .andThen(Completable.fromCallable { _userDao.purge() })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val prefs = context.getSharedPreferences("android-room-sample", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("data-initialized", false).apply()
                    insertSampleData(context, done)
                }))
    }

    private fun insertSampleData(context: Context, done: () -> Unit) {
        val prefs = context.getSharedPreferences("android-room-sample", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("data-initialized", false)) {
            Log.d("data initializer", "Started initializing data")
            val inputStreamReader = context.resources.openRawResource(R.raw.sample_data)
            val writer = StringWriter()
            val buffer = CharArray(1024)
            inputStreamReader.use {
                val reader = BufferedReader(InputStreamReader(it, "UTF-8"))
                var n: Int = reader.read(buffer)
                while (n != -1) {
                    writer.write(buffer, 0, n)
                    n = reader.read(buffer)
                }
            }

            val jsonString = writer.toString()
            val dataJson = JSONObject(jsonString)
            val usersJson = dataJson.getJSONArray("users")
            val lines = dataJson.getJSONArray("lines")
            val postContents = ArrayList<String>()
            val commentContents = ArrayList<String>()

            for (i in 0 until lines.length()) {
                val line = lines.getString(i)
                if (i < 50) {
                    postContents.add(line)
                } else {
                    commentContents.add(line)
                }
            }

            val users: ArrayList<User> = ArrayList()

            for (i in 0 until usersJson.length()) {
                val userJson = usersJson.getJSONObject(i)
                val name = userJson.getString("name")
                val image = userJson.getString("image")
                val user = User(name = name, image = image)
                users.add(user)
            }

            _disposables.add(Completable.fromCallable { _userDao.insertAll(users) }
                    .subscribeOn(Schedulers.io())
                    .andThen(_userDao.list())
                    .flatMap { savedUsers ->
                        val userCount = savedUsers.size
                        val posts = ArrayList<Post>()
                        for (i in 0..(postContents.size - 1)) {
                            val content = postContents[i]
                            val user = savedUsers[i % userCount]
                            val post = Post(userId = user.id, content = content)
                            posts.add(post)
                        }
                        Completable.fromCallable { _postDao.insertAll(posts) }
                                .andThen(_postDao.list())
                                .map { savedPosts ->
                                    val likes = ArrayList<Like>()
                                    var userIndex = 0
                                    var user = savedUsers[userIndex]
                                    for (i in 0..(savedPosts.size - 1)) {
                                        val post = savedPosts[i]
                                        val like = Like(userId = user.id, postId = post.id)
                                        likes.add(like)
                                        userIndex = (userIndex + 1) % userCount
                                        user = savedUsers[userIndex]
                                    }
                                    _likeDao.insertAll(likes)
                                    Pair(savedUsers, savedPosts)
                                }
                    }
                    .flatMapCompletable { pair ->
                        val userCount = pair.first.size
                        val postCount = pair.second.size
                        val comments = ArrayList<Comment>()

                        var userIndex = 0
                        var postIndex = 0

                        var user = pair.first[userIndex]
                        var post = pair.second[postIndex]

                        for (i in 0..(commentContents.size - 1)) {
                            val content = commentContents[i]
                            val comment = Comment(userId = user.id, postId = post.id, content = content)
                            comments.add(comment)

                            userIndex = (userIndex + 1) % userCount
                            user = pair.first[userIndex]
                            postIndex = (postIndex + 1) % postCount
                            post = pair.second[postIndex]
                        }
                        Completable.fromCallable { _commentDao.insertAll(comments) }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        prefs.edit().putBoolean("data-initialized", true).apply()
                        Log.d("data initializer", "Data initialization complete")
                        _disposables.dispose()
                        done()
                    }))
        } else {
            Log.d("data initializer", "Data already initialized")
            done()
        }
    }
}