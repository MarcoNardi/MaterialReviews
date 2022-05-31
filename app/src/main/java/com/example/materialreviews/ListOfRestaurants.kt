package com.example.materialreviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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

@ExperimentalMaterial3Api
@Composable
fun ListOfRestaurants(
    model: RestaurantViewModel = viewModel(
        factory = RestaurantViewModelFactory(
            restaurantDao = AppDatabase.getDatabase(LocalContext.current).restaurantDao()
        )
    ),
    onClickSeeAll: (Int) -> Unit = {}, onlyFavorites:Boolean=false
) {
    //val data = getInitialRestaurantsData()
    val data by model.getRestaurantsWithImage().observeAsState(emptyList())
    /*
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        items(data) { tuple ->
            RestaurantCard( tuple.restaurant, onClickSeeAll = onClickSeeAll , onCheckedChange = {it->
                model.changeFavoriteState(tuple.restaurant.rid, it)
            }, imageUri=tuple.images[0].uri,
                getAverageRating={model.getAverageRatingOfRestaurant(tuple.restaurant.rid)})
        }
    }*/


    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        data.forEach { restaurantWithImages ->
            if(onlyFavorites ){
                if(restaurantWithImages.restaurant.preferito){
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
            }else{
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
}