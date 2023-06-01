package com.kiko.rentapp.database

import androidx.room.TypeConverter
import java.util.*

class ConvDate {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}