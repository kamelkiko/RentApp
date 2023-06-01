package com.kiko.rentapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kiko.rentapp.database.ConvDate
import com.kiko.rentapp.database.ConvLocalDate
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.*


@Parcelize
@Entity(tableName = "table_not_paid")
data class UserNotPaid(
    @PrimaryKey(autoGenerate = true)
    val id: Int,//id
    var name: String? = "",//name
    var owner: String? = "",
    var nameRent: String? = "",//asm el mostlm
    var address: String? = "",//3nwan
    var netRent: Float? = 0.0f,//el 2egar
    @TypeConverters(ConvLocalDate::class)
    var fromDate: LocalDate? = null,//mn
    @TypeConverters(ConvDate::class)
    var date: Date? = Calendar.getInstance().time,//tare5 el sakn
    @TypeConverters(ConvLocalDate::class)
    var toDate: LocalDate? = null,//7ta
    @TypeConverters(ConvLocalDate::class)
    var endDate: LocalDate? = null,//tare5 el 2ntha
    @TypeConverters(ConvLocalDate::class)
    var currentDate: LocalDate? = null,//tare5 tba3t el wasl
    var raise: Float? = 0.0f,
    var number: Int? = 0,//kam 4ahr
    var newNumber: Int? = 0
) : Parcelable
