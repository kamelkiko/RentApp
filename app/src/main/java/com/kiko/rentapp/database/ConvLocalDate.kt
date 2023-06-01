package com.kiko.rentapp.database

import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate


class ConvLocalDate {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLong(@Nullable epoch: Long?): LocalDate? {
        return if (epoch == null) null else LocalDate.ofEpochDay(epoch)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun localDateToEpoch(@Nullable localDate: LocalDate?): Long? {
        return localDate?.toEpochDay()
    }
}