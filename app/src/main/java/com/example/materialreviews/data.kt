package com.example.materialreviews

import com.example.materialreviews.db.Address
import com.example.materialreviews.db.RestaurantEntity
import com.example.materialreviews.db.ReviewEntity
import com.example.materialreviews.db.UserEntity


val ristorante1 = RestaurantEntity(1, "da luca", "www.luca.pizza", "12.30-23.00", "pizzeria", false, Address("Padova", "padova", 1))
val ristorante2 = ristorante1.copy(rid=2, name="Da Fabio")
val ristorante3 = ristorante1.copy(rid=3, name="Da Ciccio")
val ristorante4 = ristorante1.copy(rid=4, name="Da Francesco")
val ristorante5 = ristorante1.copy(rid=5, name="Dal fratm")


fun getInitialRestaurantsData() : List<RestaurantEntity>{
    return listOf<RestaurantEntity>(ristorante1, ristorante2, ristorante3, ristorante4, ristorante5)
}

val utente1= UserEntity(1, "Marco", "Nardi","")
val utente2= utente1.copy(2, lastName = "Trincanato")
val utente3= UserEntity(3, "Eli", "Ferin", "")

fun getInitialUsersData() : List<UserEntity>{
    return listOf<UserEntity>(utente1, utente2, utente3)
}

val review11= ReviewEntity(5,"me piasce, voto diesci",1,1)
val review12= ReviewEntity(1,"pizza non buona, proprietario maleducato e altre cose non so che scrivere bla blaaltre cose non so che scrivere bla blaaltre cose non so che scrivere bla bla",1,2)
val review13= ReviewEntity(3,"lasagne buone quasi come quelle della nonna, kebab ottimo, sushi spettacolare con salsa al ragu'",1,3)
val review21= ReviewEntity(1,"semplicemente brutto",2,1)
val review22= ReviewEntity(5,"diffidate delle review altrui, la pizza è molto buona, la prendon surgelata dal famila, 10/10",2,2)
val review23= ReviewEntity(5,"sushi al ragu interessante, il kebab pero scadente sushi al ragu interessante, il kebab pero scadente sushi al ragu interessante, il kebab pero scadente sushi al ragu interessante, il kebab pero scadente ",2,3)
val review31= ReviewEntity(4,"non so piu che scrivere su ste cose help ",3,1)
val review32= ReviewEntity(2,"sushi al ragu un po meh, la nonna in cucina e' molto brava pero'",3,2)
val review33= ReviewEntity(5,"fine bastra lorem ipsum e altre buone cose lorem ipsum e altre buone cose lorem ipsum e altre buone cose lorem ipsum e altre buone cose",3,3)


fun getInitialReviewsData() :List<ReviewEntity> {
    return listOf<ReviewEntity>(review11, review12, review13, review21, review22, review23, review31, review32, review33)
}

