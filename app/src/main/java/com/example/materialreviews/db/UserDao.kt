package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE uid LIKE :id LIMIT 1")
    fun findById(id: Int): LiveData<UserEntity>

    @Insert
    suspend fun insert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Transaction
    @Query("SELECT * FROM users")
    fun getUsersAndReviews(): LiveData<List<UserWithReviews>>


    @Transaction
    @Query("SELECT * FROM users WHERE uid LIKE :userId")
    fun getReviewsOfUser(userId: Int): LiveData<UserWithReviews>


}