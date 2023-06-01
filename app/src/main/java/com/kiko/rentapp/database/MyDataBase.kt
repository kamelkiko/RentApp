package com.kiko.rentapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kiko.rentapp.model.User
import com.kiko.rentapp.model.UserNotPaid
import com.kiko.rentapp.pdf.UserPdf


@Database(entities = [User::class, UserNotPaid::class, UserPdf::class], version = 1)
@TypeConverters(ConvDate::class, ConvLocalDate::class)
abstract class MyDataBase : RoomDatabase() {
    abstract fun getDao(): MyDao

    companion object {
        @Volatile
        private var myDataBase: MyDataBase? = null
        fun getDataBase(context: Context): MyDataBase {
            val temp = myDataBase
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, MyDataBase::class.java,
                    "table_main"
                ).fallbackToDestructiveMigration().build()
                myDataBase = instance
                return instance
            }
        }
    }
}