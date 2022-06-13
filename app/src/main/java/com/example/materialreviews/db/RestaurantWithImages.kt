package com.example.materialreviews.db

import androidx.room.Embedded
import androidx.room.Relation

//classe POJO per ottenere immagini del ristorante
data class RestaurantWithImages(
    @Embedded val restaurant: RestaurantEntity,

    @Relation(parentColumn = "rid", entityColumn = "restaurantId")
    val images: List<ImageEntity> = emptyList()
)
