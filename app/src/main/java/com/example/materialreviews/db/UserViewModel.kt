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

    private fun createUserEntry(id: Int, firstName: String, lastName: String, imageUri: String): UserEntity{
        return UserEntity(id, firstName, lastName, imageUri)
    }
    //add user
    fun addUser(id: Int, firstName: String, lastName: String, imageUri: String){
        insertUser(createUserEntry(id,firstName,lastName, imageUri ))
    }
    //add user
    fun addUser(user:UserEntity){
        insertUser(user)
    }

    //get  a certain user
    fun getUser(userId: Int) : LiveData<UserEntity>{
        return userDao.findById(userId)
    }
    //get a list of all users
    fun getAllUsers() :  LiveData<List<UserEntity>>{
        return userDao.getAll()
    }
    //get allpairs (user, reviwes
    fun getAllUsersWithReviews() :  LiveData<List<UserWithReviews>>{
        return userDao.getUsersAndReviews()
    }
    //get a list of reviews of a certain user
    fun getReviewsOfUser(userId: Int) :  LiveData<UserWithReviews>{
        return userDao.getReviewsOfUser(userId)
    }

    fun updateImageOfUser(userId: Int, imageUri: String){
        viewModelScope.launch {
            userDao.updateImage(userId, imageUri)
        }
    }

    fun updateFirstNameOfUser(userId: Int, firstName: String){
        viewModelScope.launch {
            userDao.updateFirstName(userId, firstName)
        }
    }

    fun updateLastNameOfUser(userId: Int, lastName: String){
        viewModelScope.launch {
            userDao.updateLastName(userId, lastName)
        }
    }
    fun updateUser(user: UserEntity){
        viewModelScope.launch {
            userDao.update(user)
        }
    }

    fun updateUser(userId: Int, firstName:String, lastName: String, imageUri: String){
        viewModelScope.launch {
            userDao.update(createUserEntry(userId ,firstName, lastName, imageUri))
        }
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