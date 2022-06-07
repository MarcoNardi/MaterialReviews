package com.example.materialreviews

import android.graphics.Bitmap
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
import com.example.materialreviews.db.*
import kotlinx.coroutines.delay


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
    val context = LocalContext.current

    // Dimensioni dello schermo
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density

    //necessario il remember per rendere il imagedata uno stato del composable così che quando si carica il composable viene rieseguito
    var imageData: Bitmap? by remember {
        mutableStateOf(null)
    }

    //coroutine per caricare le immagini
    LaunchedEffect(key1 = imageUri){
        delay(500)
        imageData = getImageBitmap(imageUri , context)
    }

    val averageRating by getAverageRating().observeAsState()

    // Card con le informazioni
    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        modifier = Modifier
            .clickable { onClickSeeAll(restId) }
            .padding(8.dp)
    ) {
        Column(modifier = Modifier ){

            // "Foto profilo" del ristorante
            //se non è stata caricata la mostro senn+ mostro un indicatore circolare di progresso
            if(imageData!=null){
                Image(
                    bitmap = imageData!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.requiredSize(dpWidth.dp, (dpWidth/16*9).dp)
                )
            }else{
                Box(modifier = Modifier.requiredSize(dpWidth.dp, (dpWidth/16*9).dp),contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }


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

                    // Indirizzo e cuoricino
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement  = Arrangement.SpaceBetween){
                        Column() {
                            Text(
                                text = restCity,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Via $restRoad, $restCivic",
                                Modifier.padding(bottom = 10.dp)
                            )

                        }
                        // Pulsante per salvare il ristorante nei preferiti
                        val icon = Icons.Filled.Favorite
                        IconToggleButton( checked = restaurant.preferito, onCheckedChange = onCheckedChange, modifier=Modifier.requiredSize(48.dp)) {
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
                Spacer(Modifier.weight(1f))


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


