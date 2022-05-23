package com.example.materialreviews.navigation

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.materialreviews.ProfileScreen
import com.example.materialreviews.ListOfReviewsPreview
import com.example.materialreviews.TestComposable

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

    // Scaffold = topBar + bottomBar + content
    Scaffold(
        topBar = { TopBar() },
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
                        ListOfReviewsPreview()
                    }
                    composable(destinationsList[1]) {
                        TestComposable(7)
                    }
                    composable(destinationsList[2]) {
                        ProfileScreen()
                    }
                }
            }
        },
    )
}

@Composable
fun TopBar() {
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
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
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text(destinationsList[1]) },
            onClick = {
                selectedScreen = destinationsList[1]
                navController.navigate(destinationsList[1])
            },
        )

        NavigationBarItem(
            selected = (selectedScreen == destinationsList[2]),
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text(destinationsList[2]) },
            onClick = {
                selectedScreen = destinationsList[2]
                navController.navigate(destinationsList[2])
            },
        )
    }
}