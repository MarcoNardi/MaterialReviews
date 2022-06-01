package com.example.materialreviews

import android.app.Dialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*
import com.example.materialreviews.navigation.MaterialReviewsScreen

// Variabili che in teoria dovrebbero andare nel db

val profilePicture = null

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(
    modelReview: UserViewModel,
    model: RestaurantViewModel,
    onClickSeeRestaurant: (Int) -> Unit = {},
    onClickEdit: () -> Unit = {}
) {
    val myPreferences = MyPreferences(LocalContext.current)
    var login_id = myPreferences.getId()

    val user by modelReview.getUser(login_id).observeAsState()
    val name = (user?.firstName ?: "help")
    val surname = (user?.lastName ?: "halp")

    // Indica se disegnare il dialog
    var openDialog by remember {mutableStateOf(false)}

    var openData by remember { mutableStateOf("")}

    var visible by remember { mutableStateOf(true) }

    var confirmText: String
    when(openData) {
        "Tema" -> confirmText = "Conferma"
        "Recensioni" ->  confirmText = "Chiudi"
        "Esci" -> confirmText = "ESCI"
        "Elimina" -> confirmText = "ELIMINA"
        else -> {
            confirmText ="Errore"
        }
    }

    var dismissText: String
    when(openData) {
        "Tema" -> dismissText = ""
        "Recensioni" ->  dismissText = ""
        "Esci" -> dismissText = "ANNULLA"
        "Elimina" -> dismissText = "ANNULLA"
        else -> {
            dismissText ="Errore"
        }
    }

    if (openDialog) {

        AlertDialog(
            // Chiude il Dialog se viene cliccato nella parte oscurata
            onDismissRequest = {
                openDialog = false
            },
            modifier = Modifier.fillMaxWidth(0.99f),
            title = {
                when(openData) {
                    "Tema" -> Text(text ="Il tuo tema")
                    "Recensioni" ->  Text(text ="Le mie recensioni")
                    "Esci" -> Text(text = "Uscire dall'account?")
                    "Elimina" -> Text(text = "Eliminare l'account?")
                    else -> {
                        Text(text ="Errore")
                    }
                }
            },
            // Posso mettere dentro qualsiasi composable
            text = {
                when(openData) {
                    "Tema" -> SettingsScreen()
                    "Recensioni" ->  ListOfReviews2(model, modelReview, onClickSeeRestaurant = onClickSeeRestaurant)
                    else -> {
                        Text(text ="Errore")
                    }
                } } ,

            confirmButton = {

                Button(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text(confirmText,
                    textAlign = TextAlign.Center)
                }

                            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if(visible)
                        openDialog = false
                    }
                ) {
                    Text(dismissText)
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }


/*
    //Scaffold che permette di inserire un FAB
    Scaffold(


        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openDialog = true
                }
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "FAB Edit")
            }
        },
        floatingActionButtonPosition = FabPosition.End

    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {

         */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    ProfilePicture(size = 150.dp,)

                    Text(
                        text = "$name $surname",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )


                    Divider(
                        Modifier.padding(horizontal = 20.dp),
                        color = Color.Gray,
                        thickness = 1.dp
                    )

                    Text(text = AnnotatedString(" Modifica Tema"),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clickable {
                                openData = "Tema"
                                openDialog = true
                                visible = false}
                    )
                    Text(text = AnnotatedString(" Le mie recensioni"),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clickable {
                                openData = "Recensioni"
                                openDialog = true
                                visible = false},

                    )
                    Text(text = AnnotatedString(" Modifica profilo"),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clickable {
                                onClickEdit()
                                       }

                        )
                    Text(text = AnnotatedString("Esci dall'account"),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clickable{
                                openData = "Esci"
                                openDialog = true
                                visible = true
                            }

                    )
                    Text(text = AnnotatedString(" Elimina account"),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(top = 20.dp) .
                            clickable{
                                openData = "Elimina"
                                openDialog = true
                                visible = true
                            }
                        //onClick = { openDialog = true }
                    )
            }
       // }
    //}
}
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun ListOfReviews2(model: RestaurantViewModel, modelReview: UserViewModel, onClickSeeRestaurant: (Int) -> Unit) {
    val reviews by modelReview.getReviewsOfUser(2).observeAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        if(reviews !=null) {
            items(reviews!!.reviews) { review ->
                ReviewCard2(review, model, onClickSeeRestaurant = onClickSeeRestaurant)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun ReviewCard2(review: ReviewEntity,
                model: RestaurantViewModel,
                onClickSeeRestaurant: (Int) -> Unit) {

    val stars = review.rating
    val comment = review.review
    val date = review.date

    var openDialog by remember { mutableStateOf(false) }
    val reviewViewModel: ReviewViewModel = viewModel(factory = ReviewViewModelFactory(
        AppDatabase.getDatabase(LocalContext.current).reviewDao()
    ))

    val restId = review.rid
    val restaurant by model.getRestaurant(restId).observeAsState()
    if(restaurant != null)
    {
    val restName = restaurant!!.name
    val restCity = restaurant!!.address!!.citta
    val restCivic = restaurant!!.address!!.num_civico
    val restRoute = restaurant!!.address!!.via
        if(openDialog) {

            AlertDialog(onDismissRequest = { openDialog = false },
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
    modifier = Modifier.clickable{ onClickSeeRestaurant(restId)}
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            //Nome ristorante
            Text(
                text = "$restName",
                Modifier.padding(start = 3.dp, bottom = 3.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$restCity, Via  $restRoute, $restCivic ",
                Modifier.padding(start = 3.dp, bottom = 3.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.weight(1f))

            // Stelline e data
            Row() {
                RowOfStars(stars)

                Spacer(Modifier.weight(1f))

                Text(text = date)
            }

            // Testo della recensione
            Text(text = comment)
            Row(Modifier.fillMaxWidth()) {
                Button(onClick = {openDialog = true}) {
                    Text(text = "ELIMINA")

                }
        }
    }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun seeDialog() {}

@Preview
@Composable
fun EditProfileDialog() {

}