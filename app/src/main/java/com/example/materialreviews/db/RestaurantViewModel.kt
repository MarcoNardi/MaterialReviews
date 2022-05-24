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

    private fun createRestaurantEntry(id: Int, name: String, sito: String, citta:String, via: String, num_civico: Int, orario:String): RestaurantEntity{
        return RestaurantEntity(id, name, sito, orario, Address(citta, via, num_civico ) )
    }

    fun addRestaurant(id: Int, name: String, sito: String, citta:String, via: String, num_civico: Int, orario:String){
        insertRestaurant(createRestaurantEntry(id, name, sito, citta, via, num_civico, orario ))
    }


    fun addRestaurant(restaurant: RestaurantEntity){
        insertRestaurant(restaurant)
    }

    fun getAllRestaurants() :  LiveData<List<RestaurantEntity>>{
        return restaurantDao.getAll()
    }

    fun getRestaurantsWithImage() : LiveData<List<RestaurantWithImages>>{
        return restaurantDao.getRestaurantsAndImages()
    }
    fun getRestaurantsWithReviews() : LiveData<List<RestaurantWithReviews>>{
        return restaurantDao.getRestaurantsAndReviews()
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