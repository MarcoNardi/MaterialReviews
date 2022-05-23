package com.example.materialreviews.db

import androidx.room.Embedded
import androidx.room.Relation


data class RestaurantWithImages(
    @Embedded val restaurant: RestaurantEntity,

    @Relation(parentColumn = "rid", entityColumn = "restaurantId")
    val images: List<ImageEntity> = emptyList()
)
