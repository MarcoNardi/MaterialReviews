package com.example.materialreviews.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.ViewCompat
import com.example.materialreviews.R

// Palette generate da https://material-foundation.github.io/material-theme-builder/#/custom
private val UnipdThemeLight = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
)
private val UnipdThemeDark = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
)

/**
 * Indica se l'app ha lo stesso tema (chiaro/scuro) del sistema
 */
var isFollowingSystemDarkTheme by mutableStateOf(true)

/**
 * Indica se l'app dta utilizzando il tema scuro
 */
var isInDarkTheme by mutableStateOf(false)

/**
 * Indica se il sistema puo` utilizzare i colori dinamici
 */
val canUseDynamicColors = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)

/**
 * Indica se l'app sta utilizzando il dynamic color fornito dal sistema
 */
var isUsingDynamicColor by mutableStateOf(false)

/**
 * Schema di colori attualmente in uso nell'app
 */
var currentColorScheme: ColorScheme by mutableStateOf(UnipdThemeLight)

/**
 * Imposta il tema scuro in funzione del parametro passato
 */
fun setDarkTheme(darkTheme: Boolean = true) {
    isInDarkTheme = darkTheme
    Log.v("DynamicTheme", "Setting isInDarkTheme to $isInDarkTheme")
}

/**
 * Aggiorna il colorScheme in uso
 */
@Composable
fun updateColorScheme() {
    currentColorScheme = if (isUsingDynamicColor) {
        val context = LocalContext.current
        if (isInDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (isInDarkTheme) UnipdThemeDark else UnipdThemeLight
    }
    Log.v("DynamicTheme", "Updating currentColorScheme")
}

/**
 * Switch per attivare la sincronizzazione del tema (chiaro/scuro) con il sistema
 */
@Preview
@Composable
fun SyncWithSystemSwitch() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Switch(
            checked = isFollowingSystemDarkTheme,
            onCheckedChange = { isFollowingSystemDarkTheme = !isFollowingSystemDarkTheme }
        )
        Text("Sync theme with system")
    }
}

/**
 * Switch per passare da tema chiaro a tema scuro
 */
@Preview
@Composable
fun DarkThemeSwitch() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Switch(
            enabled = !isFollowingSystemDarkTheme,
            checked = isInDarkTheme,
            onCheckedChange = { setDarkTheme( !isInDarkTheme ) }
        )
        Text("Dark theme")
    }
}

/**
 * Tre Pulsanti per selector il tema tra le opzioni Chiaro | Scuro | Sistema
 */
@ExperimentalMaterial3Api
@Preview
@Composable
fun ThemeSelector() {
    // Indica quale tema e` attualmente in uso
    val selectedTheme = if (isFollowingSystemDarkTheme) "Sistema" else { if(isInDarkTheme) "Scuro" else "Chiaro" }

    // Forme per i pulsanti
    val leftShape = multiButtonShapes[0]
    val centerShape = multiButtonShapes[1]
    val rightShape = multiButtonShapes[2]

    // https://stackoverflow.com/questions/65665563/android-jetpack-compose-icons-doesnt-contain-some-of-the-material-icons
    // Icone per i temi
    val lightIconPainter = painterResource(id = R.drawable.ic_baseline_brightness_7_24)
    val darkIconPainter = painterResource(id = R.drawable.ic_baseline_brightness_3_24)
    val systemIconPainter = painterResource(id = R.drawable.ic_baseline_android_24)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(
            text = "Tema",
            style = MaterialTheme.typography.titleMedium
        )
        Row() {
            MultiToggleButtonItem(
                name = "Chiaro",
                iconPainter = lightIconPainter,
                shape = leftShape,
                index = 0,
                selectedItem = selectedTheme,
                enabled = true,
                onClick = {
                    isFollowingSystemDarkTheme = false
                    setDarkTheme(false)
                },
            )
            MultiToggleButtonItem(
                name = "Scuro",
                iconPainter = darkIconPainter,
                shape = centerShape,
                index = 1,
                selectedItem = selectedTheme,
                enabled = true,
                onClick = {
                    isFollowingSystemDarkTheme = false
                    setDarkTheme(true)
                },
            )
            MultiToggleButtonItem(
                name = "Sistema",
                iconPainter = systemIconPainter,
                shape = rightShape,
                index = 2,
                selectedItem = selectedTheme,
                enabled = true,
                onClick = {
                    isFollowingSystemDarkTheme = true
                },
            )
        }
    }
}

