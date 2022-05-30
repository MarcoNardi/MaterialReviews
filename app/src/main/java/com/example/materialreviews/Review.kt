package com.example.materialreviews

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*

data class Review(
    val user: String = "Eli Ferin", // oggetto di classe User quando e` pronta
    val comment: String = "Buona la varietà di piatti, saporiti e ben cucinati, fritto leggero e pesche fresco, buono il servizio e l'impiattamento. Ambiente curato ottimo per gruppi. Ottimo il rapporto qualità prezzo. Da tornarci.",
    val rating: Int = 4,
    val date: String = "12/04/1987"
)

@ExperimentalMaterial3Api
@Composable
fun RestaurantInfo(restId: Int?) {
    val context = LocalContext.current
    Surface(  ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically){
            Column() {
            Row(modifier =  Modifier.padding(top = 3.dp)) {
                for (i in 0..4) {
                    val icon = Icons.Filled.Star
                    val tint = if (i<2) Color(252, 185, 0) else Color.LightGray
                    Icon(
                        imageVector = icon,
                        tint = tint,
                        contentDescription = "Star",
                    )
                }

            }
            Text(text = restId.toString(),
                Modifier.padding(top = 3.dp)
            )
            }
                Spacer(Modifier.weight(1f))
                val icon = Icons.Filled.Favorite

                var checked by remember {
                    mutableStateOf(false)
                }
                IconToggleButton( checked = checked, onCheckedChange = {checked = it}, modifier = Modifier.padding(end = 12.dp)) {
                    val tint by animateColorAsState(if (checked) Color.Red else Color.LightGray)
                    Icon(

                        imageVector = icon,
                        contentDescription = "Aggiungi a elementi salvati",
                        tint = tint,
                        modifier = Modifier.size(35.dp)
                    )

                }
            }
            Text(text = "Posizione",
                Modifier.padding(top = 3.dp)
            )
            Text(text = "Posizione2",
                Modifier.padding(top = 3.dp)
            )
            Row(modifier = Modifier.padding(top = 3.dp)) {
                IconButton(  modifier = Modifier.padding(end = 12.dp),
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:<3467640861")
                        context.startActivity(intent)
                    }) {

                    Icon(

                        imageVector = Icons.Filled.Call,
                        contentDescription = "Chiama",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )

                }
                IconButton( onClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://retireinprogress.com/404books/")
                        context.startActivity(intent)
                    }) {

                    Icon(

                        painter = painterResource(id = R.drawable.ic_baseline_language_24),
                        contentDescription = "Vai al sito",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )

                }

            }
        }
    }
}

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
        item() {
            RestaurantInfo(restId = 1)
        }
        items(reviews) { review ->
            ReviewCard( review )
        }
    }
}

@ExperimentalMaterial3Api

@Composable
fun ListOfReviews2(restId: Int?, model: RestaurantViewModel= viewModel(factory= RestaurantViewModelFactory(AppDatabase.getDatabase(
    LocalContext.current).restaurantDao()))) {

    val reviews by model.getReviewsOfRestaurant(restId!!).observeAsState()
    model.getImageOfRestaurant(restId!!)
    val data = getInitialReviewsData()
    //val reviews = listOf(Review(), Review(user = "Marco Nardi"), Review(user = "Marco Trincanato"), Review(), Review(user = "Marco Nardi"), Review(user = "Marco Trincanato"))
    Column() {
        if(reviews!=null){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                //modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                item() {
                    RestaurantInfo(restId = 1)
                }
                items(reviews!!.reviews) { review ->
                    ReviewCard( review )
                }
            }
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