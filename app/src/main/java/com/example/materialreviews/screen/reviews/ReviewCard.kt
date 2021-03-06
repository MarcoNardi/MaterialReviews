package com.example.materialreviews

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*
import com.example.materialreviews.ui.theme.currentColorScheme
import com.example.materialreviews.util.MyPreferences
import com.example.materialreviews.util.ProfilePicture
import com.example.materialreviews.util.RowOfStars

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
                    Text(text = "Nessuno potr?? pi?? vederla e non sar?? pi?? possibile recuperarla")
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

