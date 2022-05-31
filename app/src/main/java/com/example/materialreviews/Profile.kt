package com.example.materialreviews

import android.content.Context
import android.widget.Toast
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
    model: RestaurantViewModel
) {

    val user by modelReview.getUser(3).observeAsState()
    val name = (user?.firstName ?: "help")
    val surname = (user?.lastName ?: "halp")

    // Indica se disegnare il dialog
    var openDialog by remember {mutableStateOf(false)}

    var openDialog2 by remember {mutableStateOf(false)}

    if (openDialog) {
        AlertDialog(
            // Chiude il Dialog se viene cliccato nella parte oscurata
            onDismissRequest = {
                openDialog = false
            },
            modifier = Modifier.fillMaxWidth(0.99f),
            title = {
                Text(text = "Il tuo profilo")
            },
            // Posso mettere dentro qualsiasi composable
            text = { SettingsScreen() } ,

            confirmButton = {

                Button(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Conferma",
                    textAlign = TextAlign.Center)
                }

                            },
            /*
            dismissButton = {

                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Dismiss")
                }
            },*/
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    if(openDialog2) {

        AlertDialog(onDismissRequest = {openDialog2 = false},
            modifier = Modifier.fillMaxWidth(0.99f),
            title = {
                Text(text = "Le tue recensioni")
            },
            // Posso mettere dentro qualsiasi composable
            text = { ListOfReviews2(model, modelReview) } ,
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog2 = false
                    }
                ) {
                    Text("Conferma",
                        textAlign = TextAlign.Center)
                }

            },
            properties = DialogProperties(usePlatformDefaultWidth = false))
    }

    //Scaffold che permette di inserire un FAB
    Scaffold(
        /*

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
         */
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
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
                    val restaurant by model.getRestaurant(2).observeAsState()
                    val restName: String
                    if(restaurant != null) {
                        restName = restaurant!!.preferito.toString()
                    }else{
                        restName = "fau"
                    }
                    val toast = Toast.makeText(LocalContext.current,"$restName", Toast.LENGTH_LONG)
                    toast.show()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.End
                        ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit"
                            )
                        }

                    }

                    Divider(
                        Modifier.padding(horizontal = 20.dp),
                        color = Color.Gray,
                        thickness = 1.dp
                    )

                    Text(text = AnnotatedString(" Modifica Tema"),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clickable { openDialog = true }
                    )
                    Text(text = AnnotatedString(" Le mie recensioni"),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clickable { openDialog2 = true },
                        //onClick = { openDialog = true }
                    )
                    Text(text = AnnotatedString("Esci"),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 10.dp),
                        //onClick = { openDialog = true }
                    )
                    Text(text = AnnotatedString(" Elimina account"),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 20.dp),
                        //onClick = { openDialog = true }
                    )
            }
        }
    }
}
@ExperimentalMaterial3Api
@Composable
fun ListOfReviews2(model: RestaurantViewModel, modelReview: UserViewModel) {
    val reviews by modelReview.getReviewsOfUser(2).observeAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        if(reviews !=null) {
            items(reviews!!.reviews) { review ->
                    ReviewCard2(review, model)
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ReviewCard2(review: ReviewEntity, model: RestaurantViewModel) {

    val stars = review.rating
    val comment = review.review
    val date = review.date

    val rId = review.rid
    val restaurant by model.getRestaurant(rId).observeAsState()
    val toast = Toast.makeText(LocalContext.current, " $rId", Toast.LENGTH_LONG)
    toast.show()
    if(restaurant != null)
    {
    val restName = restaurant!!.name
    val restCity = restaurant!!.address!!.citta
    val restCivic = restaurant!!.address!!.num_civico
    val restRoute = restaurant!!.address!!.via
    ElevatedCard(
        shape = RoundedCornerShape(15.dp)
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
        }
    }
    }
}


@Preview
@Composable
fun EditProfileDialog() {

}