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

    private fun createReviewEntry(rating: Int, userId: Int, restaurantId: Int): ReviewEntity{
        return ReviewEntity(rating, userId, restaurantId)
    }

    fun addReview(rating: Int, userId: Int, restaurantId: Int){
        insertReview(createReviewEntry(rating, userId, restaurantId))
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