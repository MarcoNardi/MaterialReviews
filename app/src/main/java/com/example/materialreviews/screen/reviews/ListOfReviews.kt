package com.example.materialreviews.screen.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.materialreviews.UserReviewCard
import com.example.materialreviews.db.RestaurantViewModel
import com.example.materialreviews.db.UserViewModel

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
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier= Modifier.verticalScroll(rememberScrollState())
            //modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            reviews!!.reviews.forEach(){
                UserReviewCard(it, model, onClickSeeRestaurant = onClickSeeRestaurant)
            }
            Spacer(modifier = Modifier.height(0.dp))
        }
    }
}