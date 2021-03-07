package com.emreeran.android.roomsample.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.dao.CommentDao;
import com.emreeran.android.roomsample.db.dao.LikeDao;
import com.emreeran.android.roomsample.db.dao.PostDao;
import com.emreeran.android.roomsample.db.dao.UserDao;
import com.emreeran.android.roomsample.db.entity.Comment;
import com.emreeran.android.roomsample.db.entity.Like;
import com.emreeran.android.roomsample.db.entity.Post;
import com.emreeran.android.roomsample.db.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emre Eran on 8.05.2018.
 */
public class DataInitializer {

    private CompositeDisposable mDisposables = new CompositeDisposable();
    private UserDao mUserDao;
    private PostDao mPostDao;
    private CommentDao mCommentDao;
    private LikeDao mLikeDao;

    public DataInitializer(Context context) {
        SampleDb db = SampleDb.getInstance(context);
        mUserDao = db.userDao();
        mPostDao = db.postDao();
        mCommentDao = db.commentDao();
        mLikeDao = db.likeDao();
    }

    public void initialize(Context context, InitializerCallback callback) {
        insertSampleData(context, callback);
    }

    public void clearAndInitialize(Context context, InitializerCallback callback) {
        mDisposables.add(Completable.fromCallable((Callable<Void>) () -> {
                    mLikeDao.purge();
                    return null;
                })
                        .andThen(Completable.fromCallable((Callable<Void>) () -> {
                            mCommentDao.purge();
                            return null;
                        }))
                        .andThen(Completable.fromCallable((Callable<Void>) () -> {
                            mPostDao.purge();
                            return null;
                        }))
                        .andThen(Completable.fromCallable((Callable<Void>) () -> {
                            mUserDao.purge();
                            return null;
                        }))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            SharedPreferences prefs =
                                    context.getSharedPreferences("android-room-sample", Context.MODE_PRIVATE);
                            prefs.edit().putBoolean("data-initialized", false).apply();
                            insertSampleData(context, callback);
                        })
        );
    }

    private void insertSampleData(Context context, InitializerCallback callback) {
        SharedPreferences prefs =
                context.getSharedPreferences("android-room-sample", Context.MODE_PRIVATE);

        if (!prefs.getBoolean("data-initialized", false)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.sample_data)));
            String input;
            StringWriter writer = new StringWriter();
            try {
                while ((input = reader.readLine()) != null) {
                    writer.write(input);
                }
                String jsonString = writer.toString();
                JSONObject dataJson = new JSONObject(jsonString);
                JSONArray usersJson = dataJson.getJSONArray("users");
                JSONArray lines = dataJson.getJSONArray("lines");
                ArrayList<String> postContents = new ArrayList<>();
                ArrayList<String> commentContents = new ArrayList<>();
                for (int i = 0; i < lines.length(); i++) {
                    String line = lines.getString(i);
                    if (i < 50) {
                        postContents.add(line);
                    } else {
                        commentContents.add(line);
                    }
                }

                ArrayList<User> users = new ArrayList<>();
                for (int i = 0; i < usersJson.length(); i++) {
                    JSONObject userJson = usersJson.getJSONObject(i);
                    String name = userJson.getString("name");
                    String image = userJson.getString("image");
                    User user = new User(name, image);
                    users.add(user);
                }

                mDisposables.add(Completable.fromCallable((Callable<Void>) () -> {
                            mUserDao.insertAll(users);
                            return null;
                        }).subscribeOn(Schedulers.io())
                                .andThen(mUserDao.list())
                                .flatMap((Function<List<User>, SingleSource<Pair<List<User>, List<Post>>>>) savedUsers -> {
                                    List<Post> posts = createPosts(savedUsers, postContents);
                                    return Completable.fromCallable((Callable<Void>) () -> {
                                        mPostDao.insertAll(posts);
                                        return null;
                                    }).andThen(mPostDao.list())
                                            .map(savedPosts -> {
                                                List<Like> likes = createLikes(savedUsers, savedPosts);
                                                mLikeDao.insertAll(likes);
                                                return new Pair<>(savedUsers, savedPosts);
                                            });
                                })
                                .flatMapCompletable(pair -> {
                                    List<Comment> comments = createComments(pair.first, pair.second, commentContents);
                                    return Completable.fromCallable((Callable<Void>) () -> {
                                        mCommentDao.insertAll(comments);
                                        return null;
                                    });
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    prefs.edit().putBoolean("data-initialized", true).apply();
                                    mDisposables.dispose();
                                    callback.done();
                                })

                );
            } catch (IOException | JSONException e) {
                Log.e("DataInitializer", "Could not create sample data", e);
                callback.done();
            }
        } else {
            callback.done();
        }
    }

    private List<Post> createPosts(List<User> users, List<String> contents) {
        int userCount = users.size();
        ArrayList<Post> posts = new ArrayList<>();

        for (int i = 0; i < contents.size(); i++) {
            String content = contents.get(i);
            User user = users.get(i % userCount);
            Post post = new Post(user.id, content);
            posts.add(post);
        }
        return posts;
    }

    private List<Like> createLikes(List<User> users, List<Post> posts) {
        ArrayList<Like> likes = new ArrayList<>();
        int userIndex = 0;
        int userCount = users.size();
        User user = users.get(userIndex);

        for (Post post : posts) {
            Like like = new Like(user.id, post.id);
            likes.add(like);
            userIndex = (userIndex + 1) % userCount;
            user = users.get(userIndex);
        }

        return likes;
    }

    private List<Comment> createComments(List<User> users, List<Post> posts, List<String> contents) {
        int userCount = users.size();
        int postCount = posts.size();
        ArrayList<Comment> comments = new ArrayList<>();
        int userIndex = 0;
        int postIndex = 0;

        User user = users.get(userIndex);
        Post post = posts.get(postIndex);

        for (int i = 0; i < contents.size(); i++) {
            String content = contents.get(i);
            Comment comment = new Comment(user.id, post.id, content);
            comments.add(comment);

            userIndex = (userIndex + 1) % userCount;
            user = users.get(userIndex);
            if (i % 3 == 0) {
                postIndex = (postIndex + 1) % postCount;

                post = posts.get(postIndex);
            }
        }

        return comments;
    }

    public interface InitializerCallback {
        void done();
    }
}
