package com.example.materialreviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.materialreviews.db.ReviewEntity
import com.example.materialreviews.db.UserEntity

@ExperimentalMaterial3Api
@Composable
fun ReviewCard(review: ReviewEntity, getUserInfo: (Int) -> LiveData<UserEntity>) {
    val user by getUserInfo(review.uid).observeAsState()
    val userName = (user?.firstName ?:"defaultName" ) +" "+ (user?.lastName ?:"defaultSurname" )
    val stars = review.rating
    val comment = review.review
    val date = review.date

    ElevatedCard(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.padding(bottom = 1.dp)
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

                Spacer(Modifier.weight(1f))

                // Menu
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Star",
                    )
                }
            }

            // Stelline e data
            Row() {
                RowOfStars(stars)

                Spacer(Modifier.weight(1f))

                Text(text = date)
            }

            // Testo della recensione
            Text(text = comment)
        }
    }
}