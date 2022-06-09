package com.example.materialreviews

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*
import com.example.materialreviews.ui.theme.currentColorScheme
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Schermata dove ci sono tutte le informazioni di un singolo ristorante
 */
@OptIn(ExperimentalFoundationApi::class)
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
    //val userEntity by userModel.getUser(1).observeAsState()
    //val restaurantWithImages by restaurantModel.getImageOfRestaurant(restId).observeAsState()
    // Ottengo l'ID del ristorante e dell'utente
    val restaurantId = if (restaurantWithReviews != null) restaurantWithReviews!!.restaurant.rid else 1
    val userId = 1 //TODO shared preferences

    //ottengo uri con questa funzione (attenzione che ritorna un LiveData)
    val imageUri by restaurantModel.getImageUriOfRestaurant(restId).observeAsState()

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

                //Data odierna
                val locale: Locale = Locale.getDefault()
                val sdf = SimpleDateFormat("dd/MM/yyyy", locale)
                val currentDate = sdf.format(Date())

                // Se esiste gia` la review la modifica, altrimenti la aggiunge
                if (existReview) {
                    reviewViewModel.updateReview(
                        newRating = rating,
                        newComment = comment,
                        userId = userId,
                        restaurantId = restaurantId,
                        newDate = currentDate
                    )
                }
                else {
                    reviewViewModel.addReview(
                        rating = rating,
                        review = comment,
                        userId = userId,
                        restaurantId = restaurantId,
                        date = currentDate
                    )
                }

                showAddReviewDialog = false
            },
            existingRating = existingRating,
            existingComment = existingComment
        )
    }



    //https://stackoverflow.com/questions/67737502/how-to-detect-up-down-scroll-for-a-column-with-vertical-scroll
    val fabHeight = 72.dp //FabSize+Padding
    //nel caso la scirttura sembra arcana
    //https://stackoverflow.com/questions/65921799/how-to-convert-dp-to-pixels-in-android-jetpack-compose
    val fabHeightPx = with(LocalDensity.current) { fabHeight.roundToPx().toFloat() }
    //ricordo l'offset
    val fabOffsetHeightPx = remember { mutableStateOf(0f) }
    //serve a "ricevere gli eventi" di scroll
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                //available è di quanto ci si è spostati

                val newOffset = fabOffsetHeightPx.value + available.y
                //modifico l'offset del FAB
                //coerce serve a restringere il risultato nel dominio (-fabHeightPx, 0f)
                fabOffsetHeightPx.value = newOffset.coerceIn(-fabHeightPx, 0f)
                //non abbiamo "consumato" nulla dello scroll quindi ritorniamo zero, noi osserviamo lo scroll
                // e semplicemente muoviamo il FAB come si deve
                return Offset.Zero
            }
        }
    }


    Scaffold(
        modifier=Modifier.nestedScroll(nestedScrollConnection),
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
                contentColor = currentColorScheme.onPrimary,
                //offset calcolato durante lo scroll
                modifier = Modifier
                    .offset(x = 16.dp)
                    .offset { IntOffset(x = 0, y = -fabOffsetHeightPx.value.roundToInt()) }
            )
        },

        // Dettagli del ristorante
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {

                // Mostra i dettagli del ristorante e tutte le sue recensioni
                if(restaurantWithReviews!=null /*&& restaurantWithImages!=null*/ && imageUri!=null){
                    LazyColumn(
                        //modifier=Modifier.scrollable(rememberScrollState(), Orientation.Vertical),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        //modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        item() {

                            // Dettagli
                            RestaurantDetails(
                                restaurant = restaurantWithReviews!!.restaurant,
                                //restaurantWithImages!!.images[0].uri,
                                imageUri = imageUri!!,
                                { it ->
                                    restaurantModel.changeFavoriteState(
                                        restaurantWithReviews!!.restaurant.rid,
                                        it
                                    )
                                },
                                getAverageRating = {
                                    restaurantModel.getAverageRatingOfRestaurant(restaurantWithReviews!!.restaurant.rid)
                                },
                                // Passo la lambda per mostrare il dialog per aggiungere le recensioni
                                addReviewButtonOnClick = {
                                    showAddReviewDialog = true
                                }
                            )
                        }

                        // Lista delle review del ristorante
                        if (restaurantWithReviews != null) {

                            //Placeholder nel caso non ci sia nessuna recensione
                            if(restaurantWithReviews!!.reviews.isEmpty())
                            {
                                item {
                                NoReviews()
                                }
                            }

                            items(restaurantWithReviews!!.reviews) { review ->
                                ReviewCard(review= review, getUserInfo = {
                                    userModel.getUser(it)
                                }, modifier =  Modifier.animateItemPlacement())
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(0.dp))
                        }
                    }
                }
            }
        }
    )
}

//Placeholder nel caso non ci sia nessuna recensione
@Composable
fun NoReviews(){
    Surface( ) {
        Column(
            Modifier.padding(top = 24.dp, bottom = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_no_reviews_24),
                contentDescription = "Nessuna recensione",
                modifier = Modifier.size(35.dp),
                tint = Color.Gray
            )
            Text(text = "Ancora nessuna recensione",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}