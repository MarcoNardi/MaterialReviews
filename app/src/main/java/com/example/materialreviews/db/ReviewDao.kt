package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

//dao per l'entità review
@Dao
interface ReviewDao {
    //inserisce una review
    @Insert
    suspend fun insert(review: ReviewEntity)
    //cancella una review
    @Delete
    suspend fun delete(review: ReviewEntity)
    //cancella una review passando la sua primary key
    @Query("DELETE FROM reviews WHERE restaurantId=:restaurantId AND userId=:userId")
    suspend fun deleteByIds(restaurantId: Int, userId: Int)
    //aggiorna una review
    @Query("UPDATE reviews SET review=:newComment, date=:newDate, rating=:newRating WHERE restaurantId=:restaurantId AND userId=:userId ")
    suspend fun updateReview(restaurantId: Int, userId: Int, newComment: String, newDate: String, newRating: Int)


}