package com.example.materialreviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
                ProfilePicture(size = 40.dp, null, 1.dp)

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


@ExperimentalMaterial3Api
@Composable
fun ListOfReviews(reviews: List<Review>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        items(reviews) { review ->
            ReviewCard( review )
        }
    }
}

@ExperimentalMaterial3Api

@Composable
fun ListOfReviews2(restId: Int?) {

        val data = getInitialReviewsData()
        val reviews = listOf(Review(), Review(user = "Marco Nardi"), Review(user = "Marco Trincanato"), Review(), Review(user = "Marco Nardi"), Review(user = "Marco Trincanato"))
        ListOfReviews(reviews)

}

@ExperimentalMaterial3Api
@Preview
@Composable
fun ListOfReviewsPreview() {
    val reviews = listOf(Review(), Review(user = "Marco Nardi"), Review(user = "Marco Trincanato"), Review(), Review(user = "Marco Nardi"), Review(user = "Marco Trincanato"))
    ListOfReviews(reviews)
}