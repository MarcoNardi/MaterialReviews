package com.example.materialreviews

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
                    onCheckedChange:(Boolean)-> Unit, imageUri: String
                   ) {
    val restId = restaurant.rid
    val restName = restaurant.name
    val restCity = restaurant.address!!.citta
    val restRoad = restaurant.address.via
    val restCivic = restaurant.address.num_civico

    val stars = 4
    val restImage = R.drawable.ic_launcher_background
    val context = LocalContext.current

    //LocalContext.current.resources.getResourcePackageName(R.drawable.restaurantphoto1)
    //Resources.getResourcePackageName(R.drawable.restaurantphoto1)
    //val imageUri=Uri.parse("android.resource://com.example.materialreviews/drawable/restaurantphoto1")
    val uri=Uri.parse(imageUri)
    val displayMetrics = context.resources.displayMetrics
    val dpHeight = displayMetrics.heightPixels / displayMetrics.density
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    val imageData: Bitmap
    if (Build.VERSION.SDK_INT < 28) {
        imageData= MediaStore.Images
            .Media.getBitmap(LocalContext.current.contentResolver, uri)

    } else {
        val dataSource =
            ImageDecoder
                .createSource(LocalContext.current.contentResolver, uri)

        imageData= ImageDecoder.decodeBitmap(dataSource!!)
    }
    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.clickable { onClickSeeAll(restId) }
    ) {
        Column(modifier = Modifier ){
            Image(
                //painter = painterResource(id = R.drawable.ic_launcher_background)
                bitmap = imageData.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.requiredSize(dpWidth.dp, (dpWidth/16*9).dp)
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
    val data by model.getRestaurantsWithImage().observeAsState(emptyList())
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        items(data) { tuple ->
            RestaurantCard( tuple.restaurant, onClickSeeAll = onClickSeeAll , onCheckedChange = {it->
                model.changeFavoriteState(tuple.restaurant.rid, it)
            }, imageUri=tuple.images[0].uri)
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



