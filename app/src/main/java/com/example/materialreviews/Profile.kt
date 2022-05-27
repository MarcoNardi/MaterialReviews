package com.example.materialreviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

// Variabili che in teoria dovrebbero andare nel db
val name = "Marco"
val surname = "Trincanato"
val profilePicture = null

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun ProfileScreen() {

    // Indica se disegnare il dialog
    var openDialog by remember {mutableStateOf(false)}

    if (openDialog) {
        AlertDialog(
            // Chiude il Dialog se viene cliccato nella parte oscurata
            onDismissRequest = {
                openDialog = false
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            title = {
                Text(text = "Il tuo profilo")
            },
            // Posso mettere dentro qualsiasi composable
            text = {
                ColorSchemeVisualizer()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Dismiss")
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    //Scaffold che permette di inserire un FAB
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openDialog = true
                }
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "FAB Edit")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ProfilePicture(size = 150.dp)
                    Text(
                        text = "$name $surname",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier
                            .padding(start = 30.dp)
                    )
                }

                Divider()

                // Le mie recensioni
                Text("Le mie recensioni",
                    style = MaterialTheme.typography.titleLarge
                )
                ListOfReviewsPreview()
            }
        }
    }
}

@Preview
@Composable
fun EditProfileDialog() {

}