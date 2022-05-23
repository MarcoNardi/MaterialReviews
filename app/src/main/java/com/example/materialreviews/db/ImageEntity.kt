package com.example.materialreviews.db



import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "images", foreignKeys = [ForeignKey(entity = RestaurantEntity::class, parentColumns = ["rid"], childColumns = ["restaurantId"])])
class ImageEntity(
    @PrimaryKey(autoGenerate = false)
    val uri: String,
    val restaurantId: Int
)