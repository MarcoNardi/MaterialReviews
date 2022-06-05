package com.example.materialreviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*


@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun UserReviewCard(
    review: ReviewEntity,
    restaurantViewModel: RestaurantViewModel,
    onClickSeeRestaurant: (Int) -> Unit
) {

    val stars = review.rating
    val comment = review.review
    val date = review.date

    val reviewViewModel: ReviewViewModel = viewModel(
        factory = ReviewViewModelFactory(
            AppDatabase.getDatabase(LocalContext.current).reviewDao()
        )
    )

    val restId = review.rid
    val restaurant by restaurantViewModel.getRestaurant(restId).observeAsState()
    val restPictureURI by restaurantViewModel.getImageUriOfRestaurant(restId).observeAsState()

    if (restaurant != null) {

        val restName = restaurant!!.name
        val restCity = restaurant!!.address!!.citta
        val restCivic = restaurant!!.address!!.num_civico
        val restRoute = restaurant!!.address!!.via

        // Dialog per eliminare la review
        var openDialog by remember { mutableStateOf(false) }
        if (openDialog) {

            AlertDialog(
                onDismissRequest = { openDialog = false },
                modifier = Modifier.fillMaxWidth(0.9f),
                title = {
                    Text(text = "Sicuro di voler eliminare la tua recensione?")
                },
                // Posso mettere dentro qualsiasi composable
                text = {
                    Row() {
                        Text(text = "Nessuno potrà più vederla e non sarà più possibile recuperarla")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog = false
                            reviewViewModel.deleteReview(review)
                        }
                    ) {
                        Text(
                            "Elimina",
                        )
                    }

                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog = false
                        }
                    ) {
                        Text(
                            "Annulla",
                            textAlign = TextAlign.Center
                        )
                    }
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            )
        }

        // Card che mostra il nome del ristorante e tutte le infomazioni della review
        ElevatedCard(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .padding(5.dp)
                .clickable { onClickSeeRestaurant(restId) }
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(restPictureURI!=null){
                        ProfilePicture(size = 60.dp, restPictureURI!!)
                    }else{
                        ProfilePicture(size =60.dp)
                    }
                    Column() {
                        Text(
                            text = restName,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "$restCity, Via  $restRoute, $restCivic ",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Stelline e data
                Row() {
                    RowOfStars(stars)
                    Spacer(Modifier.weight(1f))
                    Text(text = date)
                }

                // Testo della recensione
                Text(text = comment)

                // Pulsante per eliminare la recensione
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { openDialog = true }) {
                        Text(text = "Elimina")
                    }
                }
            }
        }
    }
}