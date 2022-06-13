package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
//viewModel che si interfaccia alle reviews
class ReviewViewModel(private val reviewDao: ReviewDao) : ViewModel() {

    //helper per inserire reviews
    private fun insertReview(review: ReviewEntity) {
        viewModelScope.launch{
            reviewDao.insert(review)
        }
    }
    //helper pre creare un'entry
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

    //aggiorna una review
    fun updateReview(restaurantId: Int, userId: Int, newComment:String, newDate:String, newRating: Int){
        viewModelScope.launch {
            reviewDao.updateReview(restaurantId, userId, newComment, newDate, newRating)
        }
    }
    //cancella una review
    fun deleteReview(review: ReviewEntity){
        viewModelScope.launch {
            reviewDao.delete(review)
        }
    }
    //cancella una review in base alla sua primary key
    fun deleteReviewByIds(restaurantId: Int, userId: Int){
        viewModelScope.launch {
            reviewDao.deleteByIds(restaurantId, userId)
        }
    }



}
//pattern factory
class ReviewViewModelFactory(private val reviewDao: ReviewDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(reviewDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}