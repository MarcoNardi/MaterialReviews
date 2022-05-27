package com.example.materialreviews.navigation

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.materialreviews.*
import com.example.materialreviews.db.*

// Lista che contiene le schermate a cui e` possibile navigare
val destinationsList = listOf(
    "Preferiti",
    "Esplora",
    "Impostazioni"
)

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Preview
@Composable
fun NavigationManager() {
    // Definisco il navController
    val navController = rememberNavController()
    val context = LocalContext.current


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
                    startDestination = destinationsList[1],
                ) {
                    composable(destinationsList[0]) {
                        ListOfRestaurantsPreview(restaurantViewModel)
                    }
                    composable(destinationsList[1]) {
                        ListOfReviewsPreview()
                    }
                    composable(destinationsList[2]) {
                        SettingsScreen()
                    }
                    composable("Profilo") {
                        ProfileScreen()
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
            IconButton(
                onClick = {
                    // Controllo che ci sia almeno un altro elemento nella backStack
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
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
                    navController.navigate("Profilo")
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}


var selectedScreen by mutableStateOf(destinationsList[1])

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar() {

        NavigationBarItem(
            selected = (selectedScreen == destinationsList[0]),
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text(destinationsList[0]) },
            onClick = {
                selectedScreen = destinationsList[0]
                navController.navigate(destinationsList[0])
            },
        )

        NavigationBarItem(
            selected = (selectedScreen == destinationsList[1]),
            icon = { Icon(Icons.Filled.Place, contentDescription = null) },
            label = { Text(destinationsList[1]) },
            onClick = {
                selectedScreen = destinationsList[1]
                navController.navigate(destinationsList[1])
            },
        )

        NavigationBarItem(
            selected = (selectedScreen == destinationsList[2]),
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            label = { Text(destinationsList[2]) },
            onClick = {
                selectedScreen = destinationsList[2]
                navController.navigate(destinationsList[2])
            },
        )
    }
}