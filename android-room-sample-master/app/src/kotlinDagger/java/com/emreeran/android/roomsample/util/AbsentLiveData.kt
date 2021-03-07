package com.emreeran.android.roomsample.util

import android.arch.lifecycle.LiveData

/**
 * Created by Emre Eran on 25.04.2018.
 */
class AbsentLiveData<T> : LiveData<T>() {
    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}