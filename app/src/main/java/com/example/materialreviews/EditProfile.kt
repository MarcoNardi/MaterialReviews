package com.example.materialreviews

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.materialreviews.db.UserViewModel


@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api

@Composable
fun EditProfile(model: UserViewModel) {

    val context = LocalContext.current
    val myPreferences = MyPreferences(context)
    var login_id = myPreferences.getId()
    val user by model.getUser(login_id).observeAsState()
    val name = (user?.firstName ?: "Not")
    val surname = (user?.lastName ?: "Logged")
    val profilePictureUri = user?.imageUri ?: ""

    var openDialog by remember {mutableStateOf(false)}
    var openData by remember { mutableStateOf("")}

    var confirmText: String
    when(openData) {

        "Cambia" -> confirmText = "CAMBIA ACCOUNT"
        "Elimina" -> confirmText = "ELIMINA"
        else -> {
            confirmText ="Errore"
        }
    }


    if (openDialog) {

        AlertDialog(
            // Chiude il Dialog se viene cliccato nella parte oscurata
            onDismissRequest = {
                openDialog = false
            },
            modifier = Modifier.fillMaxWidth(0.90f),
            title = {
                when(openData) {
                    "Cambia" -> Text(text = "Cambiare l'account?")
                    "Elimina" -> Text(text = "Eliminare l'account?")
                    else -> {
                        Text(text ="Errore")
                    }
                }
            },
            // Posso mettere dentro qualsiasi composable
            text = {
                when(openData) {
                    "Cambia" -> Text(text = "Sei sicuro di voler cambiare account?")
                    "Elimina" -> Text(text = "Verranno eliminate tutte le recensioni e i dati, non sarà più possibile accedervi")
                } } ,

            confirmButton = {
                Button(
                    onClick = {
                        if(openData == "Elimina") {
                            //TODO
                        }
                        openDialog = false
                    }
                ) {
                    Text(confirmText,
                        textAlign = TextAlign.Center)
                }

            },
            dismissButton = {
                TextButton(
                    onClick = {
                            openDialog = false
                    }
                ) {
                    Text(text = "ANNULLA")
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        var expandPersonalData by remember { mutableStateOf(false)  }
        var expandPassword by remember { mutableStateOf(false)}

        var imageUri by remember { mutableStateOf<Uri?>(null) }


        val launcher = rememberLauncherForActivityResult(contract =
        OpenDocumentWithPermissions(), ) { uri: Uri? ->
            if(uri.toString()!="null"){
                imageUri = uri
                model.updateImageOfUser(login_id, imageUri.toString())

            }
        }


        Row(modifier = Modifier.padding(start = 47.dp)) {

            ProfilePicture(size = 150.dp, pictureUri = profilePictureUri)

            val input = arrayOf("image/*")
            IconButton(onClick = { launcher.launch(input) },
                modifier = Modifier.padding(top = 100.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Text(
            text = "$name $surname",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .padding(top = 10.dp)
        )


        Divider(
            Modifier.padding(horizontal = 20.dp),
            color = Color.Gray,
            thickness = 1.dp
        )
        ExpandableCard(onCardArrowClick = {expandPersonalData = !(expandPersonalData) }, expanded = expandPersonalData, "Modifica dati anagrafici", 1, model)
        ExpandableCard(onCardArrowClick = {expandPassword = !(expandPassword) }, expanded = expandPassword, "Modifica password", 2, model)

        Spacer(modifier = Modifier.heightIn(15.dp))
        Text(text = AnnotatedString("Cambia account"),
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable {
                    openData = "Cambia"
                    openDialog = true
                }

        )
        Spacer(modifier = Modifier.heightIn(7.dp))

        Text(text = AnnotatedString(" Elimina account"),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(top = 20.dp)
                .clickable {
                    openData = "Elimina"
                    openDialog = true
                }
        )
    }
}
const val EXPAND_ANIMATION_DURATION = 300
const val COLLAPSE_ANIMATION_DURATION = 300
const val FADE_IN_ANIMATION_DURATION = 350
const val FADE_OUT_ANIMATION_DURATION = 300



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    title:String,
    type:Int,
    model: UserViewModel
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")

    val cardPaddingHorizontal by transition.animateDp(
        {
            tween(durationMillis = EXPAND_ANIMATION_DURATION)
        },
        label = "paddingTransition"
    ) {
        if (expanded) 20.dp else 24.dp
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = EXPAND_ANIMATION_DURATION,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        if (expanded) 10.dp else 16.dp
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Card(
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column {
            Box {
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClick
                )
                CardTitle(title = title)
            }

                ExpandableContent(visible = expanded, type, model)

        }
    }

}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_less_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@Composable
fun CardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    type: Int,
    model:UserViewModel
) {
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        if(type == 1) {
            EditPersonalData(model)
        }else if (type == 2) {
            EditPassword(model)
        }
    }
}

@Composable
fun EditPersonalData(
    model:UserViewModel
) {
    val context = LocalContext.current
    val myPreferences = MyPreferences(context)
    val login_id = myPreferences.getId()
    val user by model.getUser(login_id).observeAsState()
    val oldName = (user?.firstName ?: "")
    val oldSurname = (user?.lastName ?: "")

    var name by rememberSaveable { mutableStateOf(oldName) }
    var surname by rememberSaveable { mutableStateOf(oldSurname)}
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
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
                }else{
                    toast.show()
                }
            }else{
                toast.show()
            }

            name = ""
            surname = ""
        }) {
            Text(text = "Salva")
        }
    }

}

@Composable
fun EditPassword(model: UserViewModel) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.heightIn(10.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(8.dp),
            label = { Text("Inserisci la Password") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = androidx.compose.ui.graphics.Color.White,
                focusedIndicatorColor =  androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor =androidx.compose.ui.graphics.Color.Transparent
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(R.drawable.ic_baseline_visibility_24)
                else painterResource(R.drawable.ic_baseline_visibility_off_24)

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.heightIn(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(8.dp),
            label = { Text("Inserisci la Password") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = androidx.compose.ui.graphics.Color.White,
                focusedIndicatorColor =  androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor =androidx.compose.ui.graphics.Color.Transparent
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(R.drawable.ic_baseline_visibility_24)
                else painterResource(R.drawable.ic_baseline_visibility_off_24)

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.heightIn(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(8.dp),
            label = { Text("Reinserisci la nuova Password") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = androidx.compose.ui.graphics.Color.White,
                focusedIndicatorColor =  androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor =androidx.compose.ui.graphics.Color.Transparent
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(R.drawable.ic_baseline_visibility_24)
                else painterResource(R.drawable.ic_baseline_visibility_off_24)

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.heightIn(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Salva")
        }
    }
}


