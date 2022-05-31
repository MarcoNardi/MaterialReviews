package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ReviewViewModel(private val reviewDao: ReviewDao) : ViewModel() {
    private fun insertReview(review: ReviewEntity) {
        viewModelScope.launch{
            reviewDao.insert(review)
        }
    }

    private fun createReviewEntry(rating: Int, review: String, userId: Int, restaurantId: Int, date:String): ReviewEntity{
        return ReviewEntity(rating,review, userId, restaurantId,date)
    }
    //add a review
    fun addReview(rating: Int, review: String,userId: Int, restaurantId: Int, date:String){
        insertReview(createReviewEntry(rating,review, userId, restaurantId, date))
    }
    //add a review
    fun addReview(review: ReviewEntity){
        insertReview(review)
    }
    fun updateReview(restaurantId: Int, userId: Int, newComment:String, newDate:String, newRating: Int){
        viewModelScope.launch {
            reviewDao.updateReview(restaurantId, userId, newComment, newDate, newRating)
        }
    }



}

class ReviewViewModelFactory(private val reviewDao: ReviewDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(reviewDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}