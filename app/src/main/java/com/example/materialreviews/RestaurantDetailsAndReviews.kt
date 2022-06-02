package com.example.materialreviews

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*
import com.example.materialreviews.ui.theme.currentColorScheme

/**
 * Schermata dove ci sono tutte le informazioni di un singolo ristorante
 */
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
    val userEntity by userModel.getUser(1).observeAsState()
    val restaurantWithImages by restaurantModel.getImageOfRestaurant(restId).observeAsState()
    // Ottengo l'ID del ristorante e dell'utente
    val restaurantId = if (restaurantWithReviews != null) restaurantWithReviews!!.restaurant.rid else 1
    val userId = 1 //shared preferences

    val reviewViewModel: ReviewViewModel = viewModel(
        factory = ReviewViewModelFactory(
            AppDatabase.getDatabase(
                LocalContext.current
            ).reviewDao()
        )
    )

    // Controlla se esiste gia` una review
    var existReview = false
    var existingRating = 3
    var existingComment = ""

    if (restaurantWithReviews != null) {
        val restaurantReviews = restaurantWithReviews!!.reviews
        for (review in restaurantReviews) {
            if (review.uid == userId) {
                existingRating = review.rating
                existingComment = review.review
                existReview = true
                break
            }
        }
    }

    // Indica se mostrare il dialog per aggiungere una recensione
    var showAddReviewDialog by remember { mutableStateOf(false) }
    if (showAddReviewDialog) {
        AddReviewDialog(
            closeDialog = {
                showAddReviewDialog = false
            },
            onConfirmClick = { rating, comment ->
                Log.v(null, "$rating, $comment")

                // Se esiste gia` la review la modifica, altrimenti la aggiunge
                if (existReview) {
                    reviewViewModel.updateReview(
                        newRating = rating,
                        newComment = comment,
                        userId = userId,
                        restaurantId = restaurantId,
                        newDate = "MyDate"
                    )
                }
                else {
                    reviewViewModel.addReview(
                        rating = rating,
                        review = comment,
                        userId = userId,
                        restaurantId = restaurantId,
                        date = "MyDate"
                    )
                }

                showAddReviewDialog = false
            },
            existingRating = existingRating,
            existingComment = existingComment
        )
    }

    Scaffold(

        // FAB per aprire AddReviewDialog
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {Text("Scrivi una recensione")},
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Scrivi recensione",
                    )
                },
                onClick = { showAddReviewDialog = true },
                containerColor = currentColorScheme.primary,
                contentColor = currentColorScheme.onPrimary
            )
        },

        // Dettagli del ristorante
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {

                // Mostra i dettagli del ristorante e tutte le sue recensioni
                if(restaurantWithReviews!=null && restaurantWithImages!=null){
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        //modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        item() {

                            // Dettagli
                            RestaurantDetails(
                                restaurant = restaurantWithReviews!!.restaurant,
                                restaurantWithImages!!.images[0].uri,
                                { it ->
                                    restaurantModel.changeFavoriteState(
                                        restaurantWithImages!!.restaurant.rid,
                                        it
                                    )
                                },
                                getAverageRating = {
                                    restaurantModel.getAverageRatingOfRestaurant(restaurantWithImages!!.restaurant.rid)
                                },
                                // Passo la lambda per mostrare il dialog per aggiungere le recensioni
                                addReviewButtonOnClick = {
                                    showAddReviewDialog = true
                                }
                            )
                        }

                        // Lista delle review del ristorante
                        if (restaurantWithReviews != null) {
                            items(restaurantWithReviews!!.reviews) { review ->
                                ReviewCard(review) {
                                    userModel.getUser(it)
                                }
                            }
                        }

                        // Spacer per aggiungere un piccolo spazio estetico alla fine
                        item{
                            Spacer(modifier = Modifier.height(1.dp))
                        }
                    }
                }
            }
        }
    )
}