package com.example.materialreviews.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

//salva le info di un ristorante
@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    val rid: Int,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sito") val sito: String,
    @ColumnInfo(name = "orario") val orario: String,
    @ColumnInfo(name = "categoria") val categoria: String,
    @ColumnInfo(name = "preferito") var preferito: Boolean,
    @ColumnInfo(name = "nTelefono") val nTelefono: String,

    @Embedded val address: Address?
)

data class Address(
    val citta: String,
    val via: String,
    val num_civico: Int,
)


