package com.example.materialreviews

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.AppDatabase
import com.example.materialreviews.db.RestaurantEntity
import com.example.materialreviews.db.RestaurantViewModel
import com.example.materialreviews.db.RestaurantViewModelFactory

data class Restaurant(
    val image: Nothing? = null,
    val name: String = "NoNome",
    val position: String = "NoPos",
    val rating: Int = 3
)

@ExperimentalMaterial3Api
@Composable
fun  RestaurantCard(restaurant: RestaurantEntity,
                   onClickSeeAll: (Int) -> Unit,
                    onCheckedChange:(Boolean)-> Unit
                   ) {
    val restId = restaurant.rid
    val restName = restaurant.name!!
    val restCity = restaurant.address!!.citta!!
    val restRoad = restaurant.address.via!!
    val restCivic = restaurant.address.num_civico!!

    val stars = 4
    val restImage = R.drawable.ic_launcher_background
    val context = LocalContext.current

    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.clickable { onClickSeeAll(restId) }
    ) {
        Column(modifier = Modifier ){
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Row(Modifier.padding(start = 3.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.padding(3.dp)) {
                    Row(){
                        for (i in 0..4) {
                            val icon = Icons.Filled.Star
                            val tint = if (i<stars) Color(252, 185, 0) else Color.LightGray
                            Icon(
                                imageVector = icon,
                                tint = tint,
                                contentDescription = "Star",
                            )
                        }
                        //Spacer(Modifier.weight(1f))
                    }
                    Text(text = restName,
                        Modifier.padding(3.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = restCity,
                        Modifier.padding(start = 3.dp, bottom = 3.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(text = "Via $restRoad, $restCivic",
                        Modifier.padding(start = 3.dp, bottom = 3.dp),
                        style = MaterialTheme.typography.bodySmall
                    )

                }
                Spacer(Modifier.weight(1f))

                val icon = Icons.Filled.Favorite


                IconToggleButton( checked = restaurant.preferito, onCheckedChange = onCheckedChange) {
                    val tint by animateColorAsState(if (restaurant.preferito) Color.Red else Color.LightGray)
                    Icon(

                        imageVector = icon,
                        contentDescription = "Aggiungi a elementi salvati",
                        tint = tint,
                        modifier = Modifier.size(35.dp)
                    )
                    
                }

            }
        }
    }

}
@ExperimentalMaterial3Api
@Composable
fun ListOfRestaurants(model: RestaurantViewModel = viewModel(factory = RestaurantViewModelFactory(restaurantDao =  AppDatabase.getDatabase(LocalContext.current).restaurantDao())),
                      onClickSeeAll: (Int) -> Unit = {}
) {
    //val data = getInitialRestaurantsData()
    val data by model.getAllRestaurants().observeAsState(emptyList())
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        items(data) { restaurant ->
            RestaurantCard( restaurant, onClickSeeAll = onClickSeeAll , onCheckedChange = {it->
                model.changeFavoriteState(restaurant.rid, it)
            })
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun ListOfRestaurantsPreview() {
    val restaurants = listOf(Restaurant(), Restaurant(), Restaurant())
    //ListOfRestaurants(restaurants)
}


@ExperimentalMaterial3Api
@Composable
fun ListOfRestaurantsPreview(model: RestaurantViewModel) {
    //val restaurants = listOf(Restaurant(), Restaurant(), Restaurant())
    //ListOfRestaurants(model.getAllRestaurants())
}