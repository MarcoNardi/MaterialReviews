package com.example.materialreviews.screen.reviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.materialreviews.UserReviewCard
import com.example.materialreviews.db.RestaurantViewModel
import com.example.materialreviews.db.UserViewModel
import com.example.materialreviews.R

@ExperimentalMaterial3Api
@Composable
fun ListOfReviews(
    model: RestaurantViewModel,
    modelReview: UserViewModel,
    onClickSeeRestaurant: (Int) -> Unit,
    login_id: Int
) {
    val reviews by modelReview.getReviewsOfUser(login_id).observeAsState()

    if(reviews!=null){
        if(reviews!!.reviews.isEmpty())
        {
            NoMyReviews()
        }else {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
                //modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                reviews!!.reviews.forEach() {
                    UserReviewCard(it, model, onClickSeeRestaurant = onClickSeeRestaurant)
                }
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
}

//Placeholder nel caso non ci sia nessuna recensione dell'utente
@Composable
fun NoMyReviews() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.heightIn(16.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_no_reviews_24),
                contentDescription = "Nessuna mia recensione",
                modifier = Modifier.size(48.dp),
                tint = Color.Gray
            )
            Text(text = "Ancora nessuna recensione",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge,
                modifier =  Modifier.padding(bottom = 32.dp)
            )
            Text(text = "Qui potrai visualizzare tutte le recensioni che scriverai",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier =  Modifier.padding(bottom = 32.dp, start = 24.dp, end = 24.dp)
            )
        }
    }
}

