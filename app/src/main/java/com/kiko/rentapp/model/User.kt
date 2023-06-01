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
@Entity(tableName = "table_user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,//id
    var name: String? = "",//name
    var nameRent: String? = "",//asm el mostlm
    var owner: String? = "",
    @TypeConverters(ConvDate::class)
    var date: Date? = Calendar.getInstance().time,//tare5 el sakn
    var period: String? = "",//modt el sakn
    @TypeConverters(ConvLocalDate::class)
    var endDate: LocalDate? = null,//tare5 el 2ntha2
    var address: String? = "",//3nwan
    var rentalValue: Float? = 0.0f,//el 2egar
    var raise: Float? = 0.0f,//el nsba
    var idNumber: String? = "",//el rkm el kwmy
    var insuranceAmount: Float? = 0.0f,//mbl8 el t2men
    var rentPictureFront: String? = null, //image le el 3a2d odam
    var rentPictureBack: String? = null//image le el 3a2d wra
) : Parcelable