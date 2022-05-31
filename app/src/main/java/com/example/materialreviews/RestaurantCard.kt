package com.example.materialreviews

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*

@ExperimentalMaterial3Api
@Composable
fun  RestaurantCard(
    restaurant: RestaurantEntity,
    onClickSeeAll: (Int) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    getAverageRating: () -> LiveData<Float>,
    imageUri: String
) {
    val restId = restaurant.rid
    val restName = restaurant.name
    val restCity = restaurant.address!!.citta
    val restRoad = restaurant.address.via
    val restCivic = restaurant.address.num_civico

    val restImage = R.drawable.ic_launcher_background
    val context = LocalContext.current

    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    //LocalContext.current.resources.getResourcePackageName(R.drawable.restaurantphoto1)
    //Resources.getResourcePackageName(R.drawable.restaurantphoto1)
    //val imageUri=Uri.parse("android.resource://com.example.materialreviews/drawable/restaurantphoto1")
    //val imageData: Bitmap = getImageBitmap(imageUri, LocalContext.current)

    val restaurantModel: RestaurantViewModel = viewModel(
        factory = RestaurantViewModelFactory(
            AppDatabase.getDatabase(
                LocalContext.current
            ).restaurantDao()
        )
    )
    val imageData = getImageBitmap(imageUri , context)
    val averageRating by getAverageRating().observeAsState()
    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.clickable { onClickSeeAll(restId) }
    ) {
        Column(modifier = Modifier ){
            Image(
                bitmap = imageData!!.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.requiredSize(dpWidth.dp, (dpWidth/16*9).dp)
            )
            // "Foto profilo" del ristorante
            /*Image(
                //painter = painterResource(id = R.drawable.ic_launcher_background)
                bitmap = imageData.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.requiredSize(dpWidth.dp, (dpWidth/16*9).dp)
            )*/

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
                        if(averageRating!=null){
                            RowOfStars(averageRating!!.toInt())
                        }else{
                            RowOfStars(0)
                        }
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


