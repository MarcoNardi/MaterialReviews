package com.example.materialreviews

import android.graphics.Picture
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

@Composable
fun ProfilePicture(size: Dp, picture: Picture? = null, borderWidth: Dp = 1.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape = CircleShape)
            .border(width = borderWidth, color = Color.Black, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (picture == null) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Localized description"
            )
        }
        else {
            // Immagine del profilo
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ReviewCard(userName: String, stars: Int,  comment: String, date: String = "69/69/6969") {

    Card(
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                //Immagine del profilo
                ProfilePicture(size = 40.dp, null, 1.dp)

                //Nome utente
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Stelline e data
            Row() {
                for (i in 0..4) {
                    val icon = Icons.Filled.Star
                    val tint = if (i<stars) Color(252, 185, 0) else Color.LightGray
                    Icon(
                        imageVector = icon,
                        tint = tint,
                        contentDescription = "Star",
                    )
                }
                
                Spacer(Modifier.weight(1f))
                
                Text(text = date)
            }

            // Testo della recensione
            Text(text = comment)
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun ReviewCardPreview() {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ReviewCard("Eli Ferin", 4, "Domenica sera sono stato qui co amici e devo dire che vengo sempre volentieri, pizze buone abbondanti, anche il ristorante è ottimo e prezzi nella norma, personale cortese, parcheggio ampio, a fine cena offrono anima nera o limoncino")
        ReviewCard("Eli Ferin", 4, "Domenica sera sono stato qui co amici e devo dire che vengo sempre volentieri, pizze buone abbondanti, anche il ristorante è ottimo e prezzi nella norma, personale cortese, parcheggio ampio, a fine cena offrono anima nera o limoncino")
        ReviewCard("Eli Ferin", 4, "Domenica sera sono stato qui co amici e devo dire che vengo sempre volentieri, pizze buone abbondanti, anche il ristorante è ottimo e prezzi nella norma, personale cortese, parcheggio ampio, a fine cena offrono anima nera o limoncino")
    }
}