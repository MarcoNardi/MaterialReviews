package com.example.materialreviews

import com.example.materialreviews.db.Address
import com.example.materialreviews.db.RestaurantEntity


val ristorante1 = RestaurantEntity(1, "da luca", "www.luca.pizza", "12.30-23.00", Address("Padova", "padova", 1))
val ristorante2 = ristorante1.copy(rid=2, name="Da Fabio")
val ristorante3 = ristorante1.copy(rid=2, name="Da Ciccio")
val ristorante4 = ristorante1.copy(rid=2, name="Da Francesco")

fun getInitialrestaurantsData() : List<RestaurantEntity>{
    return listOf<RestaurantEntity>(ristorante1, ristorante2, ristorante3, ristorante4)
}

