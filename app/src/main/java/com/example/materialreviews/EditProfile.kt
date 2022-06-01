package com.example.materialreviews

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@ExperimentalMaterial3Api
@Preview
@Composable
fun EditProfile() {
    Column(modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {

        var expandPersonalData by remember { mutableStateOf(false)  }
        var expandPassword by remember { mutableStateOf(false)}

        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var bitmap by remember { mutableStateOf<Bitmap?>(null) }
        val launcher = rememberLauncherForActivityResult(contract =
        ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
        val context = LocalContext.current
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images
                    .Media.getBitmap(context.contentResolver,it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
                bitmap  = ImageDecoder.decodeBitmap(source)
            }
        }

            bitmap.let {  btm ->
                if (btm != null) {
                    /*
                    Image(bitmap = btm.asImageBitmap(),
                        contentDescription =null,
                        modifier = Modifier.size(400.dp))
                        */
                }

                }

        Divider(
            Modifier.padding(horizontal = 20.dp),
            color = Color.Gray,
            thickness = 1.dp
        )
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "Pick image")
        }
        ExpandableCard(onCardArrowClick = {expandPersonalData = !(expandPersonalData) }, expanded = expandPersonalData, "Modifica dati anagrafici", 1)
        ExpandableCard(onCardArrowClick = {expandPassword = !(expandPassword) }, expanded = expandPassword, "Modifica password", 2)
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
    type:Int
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")

    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "paddingTransition") {
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

                ExpandableContent(visible = expanded, type)

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
    type: Int
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
        EditPersonalData()
        }else if (type == 2) {
            EditPassword()
        }
    }
}

@Composable
fun EditPersonalData()
{
    var name by rememberSaveable { mutableStateOf("Text") }
    var surname by rememberSaveable { mutableStateOf("Text2")}
    Column(modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.heightIn(10.dp))
        TextField(
            value = name,
            textStyle = TextStyle(fontSize = 20.sp),
            onValueChange = {
                name = it
            }, label = {Text(text ="Nome",
                Modifier.padding(3.dp))},
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = androidx.compose.ui.graphics.Color.White,
                focusedIndicatorColor =  androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor =  androidx.compose.ui.graphics.Color.Transparent
            )
        )
        Spacer(modifier = Modifier.heightIn(8.dp))
        TextField(
            value = surname,
            textStyle = TextStyle(fontSize = 20.sp),
            shape = RoundedCornerShape(8.dp),
            onValueChange = {
                surname = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = androidx.compose.ui.graphics.Color.White,
                focusedIndicatorColor =  androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor =androidx.compose.ui.graphics.Color.Transparent
            ),
            label = {Text(
                text = "Cognome",
                Modifier.padding(top = 5.dp, bottom = 5.dp))}
        )
        Spacer(modifier = Modifier.heightIn(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Salva")
        }
    }

}

@Composable
fun EditPassword() {
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
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Salva")
        }
    }
}


