package com.example.materialreviews

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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


    // Creo una colonna di RestaurantCard
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        

    ) {
        //HeartToggleButton checked
        if (onlyFavorites) {
            // Estraggo la lista dei ristoranti preferiti
            val data by model.getALlFavorites().observeAsState(emptyList())

            //PlaceHolder in caso non ci siano preferiti
            if (data.isEmpty()) {
                NoFavourites()
            }else{

            data.forEach { restaurantWithImages ->
                // Se sto considerando solo i preferiti, quelli non preferiti appaiono invisibili, mentre i preferiti appaiono e hanno una exit animation
                AnimatedVisibility(
                    restaurantWithImages.restaurant.preferito,
                    exit = shrinkVertically() + fadeOut()
                ) {
                    RestaurantCard(
                        restaurantWithImages.restaurant,
                        onClickSeeAll = onClickSeeAll,
                        onCheckedChange = { it ->
                            model.changeFavoriteState(restaurantWithImages.restaurant.rid, it)
                        },
                        imageUri = restaurantWithImages.images[0].uri,
                        getAverageRating = { model.getAverageRatingOfRestaurant(restaurantWithImages.restaurant.rid) }
                    )
                }
                //credo questo ormai sia inutile ma lo lascio non si sa mai
                //return@forEach  //https://stackoverflow.com/questions/32540947/break-and-continue-in-foreach-in-kotlin
                //se non sto considerando una sublist particolare semplicementre mostro tutti
            }
            }
        }else{
            // Estraggo la lista dei ristoranti
            val data by model.getRestaurantsWithImage().observeAsState(emptyList())
            data.forEach { restaurantWithImages ->
                RestaurantCard(
                    restaurantWithImages.restaurant,
                    onClickSeeAll = onClickSeeAll,
                    onCheckedChange = { it ->
                        model.changeFavoriteState(restaurantWithImages.restaurant.rid, it)
                    },
                    imageUri = restaurantWithImages.images[0].uri,
                    getAverageRating = { model.getAverageRatingOfRestaurant(restaurantWithImages.restaurant.rid) }
                )
            }


        }
    }
}
@ExperimentalMaterial3Api
@Composable
fun LazyListOfRestaurants(
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
    val density = LocalDensity.current
    // Creo una colonna di RestaurantCard
    LazyVerticalGrid(
        modifier = Modifier
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Adaptive(300.dp)


    ) {items(data){
        if (onlyFavorites) {
            AnimatedVisibility(it.restaurant.preferito,
                exit = shrinkVertically() + fadeOut()) {
                RestaurantCard(
                    it.restaurant,
                    onClickSeeAll = onClickSeeAll,
                    onCheckedChange = { x ->
                        model.changeFavoriteState(it.restaurant.rid, x)
                    },
                    imageUri = it.images[0].uri,
                    getAverageRating={model.getAverageRatingOfRestaurant(it.restaurant.rid)}
                )
            }
            //credo questo ormai sia inutile ma lo lascio non si sa mai
              //https://stackoverflow.com/questions/32540947/break-and-continue-in-foreach-in-kotlin
        }//se non sto considerando una sublist particolare semplicementre mostro tutti
        else{
            RestaurantCard(
                it.restaurant,
                onClickSeeAll = onClickSeeAll,
                onCheckedChange = { x ->
                    model.changeFavoriteState(it.restaurant.rid, x)
                },
                imageUri = it.images[0].uri,
                getAverageRating = { model.getAverageRatingOfRestaurant(it.restaurant.rid) }
            )
        }
    }


    /*
        data.forEach { restaurantWithImages ->

            // Se sto considerando solo i preferiti, quelli non preferiti appaiono invisibili, mentre i preferiti appaiono e hanno una exit animation
            if (onlyFavorites) {

                AnimatedVisibility(restaurantWithImages.restaurant.preferito,
                    exit = shrinkVertically() + fadeOut()) {
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
                //credo questo ormai sia inutile ma lo lascio non si sa mai
                return@forEach  //https://stackoverflow.com/questions/32540947/break-and-continue-in-foreach-in-kotlin
            }//se non sto considerando una sublist particolare semplicementre mostro tutti
            else{
                RestaurantCard(
                    restaurantWithImages.restaurant,
                    onClickSeeAll = onClickSeeAll,
                    onCheckedChange = { it ->
                        model.changeFavoriteState(restaurantWithImages.restaurant.rid, it)
                    },
                    imageUri = restaurantWithImages.images[0].uri,
                    getAverageRating = { model.getAverageRatingOfRestaurant(restaurantWithImages.restaurant.rid) }
                )
            }


        }*/
    }

}

//Placeholder nel caso non ci sia nessun preferito
@Composable
fun NoFavourites(){
    Surface( modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.padding(top = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_no_favourites_24),
                contentDescription = "Nessun preferito",
                modifier = Modifier.size(40.dp),
                tint = Color.Gray
            )
            Text(text = "Ancora nessun preferito",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge,
                modifier =  Modifier.padding(bottom = 32.dp)
            )
            //TODO: valutare che sia OK
            Text(text = "Aggiungi qui i preferiti cliccando il cuore sotto al nome dei ristoranti",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier =  Modifier.padding(bottom = 32.dp, start = 40.dp, end = 40.dp)
            )
        }
    }
}
