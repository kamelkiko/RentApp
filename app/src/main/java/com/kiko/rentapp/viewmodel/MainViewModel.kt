package com.kiko.rentapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.kiko.rentapp.database.MyDataBase
import com.kiko.rentapp.model.User
import com.kiko.rentapp.model.UserNotPaid
import com.kiko.rentapp.pdf.UserPdf
import com.kiko.rentapp.repositiry.Repo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: Repo
    val readAllDataUser: LiveData<List<User>>
    val readAllDataUserNotPaid: LiveData<List<UserNotPaid>>
    val readAllReport: LiveData<List<UserPdf>>

    init {
        val userDao = MyDataBase.getDataBase(application).getDao()
        repo = Repo(userDao)
        readAllDataUser = repo.readDataUser().asLiveData()
        readAllDataUserNotPaid = repo.readDataUserNotPaid().asLiveData()
        readAllReport = repo.readReport().asLiveData()
    }

    fun saveUser(user: User) {
        viewModelScope.launch(IO) {
            repo.saveDataUser(user)
        }
    }

    fun saveReport(user: UserPdf) {
        viewModelScope.launch(IO) {
            repo.saveReport(user)
        }
    }

    fun saveUserNotPaid(user: UserNotPaid) {
        viewModelScope.launch(IO) {
            repo.saveDataUserNotPaid(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(IO) {
            repo.updateDataUser(user)
        }
    }

    fun updateUserNotPaid(user: UserNotPaid) {
        viewModelScope.launch(IO) {
            repo.updateDataUserNotPaid(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(IO) {
            repo.deleteDataUser(user)
        }
    }

    fun deleteReport(user: UserPdf) {
        viewModelScope.launch(IO) {
            repo.deleteReport(user)
        }
    }

    fun deleteUserNotPaid(user: UserNotPaid) {
        viewModelScope.launch(IO) {
            repo.deleteDataUserNotPaid(user)
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(IO) {
            repo.deleteAllDataUser()
        }
    }

    fun deleteAllUsersNotPaid() {
        viewModelScope.launch(IO) {
            repo.deleteAllDataUserNotPaid()
        }
    }

    fun searchDataBaseUser(searchQuery: String): LiveData<List<User>> {
        return repo.searchDataUser(searchQuery).asLiveData()
    }

    fun searchDataBaseUserNotPaid(searchQuery: String): LiveData<List<UserNotPaid>> {
        return repo.searchDataUserNotPaid(searchQuery).asLiveData()
    }

    fun searchDataBaseReport(searchQuery: String): LiveData<List<UserPdf>> {
        return repo.searchReport(searchQuery).asLiveData()
    }
}