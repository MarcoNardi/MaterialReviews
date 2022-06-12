package com.example.materialreviews.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.materialreviews.R
import com.example.materialreviews.getImageBitmap
import com.example.materialreviews.ui.theme.currentColorScheme

/**
 * Immagine del profilo circolare
 */
@Preview
@Composable
fun ProfilePicture(size: Dp = 50.dp, pictureUri: String = "", borderWidth: Dp = 1.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape = CircleShape)
            .border(
                width = borderWidth,
                color = currentColorScheme.onBackground,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (pictureUri == "") {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Default image",
                contentScale = ContentScale.Crop,
            )
        }
        else {
            // Immagine del profilo
            val profilePicture = getImageBitmap(pictureUri, LocalContext.current).asImageBitmap()
            Image(
                bitmap = profilePicture,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
            )
        }
    }
}