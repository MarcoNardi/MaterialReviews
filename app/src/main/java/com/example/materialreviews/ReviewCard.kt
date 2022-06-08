package com.example.materialreviews

import android.graphics.Picture
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*
import com.example.materialreviews.ui.theme.currentColorScheme
import com.example.materialreviews.util.MyPreferences

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun ReviewCard(
    review: ReviewEntity,
    getUserInfo: (Int) -> LiveData<UserEntity>,
    modifier: Modifier
) {
    // Ottengo i dati dell'utente e della review dal DB
    val user by getUserInfo(review.uid).observeAsState()
    val userName = (user?.firstName ?:"defaultName" ) +" "+ (user?.lastName ?:"defaultSurname" )
    val profilePictureUri = user?.imageUri ?: ""
    val stars = review.rating
    val comment = review.review
    val date = review.date

    val reviewViewModel: ReviewViewModel = viewModel(
        factory = ReviewViewModelFactory(
            AppDatabase.getDatabase(LocalContext.current).reviewDao()
        )
    )


    // Dialog per eliminare la review
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) {

        AlertDialog(
            onDismissRequest = { openDialog = false },
            modifier = Modifier.fillMaxWidth(0.9f),
            title = {
                Text(text = "Sicuro di voler eliminare la tua recensione?")
            },
            // Posso mettere dentro qualsiasi composable
            text = {
                Row() {
                    Text(text = "Nessuno potrà più vederla e non sarà più possibile recuperarla")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog = false
                        reviewViewModel.deleteReview(review)
                    }
                ) {
                    Text(
                        "Elimina",
                    )
                }

            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text(
                        "Annulla",
                        textAlign = TextAlign.Center
                    )
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(bottom = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //Immagine del profilo
                ProfilePicture(size = 40.dp, profilePictureUri, 1.dp)

                //Nome utente
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.weight(1f))
                if(review.uid==MyPreferences(LocalContext.current).getId()){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        DeleteReviewButton() { openDialog = true }
                    }
                }
            }

            // Stelline e data
            Row() {
                RowOfStars(stars)

                Spacer(Modifier.weight(1f))

                Text(text = date)
            }

            // Testo della recensione
            if (comment != "") Text(text = comment)

        }
    }
}

@Composable
fun DeleteReviewButton(
    onClick: ()->Unit = {}
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = currentColorScheme.tertiary, contentColor = currentColorScheme.onTertiary)
    ) {
        Text(text = "Elimina")
    }
}

@Composable
fun ProfilePicture(size: Dp, pictureUri: String = "", borderWidth: Dp = 1.dp) {
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