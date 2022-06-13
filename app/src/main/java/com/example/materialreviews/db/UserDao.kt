package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

//dao dell'entià User
@Dao
interface UserDao {
    //ottiene tutti gli utenti
    @Query("SELECT * FROM users")
    fun getAll(): LiveData<List<UserEntity>>

    //ottiene tutti gli utenti dati da degli id
    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): LiveData<List<UserEntity>>

    //cerca un utente in base al suo Id
    @Query("SELECT * FROM users WHERE uid=:id LIMIT 1")
    fun findById(id: Int): LiveData<UserEntity>
    //inserisce un utente
    @Insert
    suspend fun insert(user: UserEntity)
    //cancella un utente
    @Delete
    suspend fun delete(user: UserEntity)
    //aggiorna un utente
    @Update()
    suspend fun update(user: UserEntity)
    //aggiorna l'immagine di un utente
    @Query("UPDATE users SET imageUri=:imageUri WHERE uid=:userId")
    suspend fun updateImage(userId: Int, imageUri:String)
    //aggiorna il firstName di un utente
    @Query("UPDATE users SET first_name=:firstName WHERE uid=:userId")
    suspend fun updateFirstName(userId: Int, firstName:String)
    //aggiorna il lastName di un utente
    @Query("UPDATE users SET last_name=:lastName WHERE uid=:userId")
    suspend fun updateLastName(userId: Int, lastName:String)

    //ottiene tutta la lista di utenti con tutte le loro reviews
    @Transaction
    @Query("SELECT * FROM users")
    fun getUsersAndReviews(): LiveData<List<UserWithReviews>>

    //ottiene tutte le recensioni scritte da un determinato utente
    @Transaction
    @Query("SELECT * FROM users WHERE uid LIKE :userId")
    fun getReviewsOfUser(userId: Int): LiveData<UserWithReviews>


}