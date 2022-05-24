package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    fun getAll(): LiveData<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE rid IN (:restaurantIds)")
    fun loadAllByIds(restaurantIds: IntArray): LiveData<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE rid LIKE :id LIMIT 1")
    fun findById(id: Int): LiveData<RestaurantEntity>

    @Insert
    suspend fun insert(restaurant: RestaurantEntity)

    @Delete
    suspend fun delete(restaurant: RestaurantEntity)

    @Transaction
    @Query("SELECT * FROM restaurants")
    fun getRestaurantsAndImages(): LiveData<List<RestaurantWithImages>>

    @Transaction
    @Query("SELECT * FROM restaurants")
    fun getRestaurantsAndReviews(): LiveData<List<RestaurantWithReviews>>

    @Transaction
    @Query("SELECT * FROM restaurants WHERE rid LIKE :restaurantId")
    fun getReviewsOfRestaurant(restaurantId: Int): LiveData<RestaurantWithReviews>
}