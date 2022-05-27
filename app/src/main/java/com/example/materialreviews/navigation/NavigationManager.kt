package com.example.materialreviews.navigation

import android.provider.ContactsContract
import android.service.autofill.UserData
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.materialreviews.*
import com.example.materialreviews.db.*

// Lista che contiene le schermate a cui e` possibile navigare
val destinationsList = listOf(
    "Preferiti",
    "Esplora",
    "Profilo"
)

@ExperimentalMaterial3Api
@Preview
@Composable
fun NavigationManager() {
    // Definisco il navController
    val navController = rememberNavController()
    val context = LocalContext.current
    val allScreens = MaterialReviewsScreen.values().toList()


    val restaurantViewModel: RestaurantViewModel = viewModel(factory = RestaurantViewModelFactory(
        AppDatabase.getDatabase(context ).restaurantDao()
    ))
    // Scaffold = topBar + bottomBar + content
    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomBar(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 10.dp)
            ) {
                // Associazioni stringa-composable
                NavHost(
                    navController = navController,
                    startDestination = MaterialReviewsScreen.Explore.name,
                ) {
                    composable(MaterialReviewsScreen.Favourites.name) {
                        ListOfRestaurants(restaurantViewModel, onClickSeeAll = { restId ->
                            navigateToSingleRestaurant(navController, restId)
                        })
                    }
                    composable(MaterialReviewsScreen.Explore.name) {
                        ListOfReviewsPreview()
                    }
                    composable(MaterialReviewsScreen.Reviews.name) {
                        ProfileScreen()
                    }
                    composable(MaterialReviewsScreen.Profile.name) {
                        SettingsScreen()
                    }

                    val restId = MaterialReviewsScreen.Reviews.name
                    composable(
                        route = "$restId/{name}",
                        arguments = listOf(
                            navArgument("name") {
                                type = NavType.IntType
                            }
                        )
                    ) { entry ->
                        val restId = entry.arguments?.getInt("name")
                        ListOfReviews2(restId = restId)
                    }
                }
            }
        },
    )
}

@Composable
fun TopBar(navController: NavHostController) {
    SmallTopAppBar(
        // Titolo che appare nella barra in alto
        title = { Text("Titolo") },

        // Button per tornare indietro
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },

        // Altre azioni in alto a destra
        actions = {
            // Qui e` come se fossi in una Row()
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Localized description"
                )
            }
            IconButton(
                onClick = {
                    navController.navigate("Settings")
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}


var selectedScreen by mutableStateOf(MaterialReviewsScreen.Explore.name)

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar() {

        NavigationBarItem(
            selected = (selectedScreen == MaterialReviewsScreen.Explore.name),
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text(MaterialReviewsScreen.Explore.name) },
            onClick = {
                selectedScreen = MaterialReviewsScreen.Explore.name
                navController.navigate(MaterialReviewsScreen.Explore.name)
            },
        )

        NavigationBarItem(
            selected = (selectedScreen == MaterialReviewsScreen.Favourites.name),
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text(MaterialReviewsScreen.Favourites.name) },
            onClick = {
                selectedScreen = MaterialReviewsScreen.Favourites.name
                navController.navigate(MaterialReviewsScreen.Favourites.name)
            },
        )

        NavigationBarItem(
            selected = (selectedScreen == MaterialReviewsScreen.Profile.name),
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text(MaterialReviewsScreen.Profile.name) },
            onClick = {
                selectedScreen = MaterialReviewsScreen.Profile.name
                navController.navigate(MaterialReviewsScreen.Profile.name)
            },
        )
    }
}

private fun navigateToSingleRestaurant(navController: NavHostController, restId: Int) {
navController.navigate("${MaterialReviewsScreen.Reviews.name}/$restId")
}

