package com.example.materialreviews

import com.example.materialreviews.db.Address
import com.example.materialreviews.db.RestaurantEntity
import com.example.materialreviews.db.UserEntity


val ristorante1 = RestaurantEntity(1, "da luca", "www.luca.pizza", "12.30-23.00", Address("Padova", "padova", 1))
val ristorante2 = ristorante1.copy(rid=2, name="Da Fabio")
val ristorante3 = ristorante1.copy(rid=3, name="Da Ciccio")
val ristorante4 = ristorante1.copy(rid=4, name="Da Francesco")


fun getInitialRestaurantsData() : List<RestaurantEntity>{
    return listOf<RestaurantEntity>(ristorante1, ristorante2, ristorante3, ristorante4)
}

val utente1= UserEntity(1, "Marco", "Nardi")
val utente2= utente1.copy(2, lastName = "Trincanato")
val utente3= UserEntity(3, "Eli", "Ferin")

fun getInitialUsersData() : List<UserEntity>{
    return listOf<UserEntity>(utente1, utente2, utente3)
}
