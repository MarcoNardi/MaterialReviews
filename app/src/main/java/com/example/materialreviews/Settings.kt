package com.example.materialreviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.materialreviews.ui.theme.MaterialReviewsTheme

@Composable
fun ThemeGrid() {
    val currentColorScheme: ColorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.height(50.dp)
        ) {
            ColorBox(currentColorScheme.primary)
            ColorBox(currentColorScheme.onPrimary)
            ColorBox(currentColorScheme.primaryContainer)
            ColorBox(currentColorScheme.onPrimaryContainer)
        }
        Row(
            modifier = Modifier.height(50.dp)
        ) {
            ColorBox(currentColorScheme.secondary)
            ColorBox(currentColorScheme.onSecondary)
            ColorBox(currentColorScheme.secondaryContainer)
            ColorBox(currentColorScheme.onSecondaryContainer)
        }
        Row(
            modifier = Modifier.height(50.dp)
        ) {
            ColorBox(currentColorScheme.tertiary)
            ColorBox(currentColorScheme.onTertiary)
            ColorBox(currentColorScheme.tertiaryContainer)
            ColorBox(currentColorScheme.onTertiaryContainer)
        }
        Row(
            modifier = Modifier.height(50.dp)
        ) {
            ColorBox(currentColorScheme.background)
            ColorBox(currentColorScheme.onBackground)
            ColorBox(currentColorScheme.surface)
            ColorBox(currentColorScheme.onSurface)
        }
        Row(
            modifier = Modifier.height(50.dp)
        ) {
            ColorBox(currentColorScheme.surfaceVariant)
            ColorBox(currentColorScheme.onSurfaceVariant)
            ColorBox(currentColorScheme.outline)
        }
        Row(
            modifier = Modifier.height(50.dp)
        ) {
            ColorBox(currentColorScheme.error)
            ColorBox(currentColorScheme.onError)
            ColorBox(currentColorScheme.errorContainer)
            ColorBox(currentColorScheme.onErrorContainer)
        }
    }
}

@Composable
fun ColorBox(color: Color, size: Dp = 50.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(color)
    )
}