/**
 * Singoli elementi del ThemeSelector
 */
@ExperimentalMaterial3Api
@Composable
fun MultiToggleButtonItem(
    name: String,
    iconPainter: Painter,
    shape: Shape,
    index: Int,
    selectedItem: String,
    enabled: Boolean,
    onClick: ()->Unit
) {
    // Colori per i pulsanti
    val selectedColor = currentColorScheme.primaryContainer
    val unselectedColor = currentColorScheme.background
    val selectedContentColor = currentColorScheme.onPrimaryContainer
    val unselectedContentColor = currentColorScheme.outline

    val selected = (selectedItem == name)

    OutlinedButton(
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) selectedColor else unselectedColor,
            contentColor = if (selected) selectedContentColor else unselectedContentColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) selectedContentColor else unselectedContentColor
        ),
        onClick = onClick,

        //https://stackoverflow.com/questions/67023923/materialbuttontogglegroup-in-jetpack-compose
        modifier = when (index) {
            0 -> Modifier
                .offset(0.dp, 0.dp)
                .zIndex(if (selected) 1f else 0f)
            else -> Modifier
                .offset((-1 * index).dp, 0.dp)
                .zIndex(if (selected) 1f else 0f)
        }
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = "theme",
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(name)
    }
}

/**
 * Multi toggle button per utilizzare i colori dinamici o meno
 */
@ExperimentalMaterial3Api
@Preview
@Composable
fun DynamicColorSelector() {
    // Indica quali colori sono attualmente in uso nell'app
    val selectedColors = if (isUsingDynamicColor) "Dinamici" else "Default"

    // Forme per i pulsanti
    val leftShape = multiButtonShapes[0]
    val rightShape = multiButtonShapes[2]

    // Icone
    val dynamicIconPainter = painterResource(id = R.drawable.ic_baseline_color_lens_24)
    val defaultIconPainter = painterResource(id = R.drawable.ic_baseline_android_24)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Colori",
            style = MaterialTheme.typography.titleMedium
        )
        Row() {
            MultiToggleButtonItem(
                name = "Dinamici",
                iconPainter = dynamicIconPainter,
                shape = leftShape,
                index = 0,
                selectedItem = selectedColors,
                enabled = canUseDynamicColors,
                onClick = {
                    isUsingDynamicColor = true
                },
            )
            MultiToggleButtonItem(
                name = "Default",
                iconPainter = defaultIconPainter,
                shape = rightShape,
                index = 1,
                selectedItem = selectedColors,
                enabled = true,
                onClick = {
                    isUsingDynamicColor = false
                },
            )
        }
    }
}


/**
 * Switch per usare i colori dinamici o il tema di default
 */
@Preview
@Composable
fun DynamicColorsSwitch() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Switch(
            enabled = canUseDynamicColors,
            checked = isUsingDynamicColor,
            onCheckedChange = { isUsingDynamicColor = !isUsingDynamicColor }
        )
        Text("DynamicColors")
    }
}

/**
 * Tema attualmente in uso nell'app
 */
@Composable
fun MaterialReviewsTheme(
    content: @Composable () -> Unit
) {
    // Se sto usando il tema di sistema aggiorno la variabile
    if (isFollowingSystemDarkTheme) {
        setDarkTheme(isSystemInDarkTheme())
    }

    // Imposto il colorScheme in funzione dei vari parametri di tema e colore
    updateColorScheme()

    // Cambia il colore della status bar di sistema
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = currentColorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = isInDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = currentColorScheme,
        typography = Typography,
        content = content
    )
}