package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {
    private fun insertUser(user: UserEntity) {
        viewModelScope.launch{
            userDao.insert(user)
        }
    }

    private fun createUserEntry(id: Int, firstName: String, lastName: String): UserEntity{
        return UserEntity(id, firstName, lastName)
    }

    fun addUser(id: Int, firstName: String, lastName: String){
        insertUser(createUserEntry(id,firstName,lastName))
    }

    fun addUser(user:UserEntity){
        insertUser(user)
    }


    fun getAllUsers() :  LiveData<List<UserEntity>>{
        return userDao.getAll()
    }

    fun getAllUsersWithReviews() :  LiveData<List<UserWithReviews>>{
        return userDao.getUsersAndReviews()
    }

    fun getReviewsOfUser(userId: Int) :  LiveData<UserWithReviews>{
        return userDao.getReviewsOfUser(userId)
    }
}

class UserViewModelFactory(private val userDao: UserDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}