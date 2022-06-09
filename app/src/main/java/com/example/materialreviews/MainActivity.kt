package com.example.materialreviews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*
import com.example.materialreviews.navigation.NavigationManager
import com.example.materialreviews.ui.theme.MaterialReviewsTheme

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        PRIORITA DA ALTO A BASSO
        TODO RELAZIONE: RIDURRE PARTE INIZIALE, PARTE MATERIAL 3 CON RIFERIMENTI ALLA NOSTRA APP
        TODO lag mie recensioni
        TODO Pulizia
        TODO CARD PER QUANDO NON CI SONO PREFERITI
        TODO schermo orizzontale
        TODO GESTURES se possibile
        */
        super.onCreate(savedInstanceState)
        setContent {
            M3App()
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun M3App() {
    MaterialReviewsTheme() {
        /*
        val context= LocalContext.current
        val imageModel: ImageViewModel=viewModel(factory = ImageViewModelFactory(
            AppDatabase.getDatabase(context).imageDao()
            )
        )
        imageModel.addImage(restaurantImageUri+"6", rid =6)
        imageModel.addImage(restaurantImageUri+"7", rid =7)
        imageModel.addImage(restaurantImageUri+"8", rid =7)
        imageModel.addImage(restaurantImageUri+"2", rid =2)
        imageModel.addImage(restaurantImageUri+"3", rid =3)
        imageModel.addImage(restaurantImageUri+"4", rid =4)
        imageModel.addImage(restaurantImageUri+"5", rid =5)



        val userModel: UserViewModel=viewModel(factory = UserViewModelFactory(
            AppDatabase.getDatabase(context).userDao()
            )
        )

        val restaurantModel: RestaurantViewModel=viewModel(factory = RestaurantViewModelFactory(
            AppDatabase.getDatabase(context).restaurantDao()
            )
        )
        val reviewModel: ReviewViewModel=viewModel(factory = ReviewViewModelFactory(
            AppDatabase.getDatabase(context).reviewDao()
            )
        )

        for(i in getInitialUsersData()){
            userModel.addUser(i)
        }
        for(i in getInitialRestaurantsData()){
            restaurantModel.addRestaurant(i)
        }
        for(i in getInitialReviewsData()){
            reviewModel.addReview(i)
        }*/


        NavigationManager()
    }
}