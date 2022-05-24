package com.example.materialreviews.db

import androidx.room.Embedded
import androidx.room.Relation


data class UserWithReviews(
    @Embedded val user: UserEntity,

    @Relation(parentColumn = "uid", entityColumn = "userId")
    val reviews: List<ReviewEntity> = emptyList()
)
