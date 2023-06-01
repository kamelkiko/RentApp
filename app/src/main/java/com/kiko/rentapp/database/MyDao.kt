package com.kiko.rentapp.database


import androidx.room.*
import com.kiko.rentapp.model.User
import com.kiko.rentapp.model.UserNotPaid
import com.kiko.rentapp.pdf.UserPdf
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDataUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDataUserNotPaid(user: UserNotPaid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReport(user: UserPdf)

    @Query("SELECT * FROM table_user ORDER BY id ASC")
    fun readAllDataUser(): Flow<List<User>>

    @Query("SELECT * FROM table_report ORDER BY id ASC")
    fun readAllReport(): Flow<List<UserPdf>>

    @Query("SELECT * FROM table_not_paid ORDER BY id ASC")
    fun readAllDataUserNotPaid(): Flow<List<UserNotPaid>>

    @Update
    suspend fun updateDataUser(user: User)

    @Delete
    suspend fun deleteDataUser(user: User)

    @Delete
    suspend fun deleteReport(user: UserPdf)

    @Update
    suspend fun updateDataUserNotPaid(user: UserNotPaid)

    @Delete
    suspend fun deleteDataUserNotPaid(user: UserNotPaid)

    @Query("DELETE FROM table_user")
    suspend fun deleteALlDataUser()

    @Query("DELETE FROM table_not_paid")
    suspend fun deleteALlDataUserNotPaid()

    @Query("SELECT * FROM table_user WHERE name LIKE :searchQuery OR nameRent LIKE :searchQuery OR owner LIKE :searchQuery")
    fun searchDataBaseUser(searchQuery: String): Flow<List<User>>

    @Query("SELECT * FROM table_not_paid WHERE name LIKE :searchQuery OR nameRent LIKE :searchQuery OR owner LIKE :searchQuery")
    fun searchDataBaseUserNotPaid(searchQuery: String): Flow<List<UserNotPaid>>

    @Query("SELECT * FROM table_report WHERE name LIKE :searchQuery OR nameRent LIKE :searchQuery OR owner LIKE :searchQuery")
    fun searchDataBaseReport(searchQuery: String): Flow<List<UserPdf>>
}