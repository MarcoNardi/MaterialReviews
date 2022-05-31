package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: ReviewEntity)

    @Delete
    suspend fun delete(review: ReviewEntity)
    @Query("UPDATE reviews SET review=:newComment, date=:newDate, rating=:newRating WHERE restaurantId=:restaurantId AND userId=:userId ")
    suspend fun updateReview(restaurantId: Int, userId: Int, newComment: String, newDate: String, newRating: Int)


}