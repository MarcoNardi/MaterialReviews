package com.example.materialreviews

import android.graphics.drawable.Icon
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.AppDatabase
import com.example.materialreviews.db.ImageViewModel
import com.example.materialreviews.db.UserViewModel
import com.example.materialreviews.db.UserViewModelFactory
import com.example.materialreviews.util.MyPreferences

@Preview
@Composable
fun ProfileScreen() {
    // Estraggo l'id dell'utente dalle preferences
    val context = LocalContext.current
    val userID = MyPreferences(context).getId()

    // Ottengo l'istanza dell'utente dal DB
    val userModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            AppDatabase.getDatabase(
                context
            ).userDao()
        )
    )
    val user by userModel.getUser(userID).observeAsState()

    // Estraggo i dati dell'utente
    val userName = user?.firstName ?: "Default"
    val userSurname = user?.lastName ?: "Default"
    val profilePictureURI = user?.imageUri ?: ""

    // Dialog per modificare i dati del profilo
    var showEditProfileDialog by remember{ mutableStateOf(false) }
    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = userName,
            currentSurname = userSurname,
            currentProfilePictureURI = profilePictureURI,
            onDismiss = { showEditProfileDialog = false },
            onConfirm = {
                /* TODO: Salva nel DB */
                showEditProfileDialog = false
            }
        )
    }

    // Dialog per modificare il tema
    var showEditThemeDialog by remember{ mutableStateOf(false) }

    // Corpo di ProfileScreen
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Immagine profilo + nome e cognome
        Row (
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProfilePicture(size = 150.dp, profilePictureURI)
            Text(
                text = "$userName $userSurname",
                style = MaterialTheme.typography.displaySmall
            )
        }

        // Pulsante per modificare il profilo
        Button(
            onClick = {showEditProfileDialog = true},
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.Person, "Modifica il profilo")
                Text("Modifica il profilo")
            }
        }

        // Pulsante per modificare il tema
        Button(
            onClick = {showEditThemeDialog = true},
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(painterResource(id = R.drawable.ic_baseline_color_lens_24), "Seleziona il tema")
                Text("Seleziona il tema")
            }
        }
    }

}

@Preview
@Composable
fun TextIconButton(
    icon: @Composable (() -> Unit) = {},
    text: String = "Prova",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon
            Text(text)
        }
    }
}

@Preview
@Composable
fun EditProfileDialog(
    currentName: String = "Default",
    currentSurname: String = "Default",
    currentProfilePictureURI: String = "",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modifica il profilo") },
        text = { Text("Prova") },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Annulla")
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Salva")
            }
        },
    )
}