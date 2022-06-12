package com.example.materialreviews

import android.graphics.drawable.Icon
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.AppDatabase
import com.example.materialreviews.db.ImageViewModel
import com.example.materialreviews.db.UserViewModel
import com.example.materialreviews.db.UserViewModelFactory
import com.example.materialreviews.screen.profile.SettingsScreen
import com.example.materialreviews.ui.theme.currentColorScheme
import com.example.materialreviews.util.MyPreferences
import com.example.materialreviews.util.OpenDocumentWithPermissions
import com.example.materialreviews.util.ProfilePicture

/**
 * Schermata per la gestione del profilo
 */
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
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
            onConfirm = { newName, newSurname, newProfilePictureURI ->
                //Controllo che nome e cognome non siano vuoti
                val toast = Toast.makeText(context,"I campi Nome e Cognome non possono essere vuoti",Toast.LENGTH_LONG)
                if(newName != "" || newSurname != "" )
                {
                    //Controllo che nome e cognome non contengano solo spazi
                    var nameEmpty = true
                    var surnameEmpty = true

                    newName.forEach {
                        if (!it.equals(' ')) {
                            nameEmpty = false
                        }
                    }
                    newSurname.forEach {
                        if (!it.equals(' ')) {
                            surnameEmpty = false
                        }
                    }

                    //Inserimento dati nel db
                    if( nameEmpty == false && surnameEmpty == false) {
                        userModel.updateFirstNameOfUser(userID, newName)
                        userModel.updateLastNameOfUser(userID, newSurname)
                        userModel.updateImageOfUser(userID, newProfilePictureURI)
                        showEditProfileDialog = false
                    }else{
                        toast.show()
                    }
                }else{
                    toast.show()
                }

            }
        )
    }

    // Dialog per modificare il tema
    var showEditThemeDialog by remember{ mutableStateOf(false) }
    if (showEditThemeDialog) {
        EditThemeDialog(
            onDismiss = { showEditThemeDialog = false }
        )
    }

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
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ProfilePicture(size = 160.dp, profilePictureURI)
            Text(
                text = "$userName $userSurname",
                style = MaterialTheme.typography.displaySmall
            )
        }

        // Divisore
        Divider(
            color = currentColorScheme.onSurface,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(0.8f)
        )

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

/**
 * Dialog che permette di selezioanre nome, cognome e foto profilo
 */
@Composable
fun EditProfileDialog(
    currentName: String = "Default",
    currentSurname: String = "Default",
    currentProfilePictureURI: String = "",
    onDismiss: () -> Unit = {},
    onConfirm: (newName: String, newSurname: String, newProfilePictureURI: String) -> Unit
) {
    val context = LocalContext.current
    // Variabili per nome, cognome e URI
    var name by remember { mutableStateOf(currentName)}
    var surname by remember { mutableStateOf(currentSurname)}
    var pictureURI by remember { mutableStateOf(currentProfilePictureURI)}

    // Launcher per acquisire la nuova immagine profilo dall galleria
    val launcher = rememberLauncherForActivityResult(
        contract = OpenDocumentWithPermissions(),
    ) { newPictureURI: Uri? ->
        if(newPictureURI.toString()!="null") {
            pictureURI = newPictureURI.toString()
        }
    }

    //Numero massimo di caratteri per nome e cognome
    val maxLength = 20

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modifica il profilo") },
        text = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Immagine del profilo con pulsante per modificarla
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable { launcher.launch( arrayOf("image/*") ) }
                ) {
                    ProfilePicture(size = 140.dp, pictureURI)
                    // Sfondo semitrasparente
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(currentColorScheme.background.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Modifica immagine del profilo",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
                
                // Text Field per il nome
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        if (it.length <= maxLength) name = it
                        else Toast.makeText(context, "Non  può essere più lungo di $maxLength caratteri", Toast.LENGTH_SHORT).show()
                    },
                    label = { Text("Nome")}
                )

                // Text Field per il cognome
                OutlinedTextField(
                    value = surname,
                    onValueChange = {
                        if (it.length <= maxLength) surname = it
                        else Toast.makeText(context, "Non  può essere più lungo di $maxLength caratteri", Toast.LENGTH_SHORT).show()
                    },
                    label = { Text("Cognome")}
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Annulla")
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm( name, surname, pictureURI ) }
            ) {
                Text("Salva")
            }
        },
    )
}

/**
 * Dialog che permette di selezionare il tema
 */
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun EditThemeDialog(
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleziona il tema") },
        text = {
            SettingsScreen()
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Chiudi")
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxSize(0.9f)
    )
}