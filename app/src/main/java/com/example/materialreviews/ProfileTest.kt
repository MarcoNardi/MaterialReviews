package com.example.materialreviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Variabili che in teoria dovrebbero andare nel db
val name = "Eli"
val surname = "Ferin"
val profilePicture = null

@Preview
@Composable
fun ProfileScreen() {
    //Scaffold con content=Column e FAB per editare
    Scaffold(
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "FAB Edit")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            ProfilePicture(size = 150.dp)
            Text(text = name)
            Text(text = surname)
            Text("Le mie recensioni")
            Text(text = "Impostazioni")
        }
    }
}