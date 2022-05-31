package com.example.materialreviews

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.materialreviews.db.RestaurantEntity

@ExperimentalMaterial3Api
@Composable
fun RestaurantDetails(restaurant: RestaurantEntity, imageUri: String, onCheckedChange: (Boolean) -> Unit, getAverageRating:()-> LiveData<Float>,
                      addReviewButtonOnClick: () -> Unit
) {
    val context = LocalContext.current

    val imageData = getImageBitmap(imageUri, context)
    val averageRating by getAverageRating().observeAsState()
    Surface() {
        Column(modifier = Modifier.padding(5.dp)) {
            Image(
                bitmap = imageData!!.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    // Nome del ristorante
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.displaySmall
                    )
                    // Valutazione media
                    if (averageRating != null) {
                        RowOfStars(averageRating!!.toInt())
                    } else {
                        RowOfStars(0)
                    }
                }

                Spacer(Modifier.weight(1f))

                // Pulsante per salvarlo nei preferiti
                val icon = Icons.Filled.Favorite
                IconToggleButton(
                    checked = restaurant.preferito,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    val tint by animateColorAsState(if (restaurant.preferito) Color.Red else Color.LightGray)
                    Icon(
                        imageVector = icon,
                        contentDescription = "Aggiungi a elementi salvati",
                        tint = tint,
                        modifier = Modifier.size(35.dp)
                    )

                }
            }
            Text(
                text = restaurant.address?.citta ?: "citta",
                Modifier.padding(top = 3.dp)
            )
            Text(
                text = "Via " + restaurant.address?.via!! + " " + restaurant.address?.num_civico!!.toString(),
                Modifier.padding(top = 3.dp)
            )
            Row(modifier = Modifier.padding(top = 3.dp)) {
                IconButton(
                    modifier = Modifier.padding(end = 12.dp),
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:<${restaurant.nTelefono}")
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
                        intent.data = Uri.parse("https://" + restaurant.sito)
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