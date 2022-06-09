package com.example.materialreviews.navigation

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.materialreviews.util.MyPreferences

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun NavigationManager() {
    // Definisco il navController
    val navController = rememberNavController()
    val context = LocalContext.current
    var topBarTitle by rememberSaveable() { mutableStateOf("")    }

    var onlyFavorites by rememberSaveable() {
        mutableStateOf(false)
    }

    val restaurantViewModel: RestaurantViewModel = viewModel(factory = RestaurantViewModelFactory(
        AppDatabase.getDatabase(context ).restaurantDao()
    ))

    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(
        AppDatabase.getDatabase(context ).userDao()
    ))


    // Scaffold = topBar + bottomBar + content
    Scaffold(
        topBar = { TopBar(navController, topBarTitle, onlyFavorites,
        onCheckedChange={ onlyFavorites=it }) },
        bottomBar = { BottomBar(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Associazioni stringa-composable
                NavHost(
                    navController = navController,
                    startDestination = MaterialReviewsScreen.Explore.name,
                ) {
                    //Le mie recensioni
                    composable(MaterialReviewsScreen.MyReviews.name) {
                       ListOfReviews2(
                           model = restaurantViewModel,
                           modelReview = userViewModel,
                           { restId
                               ->
                               navigateToSingleRestaurant(navController, restId)
                           },
                           login_id = MyPreferences(context).getId()
                       )
                    }
                    //Esplora
                    composable(MaterialReviewsScreen.Explore.name) {
                        ListOfRestaurants(
                            restaurantViewModel,
                            onClickSeeAll = { restId ->
                                navigateToSingleRestaurant(navController, restId)
                            },
                            onlyFavorites = onlyFavorites
                        )
                    }
                    /*
                    composable(MaterialReviewsScreen.Favourites.name) {
                        ProfileScreen(userViewModel, restaurantViewModel)
                    }
                     */
                    //Profilo
                    composable(MaterialReviewsScreen.Profile.name) {
                        /*ProfileScreen(
                            userViewModel,
                            restaurantViewModel,
                            onClickSeeRestaurant = { restId
                                ->
                                navigateToSingleRestaurant(navController, restId)
                            },
                            onClickEdit = { navController.navigate(MaterialReviewsScreen.EditProfile.name) }
                        )*/
                        ProfileScreen()
                    }
                    //TODO Impostazioni da eliminare
                    composable(MaterialReviewsScreen.Settings.name) {
                        SettingsScreen()
                    }
                    //Modifica profilo
                    composable(MaterialReviewsScreen.EditProfile.name){
                        EditProfile(userViewModel)
                    }

                    //Dettagli del ristorante e recensioni
                    val restId = MaterialReviewsScreen.RestaurantDetails.name
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

//Titoli delle schermate
fun getTitleByRoute(context: Context, route:String?): String {
    return when (route) {
        MaterialReviewsScreen.Profile.name -> context.getString(R.string.profile)
        MaterialReviewsScreen.Settings.name -> context.getString(R.string.settings)
        MaterialReviewsScreen.Explore.name -> context.getString(R.string.explore)
        MaterialReviewsScreen.RestaurantDetails.name -> context.getString(R.string.details)
        MaterialReviewsScreen.MyReviews.name -> context.getString(R.string.myreviews)
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

@ExperimentalMaterial3Api
@Composable
fun TopBar(navController: NavHostController, topBarTitle: String, selectFavorites:Boolean, onCheckedChange:(Boolean)->Unit) {

    val currentRoute = currentRoute(navController = navController)

    SmallTopAppBar(
        // Titolo che appare nella barra in alto
        title = { Text(topBarTitle) },

        // Button per tornare indietro
        navigationIcon = {
            // Controllo che ci sia almeno un altro elemento nella backStack
            if (
                navController.previousBackStackEntry != null &&
                currentRoute != MaterialReviewsScreen.Explore.name &&
                currentRoute != MaterialReviewsScreen.MyReviews.name &&
                currentRoute != MaterialReviewsScreen.Profile.name
            ) {
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
        actions = {
            if(currentRoute=="Explore"){
                Switch(
                    checked = selectFavorites,
                    onCheckedChange = onCheckedChange,
                    thumbContent = {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "filtra per preferiti",
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    },
                    modifier = Modifier.padding(end = 16.dp),
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = currentColorScheme.secondaryContainer,
                        uncheckedIconColor = currentColorScheme.secondaryContainer,
                        uncheckedBorderColor = currentColorScheme.secondaryContainer,
                        uncheckedThumbColor = currentColorScheme.onSecondaryContainer,
                        checkedTrackColor = currentColorScheme.secondary,
                        checkedIconColor = currentColorScheme.secondary,
                        checkedBorderColor = currentColorScheme.secondary,
                        checkedThumbColor = currentColorScheme.onSecondary,
                    )
                )
            }

        },
    )
}

@Composable
fun BottomBar(navController: NavHostController) {

    val currentRoute = currentRoute(navController)

    NavigationBar() {

        NavigationBarItem(
            selected = (currentRoute == MaterialReviewsScreen.Explore.name),
            icon = { Icon(painterResource(R.drawable.ic_baseline_restaurant_24), contentDescription = "Ristoranti") },
            label = { Text(text = stringResource(id = R.string.explore)) },
            onClick = {
                navController.navigate(MaterialReviewsScreen.Explore.name)
            },
            //colors = NavigationBarItemDefaults.colors(indicatorColor = currentColorScheme.inversePrimary)
        )

        NavigationBarItem(
            selected = (currentRoute == MaterialReviewsScreen.MyReviews.name),
            icon = { Icon(Icons.Filled.Star, contentDescription = "Recensioni") },
            //Nome della pagina
            label = { Text(text = stringResource(R.string.myreviews)) },
            onClick = {
                navController.navigate(MaterialReviewsScreen.MyReviews.name)
            },
            //colors = NavigationBarItemDefaults.colors(indicatorColor = currentColorScheme.inversePrimary)
        )

        NavigationBarItem(
            selected = (currentRoute == MaterialReviewsScreen.Profile.name),
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profilo") },
            //Nome della pagina
            label = { Text(text = stringResource(R.string.profile)) },
            onClick = {
                navController.navigate(MaterialReviewsScreen.Profile.name)
            },
            //colors = NavigationBarItemDefaults.colors(indicatorColor = currentColorScheme.inversePrimary)
        )
    }
}

private fun navigateToSingleRestaurant(navController: NavHostController, restId: Int) {
    navController.navigate("${MaterialReviewsScreen.RestaurantDetails.name}/$restId")
}

