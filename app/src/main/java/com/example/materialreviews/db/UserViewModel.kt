package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//viewModel per l'utente
class UserViewModel(private val userDao: UserDao) : ViewModel() {
    /**
     *helper per inserimento
     */
    private fun insertUser(user: UserEntity) {
        viewModelScope.launch{
            userDao.insert(user)
        }
    }
    /**
     *helper per creare un'entry
     */
    private fun createUserEntry(id: Int, firstName: String, lastName: String, imageUri: String): UserEntity{
        return UserEntity(id, firstName, lastName, imageUri)
    }
    /**
     *aggiungi utente
     */
    fun addUser(id: Int, firstName: String, lastName: String, imageUri: String){
        insertUser(createUserEntry(id,firstName,lastName, imageUri ))
    }
    /**
     *aggiungi utente
     */
    fun addUser(user:UserEntity){
        insertUser(user)
    }

    /**
     *ottiene un determinato utente
     */
    fun getUser(userId: Int) : LiveData<UserEntity>{
        return userDao.findById(userId)
    }
    /**
     *ottiene una lista di tutti gli utenti
     */
    fun getAllUsers() :  LiveData<List<UserEntity>>{
        return userDao.getAll()
    }
    /**
     *ottiene tutte le copie(user, listReviews)
     */
    fun getAllUsersWithReviews() :  LiveData<List<UserWithReviews>>{
        return userDao.getUsersAndReviews()
    }
    /**
     * ottiene tutte le reviews di un utente
     */
    fun getReviewsOfUser(userId: Int) :  LiveData<UserWithReviews>{
        return userDao.getReviewsOfUser(userId)
    }
    /**
     *aggiorna l'immagine di un utente
     */
    fun updateImageOfUser(userId: Int, imageUri: String){
        viewModelScope.launch {
            userDao.updateImage(userId, imageUri)
        }
    }

    /**
     *aggiorna il firstName
     */
    fun updateFirstNameOfUser(userId: Int, firstName: String){
        viewModelScope.launch {
            userDao.updateFirstName(userId, firstName)
        }
    }
    /**
     *aggiorna il lastName
     */
    fun updateLastNameOfUser(userId: Int, lastName: String){
        viewModelScope.launch {
            userDao.updateLastName(userId, lastName)
        }
    }
    /**
     *aggiorna l'utente
     */
    fun updateUser(user: UserEntity){
        viewModelScope.launch {
            userDao.update(user)
        }
    }
    /**
     *aggiorna l'utente
     */
    fun updateUser(userId: Int, firstName:String, lastName: String, imageUri: String){
        viewModelScope.launch {
            userDao.update(createUserEntry(userId ,firstName, lastName, imageUri))
        }
    }

}
//pattern factory
class UserViewModelFactory(private val userDao: UserDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}