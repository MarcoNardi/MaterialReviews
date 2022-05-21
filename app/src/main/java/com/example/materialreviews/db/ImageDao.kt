package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageDao {
    @Insert
    suspend fun insert(img: ImageEntity)

    @Delete
    suspend fun delete(img: ImageEntity)
}