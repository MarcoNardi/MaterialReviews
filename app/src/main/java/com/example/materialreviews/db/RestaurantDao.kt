package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RestaurantDao {
    //query per ottenere tutti i ristoranti
    @Query("SELECT * FROM restaurants")
    fun getAll(): LiveData<List<RestaurantEntity>>
    //query per ottenere tutti i ristoranti de una lista di Id
    @Query("SELECT * FROM restaurants WHERE rid IN (:restaurantIds)")
    fun loadAllByIds(restaurantIds: IntArray): LiveData<List<RestaurantEntity>>
    //query per trovare info di un ristorante
    @Query("SELECT * FROM restaurants WHERE rid LIKE :id LIMIT 1")
    fun findById(id: Int): LiveData<RestaurantEntity>
    //query per inserire
    @Insert
    suspend fun insert(restaurant: RestaurantEntity)
    //query che cambia lo stato di preferito
    @Query("UPDATE restaurants SET preferito = :isFavorite WHERE rid LIKE :restaurantId")
    suspend fun updateFavorite(restaurantId: Int, isFavorite: Boolean)
    //query che cancella un ristorante
    @Delete
    suspend fun delete(restaurant: RestaurantEntity)
    //query che ottiene tutti i ristoranti marcati come preferiti
    @Query("SELECT * FROM restaurants WHERE preferito = 1" )
    fun getAllFavorites(): LiveData<List<RestaurantWithImages>>

    //query che ottiene tutti i ristoranti con tutte le immagini
    @Transaction
    @Query("SELECT * FROM restaurants")
    fun getRestaurantsAndImages(): LiveData<List<RestaurantWithImages>>
    //query che ottiene tutte le immagini di un ristorante
    @Transaction
    @Query("SELECT * FROM restaurants WHERE rid = :restaurantId")
    fun getImageOfRestaurant(restaurantId: Int): LiveData<RestaurantWithImages>
    //query che ottiene tutti i ristoranti con tutte le reviews
    @Transaction
    @Query("SELECT * FROM restaurants")
    fun getRestaurantsAndReviews(): LiveData<List<RestaurantWithReviews>>
    //query che ottiene tutte le reviews di un ristorante
    @Transaction
    @Query("SELECT * FROM restaurants WHERE rid LIKE :restaurantId")
    fun getReviewsOfRestaurant(restaurantId: Int): LiveData<RestaurantWithReviews>

    //query che ottiene il rating medio
    @Query("SELECT AVG(rating) FROM reviews WHERE restaurantId=:restaurantId")
    fun getAverageRating(restaurantId: Int): LiveData<Float>

    //query per ottenere uri di prima immagine (semplifica lavoro)
    @Transaction
    @Query("SELECT * FROM restaurants WHERE rid = :restaurantId")
    suspend fun getImageUriOfRestaurant(restaurantId: Int): RestaurantWithImages

}