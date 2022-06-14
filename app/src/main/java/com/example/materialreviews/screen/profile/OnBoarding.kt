package com.example.materialreviews.screen.profile


import android.graphics.Color.alpha
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.ExperimentalAnimationApi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.materialreviews.db.UserViewModel
import com.example.materialreviews.navigation.MaterialReviewsScreen
import com.example.materialreviews.ui.theme.currentColorScheme
import com.example.materialreviews.util.MyPreferences
import com.example.materialreviews.util.OpenDocumentWithPermissions
import com.example.materialreviews.util.ProfilePicture

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun OnBoarding(model: UserViewModel,
               onClickStart: () -> Unit = {}) {
    val context = LocalContext.current
    val myPreferences = MyPreferences(context)
    val login_id = myPreferences.getId()

    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("")}
    var openDialog by remember {mutableStateOf(false)}
    if (openDialog) {

        AlertDialog(
            // Chiude il Dialog se viene cliccato nella parte oscurata
            onDismissRequest = {
                openDialog = false
            },
            modifier = Modifier.fillMaxWidth(0.90f),
            title =  { Text(text ="Pronto ad iniziare?")
            },
            // Posso mettere dentro qualsiasi composable
            text = { Text(text = "Scopri nuovi ristoranti, vedi cosa ne pensa la gente e racconta la tua esperienza! ") },

            confirmButton = {
                Button(
                    onClick = onClickStart
                ) {
                    Text("Inizia",
                        textAlign = TextAlign.Center)
                }

            },

            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {


        var imageURI by remember { mutableStateOf("") }

        val launcher = rememberLauncherForActivityResult(
            contract = OpenDocumentWithPermissions(),
        ) { newPictureURI: Uri? ->
            if(newPictureURI.toString()!="null") {
                imageURI = newPictureURI.toString()
            }
        }
        Spacer(modifier = Modifier.heightIn(8.dp))
        Text(text = "Benvenuto, effettua l'accesso:",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.heightIn(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clickable { launcher.launch( arrayOf("image/*") ) }
        ) {
            ProfilePicture(size = 140.dp, imageURI)
            // Sfondo semitrasparente
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd)
                    .background(currentColorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Modifica immagine del profilo",
                    modifier = Modifier.padding(8.dp),
                    tint = currentColorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.heightIn(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            singleLine = true,
            onValueChange = {
                val maxLength = 20
                if (it.length <= maxLength) name = it
                else Toast.makeText(context, "Non  può essere più lungo di $maxLength caratteri", Toast.LENGTH_SHORT).show()
            },
            label = { Text("Nome") }
        )

        Spacer(modifier = Modifier.heightIn(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = surname,
            onValueChange = {
                val maxLength = 20
                if (it.length <= maxLength) surname = it
                else Toast.makeText(context, "Non  può essere più lungo di $maxLength caratteri", Toast.LENGTH_SHORT).show()
            },
            label = { Text("Cognome") }
        )
        Spacer(modifier = Modifier.heightIn(16.dp))

        Button(onClick = {

            val toast = Toast.makeText(
                context,
                "I campi Nome e Cognome non possono essere vuoti",
                Toast.LENGTH_LONG
            )

            //Controllo che nome e cognome non siano vuoti
            if(name != "" || surname != "" )
            {
                //Controllo che nome e cognome non contengano solo spazi
                var nameEmpty = true
                var surnameEmpty = true

                name.forEach {
                    if (!it.equals(' ')) {
                        nameEmpty = false
                    }
                }
                surname.forEach {
                    if (!it.equals(' ')) {
                        surnameEmpty = false
                    }
                }

                //Inserimento dati nel db
                if( nameEmpty == false && surnameEmpty == false) {
                    model.updateFirstNameOfUser(login_id, name)
                    model.updateLastNameOfUser(login_id, surname)
                    myPreferences.setAccess(0)
                    openDialog = true
                }else{
                    toast.show()
                }
            }else{
                toast.show()
            }

            name = ""
            surname = ""
        }) {
            Text(text = "Vai")
        }


    }
}

