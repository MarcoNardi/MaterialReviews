package com.example.materialreviews

import android.media.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Restaurant(
    val image: Nothing? = null,
    val name: String = "NoNome",
    val position: String = "NoPos",
    val rating: Int = 3
)
@ExperimentalMaterial3Api
@Composable
fun RestaurantCard(restaurant: Restaurant,
                   modifier: Modifier = Modifier) {
    val restName = restaurant.name
    val restPosition = restaurant.position
    val stars = restaurant.rating
    val restImage = restaurant.image

    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(10.dp)
    ) {
        Column(modifier = modifier ){
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
            Text(text = restPosition,
                Modifier.padding(start = 3.dp, bottom = 3.dp),
                style = MaterialTheme.typography.bodyMedium)
            }
                Spacer(Modifier.weight(1f))

            val icon = Icons.Filled.FavoriteBorder
            val tint = Color.LightGray
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
@ExperimentalMaterial3Api
@Composable
fun ListOfRestaurants(restaurants: List<Restaurant>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        items(restaurants) { restaurant ->
            RestaurantCard( restaurant )
        }
    }
}
    @ExperimentalMaterial3Api
    @Preview
    @Composable
    fun ListOfRestaurantsPreview() {
        val restaurants = listOf(Restaurant(), Restaurant(), Restaurant())
        ListOfRestaurants(restaurants)
    }