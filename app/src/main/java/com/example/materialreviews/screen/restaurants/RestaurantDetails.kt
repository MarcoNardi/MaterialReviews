package com.example.materialreviews

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.example.materialreviews.ui.theme.currentColorScheme
import com.example.materialreviews.util.RowOfStars

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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // "Foto profilo" del ristorante
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
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

                }

                Spacer(Modifier.weight(1f))


            }


            Row(Modifier.fillMaxWidth(),
                horizontalArrangement  = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Column() {
                    // Valutazione media
                    if (averageRating != null) {
                        RowOfStars(averageRating!!.toInt())
                    } else {
                        RowOfStars(0)
                    }

                    // Indirizzo
                    Text(
                        text = restaurant.address?.citta ?: "citta",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Via " + restaurant.address?.via!! + " " + restaurant.address?.num_civico!!.toString(),
                    )
                }
                // Pulsante per salvarlo nei preferiti
                val icon = Icons.Filled.Favorite
                IconToggleButton(
                    checked = restaurant.preferito,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    val tint by animateColorAsState(if (restaurant.preferito) Color.Red else Color.LightGray)
                    Icon(
                        imageVector = icon,
                        contentDescription = "Aggiungi a elementi salvati",
                        tint = tint,
                        modifier = Modifier.size(36.dp)
                    )

                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                // Telefona => apre il dialer
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:<${restaurant.nTelefono}")
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = currentColorScheme.secondary)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Chiama",
                            //tint = Color.Gray,
                            //modifier = Modifier.size(25.dp)
                        )
                        Text("Chiama")
                    }
                }

                // Visita il sito => apre il browser
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://" + restaurant.sito)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = currentColorScheme.secondary)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_link_web_24),
                            contentDescription = "Vai al sito",
                        )
                        Text("Sito")
                    }
                }

                // Visualizza sulla mappa => Apre Maps
                Button(
                    onClick = {
                        val gmmIntentUri = Uri.parse("geo:0,0?q=${restaurant.address!!.num_civico} ${restaurant.address!!.via}, ${restaurant.address!!.citta}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = currentColorScheme.secondary)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = "Collegamento maps",
                        )
                        Text("Mappa")
                    }
                }

            }
        }
    }
}