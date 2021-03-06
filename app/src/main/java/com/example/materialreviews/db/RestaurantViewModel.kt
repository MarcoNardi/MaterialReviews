package com.example.materialreviews.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RestaurantViewModel(private val restaurantDao: RestaurantDao) : ViewModel() {
    /**
     *helper per inserire un ristorante
     */

    private fun insertRestaurant(restaurant: RestaurantEntity) {
        viewModelScope.launch{
            restaurantDao.insert(restaurant)
        }
    }

    /**
     *helper per creare un entry
     */
    private fun createRestaurantEntry(id: Int, name: String, sito: String, citta:String, via: String, num_civico: Int, orario:String, preferito: Boolean, categoria: String, nTelefono:String): RestaurantEntity{
        return RestaurantEntity(id, name, sito, orario,categoria,preferito,nTelefono, Address(citta, via, num_civico ) )
    }

    /**
    aggiunge un ristorante
     *
     */
    fun addRestaurant(id: Int, name: String, sito: String, citta:String, via: String, num_civico: Int, orario:String, preferito: Boolean, categoria: String, nTelefono:String){
        insertRestaurant(createRestaurantEntry(id, name, sito, citta, via, num_civico, orario, preferito, categoria, nTelefono))
    }


    /**
    aggiunge un ristorante
     *
     */
    fun addRestaurant(restaurant: RestaurantEntity){
        insertRestaurant(restaurant)
    }
    /**
     *ottiene info di un ristorante
     */
    fun getRestaurant(restaurantId: Int): LiveData<RestaurantEntity>{
        return restaurantDao.findById(restaurantId)
    }

    /**
     *ottiene tutti i ristoranti preferiti
    */
    fun getAllFavorites(): LiveData<List<RestaurantEntity>>{
        return restaurantDao.getAllFavorites()
    }


    /**
     * ottiene tutti i preferiti con le loro immagini
     */
    fun getAllFavoritesWithImages(): LiveData<List<RestaurantWithImages>>{
        return restaurantDao.getAllFavoritesWithImages()
    }
    /**
     *cambia lo stato di preferito in un ristorante
     */
    fun changeFavoriteState(restaurantId: Int, isFavorite: Boolean){
        viewModelScope.launch{
            restaurantDao.updateFavorite(restaurantId, isFavorite)
        }
    }

    /**
     *ottiene lista di tutti i ristoranti
     */
    fun getAllRestaurants() :  LiveData<List<RestaurantEntity>>{
        return restaurantDao.getAll()
    }
    /**
     *ottiene tutte le immagini di un ristorante
     */
    fun getImageOfRestaurant(restaurantId: Int) : LiveData<RestaurantWithImages>{
        return restaurantDao.getImageOfRestaurant(restaurantId)
    }

    /**
     * ottiene tutte le coppie ristorante immagini
     */
    fun getRestaurantsWithImage() : LiveData<List<RestaurantWithImages>>{
        return restaurantDao.getRestaurantsAndImages()
    }
    /**
     *ottiene tutte le coppie (restaurant, reviewsList)
     */
    fun getRestaurantsWithReviews() : LiveData<List<RestaurantWithReviews>>{
        return restaurantDao.getRestaurantsAndReviews()
    }
    /**
     *ottiene le recensioni di un ristorante
     */
    fun getReviewsOfRestaurant(restaurantId: Int): LiveData<RestaurantWithReviews>{
        return  restaurantDao.getReviewsOfRestaurant(restaurantId)
    }
    /**
     *ottiene il rating medio
     */
    fun getAverageRatingOfRestaurant(restaurantId: Int) :LiveData<Float>{
        return restaurantDao.getAverageRating(restaurantId)
    }

    /**
     * ottiene l'uri della prima immagine di un ristorante
     */
    fun getImageUriOfRestaurant(restaurantId: Int):LiveData<String>{
        val imageUri: MutableLiveData<String> by lazy {
            MutableLiveData<String>()
        }
        viewModelScope.launch{
            val restaurantImages=restaurantDao.getImageUriOfRestaurant(restaurantId)
            imageUri.value = restaurantImages.images[0].uri
        }

        return imageUri
    }

    //serve per ottenere dati dell'immagine con coroutine ma non ?? usata perch?? trovato un metodo "migliore"
    fun getImageData(imageUri:String, context: Context): MutableLiveData<Bitmap> {
        val imageBitmap: MutableLiveData<Bitmap> by lazy {
            MutableLiveData<Bitmap>()
        }
        viewModelScope.launch{
            val uri = Uri.parse(imageUri)
            if (Build.VERSION.SDK_INT < 28) {
                imageBitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, uri)

            } else {
                val dataSource =
                    ImageDecoder
                        .createSource(context.contentResolver, uri)

                imageBitmap.value = ImageDecoder.decodeBitmap(dataSource)
            }

        }
        return imageBitmap
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