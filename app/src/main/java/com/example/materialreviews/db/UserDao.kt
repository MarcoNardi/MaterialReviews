package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE uid=:id LIMIT 1")
    fun findById(id: Int): LiveData<UserEntity>

    @Insert
    suspend fun insert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Update()
    suspend fun update(user: UserEntity)

    @Query("UPDATE users SET imageUri=:imageUri WHERE uid=:userId")
    suspend fun updateImage(userId: Int, imageUri:String)

    @Query("UPDATE users SET first_name=:firstName WHERE uid=:userId")
    suspend fun updateFirstName(userId: Int, firstName:String)

    @Query("UPDATE users SET last_name=:lastName WHERE uid=:userId")
    suspend fun updateLastName(userId: Int, lastName:String)

    @Transaction
    @Query("SELECT * FROM users")
    fun getUsersAndReviews(): LiveData<List<UserWithReviews>>


    @Transaction
    @Query("SELECT * FROM users WHERE uid LIKE :userId")
    fun getReviewsOfUser(userId: Int): LiveData<UserWithReviews>


}