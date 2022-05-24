package com.example.materialreviews.db

import androidx.room.Embedded
import androidx.room.Relation


data class RestaurantWithReviews(
    @Embedded val restaurant: RestaurantEntity,

    @Relation(parentColumn = "rid", entityColumn = "restaurantId")
    val reviews: List<ReviewEntity> = emptyList()
)
