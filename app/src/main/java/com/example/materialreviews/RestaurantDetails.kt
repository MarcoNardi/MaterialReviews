package com.example.materialreviews

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.materialreviews.db.RestaurantEntity

/**
 * Card riassuntiva di un ristorante
 */
@ExperimentalMaterial3Api
@Composable
fun RestaurantDetails(
    restaurant: RestaurantEntity,
    imageUri: String,
    onCheckedChange: (Boolean) -> Unit,
    getAverageRating: () -> LiveData<Float>,
    addReviewButtonOnClick: () -> Unit
) {
    val context = LocalContext.current

    // Immagine di sfondo del ristorante
    val imageData = getImageBitmap(imageUri, context)

    // Dimensioni dello schermo
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density

    // Media delle recensioni
    val averageRating by getAverageRating().observeAsState()

    Surface() {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // "Foto profilo" del ristorante
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    bitmap = imageData!!.asImageBitmap(),
                    contentDescription = "Restaurant image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredSize(dpWidth.dp, (dpWidth/4*3).dp)
                )
            }

            // Informazioni del ristorante
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

            // Indirizzo -> collegamento google maps
            Row(Modifier.clickable {
                val gmmIntentUri = Uri.parse("geo:0,0?q=${restaurant.address!!.num_civico} ${restaurant.address!!.via}, ${restaurant.address!!.citta}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
                }, 
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Collegamento maps",
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column() {
                    Text(
                        text = restaurant.address?.citta ?: "citta",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Via " + restaurant.address?.via!! + " " + restaurant.address?.num_civico!!.toString(),
                    )
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                // Telefona => apre il dialer
                FilledTonalButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:<${restaurant.nTelefono}")
                        context.startActivity(intent)
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Chiama",
                            //tint = Color.Gray,
                            //modifier = Modifier.size(25.dp)
                        )
                        Text("Telefona")
                    }
                }

                // Visita il sito => apre il browser
                FilledTonalButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://" + restaurant.sito)
                        context.startActivity(intent)
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_language_24),
                            contentDescription = "Vai al sito",
                        )
                        Text("Visita il sito")
                    }
                }

            }

            // Pulsante per aggiungere una recensione al ristorante
            // AddReviewButton(onClick = addReviewButtonOnClick)
        }
    }
}