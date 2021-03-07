package com.emreeran.android.roomsample.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.DataInitializer;
import com.emreeran.android.roomsample.db.dao.UserDao;
import com.emreeran.android.roomsample.db.entity.User;
import com.emreeran.android.roomsample.di.Injectable;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emre Eran on 26.05.2018.
 */
public class InitializerFragment extends Fragment implements Injectable {

    public static InitializerFragment create() {
        return new InitializerFragment();
    }

    @Inject
    UserDao mUserDao;

    @Inject
    NavigationController mNavigationController;

    private CompositeDisposable mDisposables;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.initializer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        mDisposables = new CompositeDisposable();

        if (view != null && getContext() != null) {
            Button continueButton = view.findViewById(R.id.continue_button);
            TextView initializeTitleTextView = view.findViewById(R.id.initialize_data_title);
            ProgressBar progressBar = view.findViewById(R.id.progress_bar);
            EditText nameEditText = view.findViewById(R.id.user_name_input);


            continueButton.setOnClickListener(v -> {
                String name = nameEditText.getText().toString();
                User user = new User(name, null);
                mDisposables.add(
                        Single.fromCallable(() -> mUserDao.insert(user))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((userId) -> {
                                    SharedPreferences prefs =
                                            getContext().getSharedPreferences("android-room-sample", Context.MODE_PRIVATE);
                                    prefs.edit().putInt("logged_in_as", userId.intValue()).apply();
                                    mNavigationController.navigateToFeed();
                                })
                );
            });

            DataInitializer dataInitializer = new DataInitializer();
            dataInitializer.initialize(getContext(), () -> {
                initializeTitleTextView.setText(R.string.initializing_complete);
                progressBar.setVisibility(View.INVISIBLE);
                continueButton.setClickable(true);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }
}
