package com.example.materialreviews.util

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * 5 stelline, colorate in funzione del parametro
 */
@Preview
@Composable
fun RowOfStars(
    rating: Int = 4,
) {
    Row() {
        for (i in 0..4) {
            val icon = Icons.Filled.Star
            val tint = if (i<rating) Color(252, 185, 0) else Color.LightGray
            Icon(
                imageVector = icon,
                tint = tint,
                contentDescription = "Star",
            )
        }
    }
}

/**
 * 5 stelline, colorate in funzione del parametro, cliccabile
 */
@Composable
fun RowOfStars(
    rating: Int = 1,
    onClick: (Int)->Unit
) {
    Row() {
        for (i in 0..4) {
            val icon = Icons.Filled.Star
            val tint = if (i<rating) Color(252, 185, 0) else Color.LightGray

            // Quando viene cliccata una stella, passa alla lambda la posizione della stella (da 1 a 5)
            IconButton(
                onClick = { onClick(i+1) },
            ) {
                Icon(
                    imageVector = icon,
                    tint = tint,
                    contentDescription = "Star",
                )
            }

        }
    }
}