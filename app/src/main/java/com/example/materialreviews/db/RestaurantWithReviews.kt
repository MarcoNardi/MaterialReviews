package com.example.materialreviews.db

import androidx.room.Embedded
import androidx.room.Relation

//classe pojo per ottenere reviews del ristorante
data class RestaurantWithReviews(
    @Embedded val restaurant: RestaurantEntity,

    @Relation(parentColumn = "rid", entityColumn = "restaurantId")
    val reviews: List<ReviewEntity> = emptyList()
)
