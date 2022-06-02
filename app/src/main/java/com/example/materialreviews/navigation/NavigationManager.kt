package com.example.materialreviews.navigation

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract
import android.service.autofill.UserData
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.Animation
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.materialreviews.*
import com.example.materialreviews.R
import com.example.materialreviews.db.*
import com.example.materialreviews.ui.theme.currentColorScheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@ExperimentalMaterial3Api
@Preview
@Composable
fun NavigationManager() {
    // Definisco il navController
    val navController = rememberNavController()
    val context = LocalContext.current
    var topBarTitle by rememberSaveable() { mutableStateOf("")    }
    val currentRoute = currentRoute(navController = navController)



    val restaurantViewModel: RestaurantViewModel = viewModel(factory = RestaurantViewModelFactory(
        AppDatabase.getDatabase(context ).restaurantDao()
    ))

    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(
        AppDatabase.getDatabase(context ).userDao()
    ))
    // Scaffold = topBar + bottomBar + content
    Scaffold(
        topBar = { TopBar(navController, topBarTitle) },
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
                        ListOfRestaurants(
                            restaurantViewModel,
                            onClickSeeAll = { restId ->
                                navigateToSingleRestaurant(navController, restId)
                            },
                            onlyFavorites = true
                        )
                    }
                    composable(MaterialReviewsScreen.Explore.name) {
                        ListOfRestaurants(
                            restaurantViewModel,
                            onClickSeeAll = { restId ->
                                navigateToSingleRestaurant(navController, restId)
                            },
                            onlyFavorites = false
                        )
                    }
                    composable(MaterialReviewsScreen.Reviews.name) {
                        ProfileScreen(userViewModel, restaurantViewModel)
                    }
                    composable(MaterialReviewsScreen.Profile.name) {
                        ProfileScreen(
                            userViewModel,
                            restaurantViewModel,
                            onClickSeeRestaurant = { restId
                                ->
                                navigateToSingleRestaurant(navController, restId)
                            },
                            onClickEdit = { navController.navigate(MaterialReviewsScreen.EditProfile.name) }
                        )
                    }
                    composable(MaterialReviewsScreen.Settings.name) {
                        SettingsScreen()
                    }
                    composable(MaterialReviewsScreen.EditProfile.name){
                        EditProfile(userViewModel)
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
                        RestaurantDetailsAndReviews(restId = restId!!)
                    }
                }
            }
        },
    )

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            // You can map the title based on the route using:
            topBarTitle = getTitleByRoute(context, backStackEntry.destination.route)
        }
    }
}

fun getTitleByRoute(context: Context, route:String?): String {
    return when (route) {
        MaterialReviewsScreen.Profile.name -> context.getString(R.string.profile)
        MaterialReviewsScreen.Settings.name -> context.getString(R.string.settings)
        MaterialReviewsScreen.Explore.name -> context.getString(R.string.explore)
        MaterialReviewsScreen.Favourites.name -> context.getString(R.string.favourites)
        MaterialReviewsScreen.Reviews.name -> context.getString(R.string.reviews)
        MaterialReviewsScreen.EditProfile.name -> context.getString(R.string.edit_profile)
        else -> context.getString(R.string.explore)
    }

}

/**
 * Ritorna la destination attuale come stringa
 */
@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun TopBar(navController: NavHostController, topBarTitle: String) {
    SmallTopAppBar(
        // Titolo che appare nella barra in alto
        title = { Text(topBarTitle) },

        // Button per tornare indietro
        navigationIcon = {
            // Controllo che ci sia almeno un altro elemento nella backStack
            if (navController.previousBackStackEntry != null) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },

        // Altre azioni in alto a destra
        actions = {},
    )
}


var selectedScreen by mutableStateOf(MaterialReviewsScreen.Explore.name)

@Composable
fun BottomBar(navController: NavHostController) {

    val currentRoute = currentRoute(navController)

    NavigationBar() {

        NavigationBarItem(
            selected = (currentRoute == MaterialReviewsScreen.Explore.name),
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text(text = stringResource(id = R.string.explore)) },
            onClick = {
                navController.navigate(MaterialReviewsScreen.Explore.name)
            },
        )

        NavigationBarItem(
            selected = (currentRoute == MaterialReviewsScreen.Favourites.name),
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text(text = stringResource(R.string.favourites)) },
            onClick = {
                navController.navigate(MaterialReviewsScreen.Favourites.name)
            }
        )

        NavigationBarItem(
            selected = (currentRoute == MaterialReviewsScreen.Profile.name),
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            label = { Text(text = stringResource(R.string.profile)) },
            onClick = {
                navController.navigate(MaterialReviewsScreen.Profile.name)
            },
        )
    }
}

private fun navigateToSingleRestaurant(navController: NavHostController, restId: Int) {
    navController.navigate("${MaterialReviewsScreen.Reviews.name}/$restId")
}

