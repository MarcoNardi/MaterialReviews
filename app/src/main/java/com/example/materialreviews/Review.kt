package com.example.materialreviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

data class Review(
    val user: String = "Eli Ferin", // oggetto di classe User quando e` pronta
    val comment: String = "Buona la varietà di piatti, saporiti e ben cucinati, fritto leggero e pesche fresco, buono il servizio e l'impiattamento. Ambiente curato ottimo per gruppi. Ottimo il rapporto qualità prezzo. Da tornarci.",
    val rating: Int = 4,
    val date: String = "12/04/1987"
)



@ExperimentalMaterial3Api
@Composable
fun ReviewCard(review: Review) {
    val userName = review.user
    val stars = review.rating
    val comment = review.comment
    val date = review.date

    ElevatedCard(
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                //Immagine del profilo
                ProfilePicture(size = 40.dp, "null", 1.dp)

                //Nome utente
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.weight(1f))

                // Menu
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Star",
                    )
                }
            }

            // Stelline e data
            Row() {
                for (i in 0..4) {
                    val icon = Icons.Filled.Star
                    val tint = if (i<stars) Color(252, 185, 0) else Color.LightGray
                    Icon(
                        imageVector = icon,
                        tint = tint,
                        contentDescription = "Star",
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(text = date)
            }

            // Testo della recensione
            Text(text = comment)
        }
    }
}

/**
 * 5 stelline, colorate in funzione del parametro
 */
@Preview
@Composable
fun RowOfStars(
    rating: Int = 4,
) {
    Row() {
        for (i in 0..4) {
            val icon = Icons.Filled.Star
            val tint = if (i<rating) Color(252, 185, 0) else Color.LightGray
            Icon(
                imageVector = icon,
                tint = tint,
                contentDescription = "Star",
            )
        }
    }
}

/**
 * 5 stelline, colorate in funzione del parametro, cliccabile
 */
@Preview
@Composable
fun RowOfStars(
    rating: Int = 1,
    onClick: (Int)->Unit = {}
) {
    Row() {
        for (i in 0..4) {
            val icon = Icons.Filled.Star
            val tint = if (i<rating) Color(252, 185, 0) else Color.LightGray

            // Quando viene cliccata una stella, passa alla lambda la posizione della stella (da 1 a 5)
            IconButton(
                onClick = { onClick(i+1) },
            ) {
                Icon(
                    imageVector = icon,
                    tint = tint,
                    contentDescription = "Star",
                )
            }

        }
    }
}

/**
 * Apre il dialog per inserire una nuova recensione. Ritorna un int e una stringa
 */
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun AddReviewDialog(
    closeDialog: ()->Unit,
    onConfirmClick: (Int, String)->Unit,
    existingRating: Int = 1,
    existingComment: String = "Prova",
) {
    // Variabili della recensione
    var rating by remember{mutableStateOf(existingRating)}
    var comment by remember{mutableStateOf(existingComment)}

    AlertDialog(
        onDismissRequest = closeDialog,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.9f),
        title = { Text("Scrivi una recensione") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Seleziona quante stelle mettere nella recensione
                RowOfStars(
                    rating = rating,
                    onClick = { selectedRating ->
                        rating = selectedRating
                    }
                )

                // Inserisce il testo della recensione
                OutlinedTextField(
                    value = comment,
                    onValueChange = {comment = it},
                    label = { Text("Inserisci un commento") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmClick(rating, comment)
                }
            ) {
                Text("Pubblica")
            }
        },
        dismissButton = {
            TextButton(
                onClick = closeDialog
            ) {
                Text("Annulla")
            }
        },
    )
}


@ExperimentalMaterial3Api
@Composable
fun ListOfReviews(reviews: List<Review>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        item() {
            //RestaurantDetails(restId = 1, {})
        }
        items(reviews) { review ->
            ReviewCard( review )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun ListOfReviewsPreview() {
    val reviews = listOf(Review(), Review(user = "Marco Nardi"), Review(user = "Marco Trincanato"), Review(), Review(user = "Marco Nardi"), Review(user = "Marco Trincanato"))
    ListOfReviews(reviews)
}