package com.example.materialreviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.AppDatabase
import com.example.materialreviews.db.RestaurantViewModel
import com.example.materialreviews.db.RestaurantViewModelFactory

/**
 * Data un RestaurantModel produce una lista di RestaurantCard, eventualmente filtrando solo i preferiti
 */
@ExperimentalMaterial3Api
@Composable
fun ListOfRestaurants(
    model: RestaurantViewModel = viewModel(
        factory = RestaurantViewModelFactory(
            restaurantDao = AppDatabase.getDatabase(LocalContext.current).restaurantDao()
        )
    ),
    onClickSeeAll: (Int) -> Unit = {},
    onlyFavorites:Boolean = false
) {
    // Estraggo la lista dei ristoranti
    val data by model.getRestaurantsWithImage().observeAsState(emptyList())

    // Creo una colonna di RestaurantCard
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        data.forEach { restaurantWithImages ->

            // Se sto considerando solo i preferiti, salto questo ristorante
            if (onlyFavorites && !restaurantWithImages.restaurant.preferito) {
                return@forEach  //https://stackoverflow.com/questions/32540947/break-and-continue-in-foreach-in-kotlin
            }

            // Card con alcune informazioni del ristorante
            // Se cliccata apre la RestaurantDetailsAndReviews corrispondente
            RestaurantCard(
                restaurantWithImages.restaurant,
                onClickSeeAll = onClickSeeAll,
                onCheckedChange = { it ->
                    model.changeFavoriteState(restaurantWithImages.restaurant.rid, it)
                },
                imageUri = restaurantWithImages.images[0].uri,
                getAverageRating={model.getAverageRatingOfRestaurant(restaurantWithImages.restaurant.rid)}
            )
        }
    }
}