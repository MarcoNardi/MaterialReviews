package com.example.materialreviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Variabili che in teoria dovrebbero andare nel db
val name = "Marco"
val surname = "Trincanato"
val profilePicture = null

@ExperimentalMaterial3Api
@Preview
@Composable
fun ProfileScreen() {
    //Scaffold che permette di inserire un FAB
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
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
                Text(text = "Impostazioni")

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