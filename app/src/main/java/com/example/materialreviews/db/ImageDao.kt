package com.example.materialreviews.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageDao {
    //inserire un'immagine
    @Insert()
    suspend fun insert(img: ImageEntity)
    //rimuovere un'immagine
    @Delete
    suspend fun delete(img: ImageEntity)
}