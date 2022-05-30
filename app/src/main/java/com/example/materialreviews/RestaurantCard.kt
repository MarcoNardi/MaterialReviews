package com.example.materialreviews

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*

@ExperimentalMaterial3Api
@Composable
fun  RestaurantCard(
    restaurant: RestaurantEntity,
    onClickSeeAll: (Int) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val restId = restaurant.rid
    val restName = restaurant.name
    val restCity = restaurant.address!!.citta
    val restRoad = restaurant.address.via
    val restCivic = restaurant.address.num_civico
    val stars = 4
    val restImage = R.drawable.ic_launcher_background

    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.clickable { onClickSeeAll(restId) }
    ) {
        Column(modifier = Modifier ){

            // "Foto profilo" del ristorante
            Image(
                painter = painterResource(restImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            // Altri elementi
            Row(
                Modifier.padding(start = 3.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.padding(3.dp)) {
                    // Nome del ristorante
                    Text(
                        text = restName,
                        Modifier.padding(3.dp),
                        style = MaterialTheme.typography.displaySmall
                    )

                    // Stelle della recensione
                    Row {
                        RowOfStars(stars)
                        //Spacer(Modifier.weight(1f))
                    }

                    Text(
                        text = restCity,
                        Modifier.padding(start = 3.dp, bottom = 3.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Via $restRoad, $restCivic",
                        Modifier.padding(start = 3.dp, bottom = 3.dp),
                        style = MaterialTheme.typography.bodySmall
                    )

                }
                Spacer(Modifier.weight(1f))

                // Pulsante per salvare il ristorante nei preferiti
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
fun ListOfRestaurants(
    model: RestaurantViewModel = viewModel(
        factory = RestaurantViewModelFactory(
            restaurantDao = AppDatabase.getDatabase(LocalContext.current).restaurantDao()
        )
    ),
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
fun RestaurantCardPreview() {
    val model: RestaurantViewModel = viewModel(
        factory = RestaurantViewModelFactory(
            restaurantDao = AppDatabase.getDatabase(LocalContext.current).restaurantDao()
        )
    )

    val restaurantList by model.getAllRestaurants().observeAsState(emptyList())
    val restaurantEntity = restaurantList[0]

    RestaurantCard(restaurant = restaurantEntity, onClickSeeAll = {}, onCheckedChange = {})
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun RestaurantDetailsAndReviews(
    restId: Int = 1,
    restaurantModel: RestaurantViewModel = viewModel(
        factory = RestaurantViewModelFactory(
            AppDatabase.getDatabase(
                LocalContext.current
            ).restaurantDao()
        )
    ),
    userModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            AppDatabase.getDatabase(
                LocalContext.current
            ).userDao()
        )
    )
) {
    val restaurantWithReviews by restaurantModel.getReviewsOfRestaurant(restId).observeAsState()

    // Indica se mostrare il dialog per aggiungere una recensione
    var showAddReviewDialog by remember { mutableStateOf(false) }
    if (showAddReviewDialog) {
        AddReviewDialog(
            returnReviewEntity = {},
            onDismissClick = {
                showAddReviewDialog = false
            }
        )
    }

    if(restaurantWithReviews!=null){
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            //modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            item() {
                RestaurantDetails(
                    restId = 1,
                    addReviewButtonOnClick = {
                        showAddReviewDialog = true
                    }
                )
            }
            if(restaurantWithReviews !=null) {
                items(restaurantWithReviews!!.reviews) { review ->
                    //val user by userModel.getUser(review.uid).observeAsState()
                    //il passaggio di user è diventata una lambda
                    ReviewCard(review) {
                        userModel.getUser(it)
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun RestaurantDetails(
    restId: Int?,
    addReviewButtonOnClick: () -> Unit
) {
    val context = LocalContext.current

    Surface() {
        Column(modifier = Modifier.padding(5.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically){
                Column() {
                    // Nome del ristorante
                    Text(
                        text = "RestaurantDetails",
                        style = MaterialTheme.typography.displaySmall
                    )
                    // Valutazione media
                    RowOfStars(4)
                }

                Spacer(Modifier.weight(1f))

                // Pulsante per salvarlo nei preferiti
                val icon = Icons.Filled.Favorite
                var checked by remember { mutableStateOf(false) }
                IconToggleButton( checked = checked, onCheckedChange = {checked = it}, modifier = Modifier.padding(end = 12.dp)) {
                    val tint by animateColorAsState(if (checked) Color.Red else Color.LightGray)
                    Icon(
                        imageVector = icon,
                        contentDescription = "Aggiungi a elementi salvati",
                        tint = tint,
                        modifier = Modifier.size(35.dp)
                    )

                }
            }
            Text(text = "Posizione",
                Modifier.padding(top = 3.dp)
            )
            Text(text = "Posizione2",
                Modifier.padding(top = 3.dp)
            )
            Row(modifier = Modifier.padding(top = 3.dp)) {
                IconButton(
                    modifier = Modifier.padding(end = 12.dp),
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:<3467640861")
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Chiama",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )

                }
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://retireinprogress.com/404books/")
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_language_24),
                        contentDescription = "Vai al sito",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )

                }

            }

            // Pulsante per aggiungere una recensione al ristorante
            AddReviewButton(onClick = addReviewButtonOnClick)
        }
    }
}

@Preview
@Composable
fun AddReviewButton(
    onClick: ()->Unit = {}
) {
    FilledTonalButton(onClick = onClick) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Scrivi recensione"
            )
            Text("Scrivi recensione")
        }
    }
}

/*
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
*/


