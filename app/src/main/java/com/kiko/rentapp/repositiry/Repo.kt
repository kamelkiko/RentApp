package com.kiko.rentapp.repositiry

import com.kiko.rentapp.database.MyDao
import com.kiko.rentapp.model.User
import com.kiko.rentapp.model.UserNotPaid
import com.kiko.rentapp.pdf.UserPdf
import kotlinx.coroutines.flow.Flow

class Repo(private val myDao: MyDao) {

    fun readDataUser(): Flow<List<User>> {
        return myDao.readAllDataUser()
    }

    fun readReport(): Flow<List<UserPdf>> {
        return myDao.readAllReport()
    }

    fun readDataUserNotPaid(): Flow<List<UserNotPaid>> {
        return myDao.readAllDataUserNotPaid()
    }

    suspend fun saveDataUser(user: User) {
        myDao.saveDataUser(user)
    }

    suspend fun saveReport(userPdf: UserPdf) {
        myDao.saveReport(userPdf)
    }

    suspend fun saveDataUserNotPaid(user: UserNotPaid) {
        myDao.saveDataUserNotPaid(user)
    }

    suspend fun updateDataUser(user: User) {
        myDao.updateDataUser(user)
    }

    suspend fun updateDataUserNotPaid(user: UserNotPaid) {
        myDao.updateDataUserNotPaid(user)
    }

    suspend fun deleteDataUser(user: User) {
        myDao.deleteDataUser(user)
    }

    suspend fun deleteReport(user: UserPdf) {
        myDao.deleteReport(user)
    }

    suspend fun deleteDataUserNotPaid(user: UserNotPaid) {
        myDao.deleteDataUserNotPaid(user)
    }

    suspend fun deleteAllDataUser() {
        myDao.deleteALlDataUser()
    }

    suspend fun deleteAllDataUserNotPaid() {
        myDao.deleteALlDataUserNotPaid()
    }

    fun searchDataUser(searchQuery: String): Flow<List<User>> {
        return myDao.searchDataBaseUser(searchQuery)
    }

    fun searchReport(searchQuery: String): Flow<List<UserPdf>> {
        return myDao.searchDataBaseReport(searchQuery)
    }

    fun searchDataUserNotPaid(searchQuery: String): Flow<List<UserNotPaid>> {
        return myDao.searchDataBaseUserNotPaid(searchQuery)
    }
}