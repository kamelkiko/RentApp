package com.kiko.rentapp.pdf

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kiko.rentapp.database.ConvDate
import com.kiko.rentapp.database.ConvLocalDate
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.Period
import java.util.*
@Parcelize
@Entity(tableName = "table_report")
data class UserPdf(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String? = "",//asm el sakn
    var owner: String? = "",//el owner
    var nameRent: String? = "",//asm el mostlm
    var address: String? = "",//3nwan
    var rentalValue: Float? = 0.0f,//el 2egar
    @TypeConverters(ConvLocalDate::class)
    var fromDate: LocalDate? = null,//mn
    @TypeConverters(ConvDate::class)
    var date: Date? = Calendar.getInstance().time,//tare5 el sakn
    @TypeConverters(ConvLocalDate::class)
    var toDate: LocalDate? = null,//7ta
    @TypeConverters(ConvLocalDate::class)
    var endDate: LocalDate? = null,//tare5 el 2ntha
    var notes: String? = "",//notes
    var raise: Float? = 0.0f,
    var water: Float? = 0.0f,//water
    @TypeConverters(ConvLocalDate::class)
    var currentDate: LocalDate? = null//tare5 tba3t el wasl
): Parcelable
