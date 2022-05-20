package com.example.materialreviews

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun TestComposable(n: Int = 1) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 1..n) {
            Button(onClick = { /*TODO*/ }) {
                Text("Button $i")
            }
        }
    }
}