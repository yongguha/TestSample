package com.emreeran.android.roomsample.db

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Created by Emre Eran on 20.04.2018.
 */
class Converters {
    @TypeConverter
    fun toDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToLong(date: Date?): Long? = date?.time
}