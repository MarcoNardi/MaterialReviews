package com.example.materialreviews.db



import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "images")
class ImageEntity(
    @PrimaryKey(autoGenerate = false)
    val uri: String,
    val restaurantId: Int
)