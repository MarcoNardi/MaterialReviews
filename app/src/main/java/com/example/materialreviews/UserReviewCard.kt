package com.example.materialreviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
fun UserReviewCard(review: ReviewEntity,
                    model: RestaurantViewModel,
                    onClickSeeRestaurant: (Int) -> Unit) {

    val stars = review.rating
    val comment = review.review
    val date = review.date

    var openDialog by remember { mutableStateOf(false) }
    val reviewViewModel: ReviewViewModel = viewModel(
        factory = ReviewViewModelFactory(
            AppDatabase.getDatabase(LocalContext.current).reviewDao()
        )
    )

    val restId = review.rid
    val restaurant by model.getRestaurant(restId).observeAsState()
    if (restaurant != null) {
        val restName = restaurant!!.name
        val restCity = restaurant!!.address!!.citta
        val restCivic = restaurant!!.address!!.num_civico
        val restRoute = restaurant!!.address!!.via
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
                    TextButton(
                        onClick = {
                            openDialog = false
                            reviewViewModel.deleteReview(review)
                        }
                    ) {
                        Text(
                            "ELIMINA",
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
                            "ANNULLA",
                            textAlign = TextAlign.Center
                        )
                    }
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            )
        }

        ElevatedCard(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.clickable { onClickSeeRestaurant(restId) }
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                //Nome ristorante e data
                Row() {
                    Text(
                        text = restName,
                        Modifier.padding(start = 3.dp, bottom = 3.dp),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.weight(1f))

                    Text(text = date)
                }

                Text(
                    text = "$restCity, Via  $restRoute, $restCivic ",
                    Modifier.padding(start = 3.dp, bottom = 3.dp),
                    style = MaterialTheme.typography.titleSmall
                )

                // Stelline
                Row() {
                    RowOfStars(stars)

                    Spacer(Modifier.weight(1f))

                }

                // Testo della recensione
                Text(text = comment)
                Row(Modifier.fillMaxWidth()) {
                    Button(onClick = { openDialog = true }) {
                        Text(text = "ELIMINA")
                    }
                }
            }
        }
    }
}