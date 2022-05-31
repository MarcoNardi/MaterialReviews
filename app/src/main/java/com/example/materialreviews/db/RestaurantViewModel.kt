package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RestaurantViewModel(private val restaurantDao: RestaurantDao) : ViewModel() {
    private fun insertRestaurant(restaurant: RestaurantEntity) {
        viewModelScope.launch{
            restaurantDao.insert(restaurant)
        }
    }

    private fun createRestaurantEntry(id: Int, name: String, sito: String, citta:String, via: String, num_civico: Int, orario:String, preferito: Boolean, categoria: String, nTelefono:String): RestaurantEntity{
        return RestaurantEntity(id, name, sito, orario,categoria,preferito,nTelefono, Address(citta, via, num_civico ) )
    }
    //add a restaurant
    fun addRestaurant(id: Int, name: String, sito: String, citta:String, via: String, num_civico: Int, orario:String, preferito: Boolean, categoria: String, nTelefono:String){
        insertRestaurant(createRestaurantEntry(id, name, sito, citta, via, num_civico, orario, preferito, categoria, nTelefono))
    }

    //add a restaurant
    fun addRestaurant(restaurant: RestaurantEntity){
        insertRestaurant(restaurant)
    }

    fun getRestaurant(restaurantId: Int): LiveData<RestaurantEntity>{
        return restaurantDao.findById(restaurantId)
    }

    //get all favorite restaurants
    fun getALlFavorites(): LiveData<List<RestaurantEntity>>{
        return restaurantDao.getAllFavorites()
    }
    //change the favorite state of a certain restaurant
    fun changeFavoriteState(restaurantId: Int, isFavorite: Boolean){
        viewModelScope.launch{
            restaurantDao.updateFavorite(restaurantId, isFavorite)
        }
    }

    //get list of all restaurants
    fun getAllRestaurants() :  LiveData<List<RestaurantEntity>>{
        return restaurantDao.getAll()
    }
    //get images of a certain restaurant
    fun getImageOfRestaurant(restaurantId: Int) : LiveData<RestaurantWithImages>{
        return restaurantDao.getImageOfRestaurant(restaurantId)
    }
    //get all pairs (restaurant, imageList)
    fun getRestaurantsWithImage() : LiveData<List<RestaurantWithImages>>{
        return restaurantDao.getRestaurantsAndImages()
    }
    //get all pairs (restaurant, reviewsList)
    fun getRestaurantsWithReviews() : LiveData<List<RestaurantWithReviews>>{
        return restaurantDao.getRestaurantsAndReviews()
    }
    //get reviews of a certain restaurant
    fun getReviewsOfRestaurant(restaurantId: Int): LiveData<RestaurantWithReviews>{
        return  restaurantDao.getReviewsOfRestaurant(restaurantId)
    }
    fun getAverageRatingOfRestaurant(restaurantId: Int) :LiveData<Float>{
        return restaurantDao.getAverageRating(restaurantId)
    }
}

class RestaurantViewModelFactory(private val restaurantDao: RestaurantDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RestaurantViewModel(restaurantDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}