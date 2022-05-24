package com.example.materialreviews.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "reviews", foreignKeys = [
    ForeignKey(entity=UserEntity::class, parentColumns = ["uid"], childColumns = ["userId"]),
    ForeignKey(entity=RestaurantEntity::class, parentColumns = ["rid"], childColumns = ["restaurantId"])
],
    primaryKeys = ["userId", "restaurantId"]
)
data class ReviewEntity(
    @ColumnInfo(name="rating") val rating: Int,
    @ColumnInfo(name = "userId") val uid: Int,
    @ColumnInfo(name = "restaurantId") val rid: Int
)