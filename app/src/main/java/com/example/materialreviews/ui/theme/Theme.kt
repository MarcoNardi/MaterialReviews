package com.example.materialreviews.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

// Indica se l'app ha lo stesso tema (chiaro/scuro) del sistema
var isFollowingSystemDarkTheme by mutableStateOf(false)

// Indica se l'app dta utilizzando il tema scuro
var isInDarkTheme by mutableStateOf(false)

// Indica se l'app sta utilizzando il dynamic color fornito dal sistema
var isUsingDynamicColor by mutableStateOf(false)

var LightColorScheme = UnipdThemeLight
var DarkColorScheme = UnipdThemeDark

// Schema di colori attualmente in uso nell'app
var currentColorScheme: ColorScheme by mutableStateOf(LightColorScheme)

// Funzione che setta il tema chiaro/scuro
fun setDarkTheme(darkTheme: Boolean = true) {
    isInDarkTheme = darkTheme
    currentColorScheme = if(darkTheme) DarkColorScheme else LightColorScheme
}

// Switch per attivare la sincronizzazione del tema (chiaro/scuro) con il sistema
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

// Switch per passare da tema chiaro a tema scuro
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

// Switch per usare i colori dinamici o il tema di default
@Preview
@Composable
fun DynamicColorsSwitch() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Switch(
            enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
            checked = isUsingDynamicColor,
            onCheckedChange = { isUsingDynamicColor = !isUsingDynamicColor }
        )
        Text("DynamicColors")
    }
}

@Composable
fun MaterialReviewsTheme(
    content: @Composable () -> Unit
) {
    /*
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }
    */
    if (isUsingDynamicColor) {
        // Controlla che sia su Android 12
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            LightColorScheme = dynamicLightColorScheme(context)
            DarkColorScheme = dynamicDarkColorScheme(context)
        }
    }
    else {
        LightColorScheme = UnipdThemeLight
        DarkColorScheme = UnipdThemeDark
    }

    if (isFollowingSystemDarkTheme) {
        setDarkTheme(isSystemInDarkTheme())
    }
    else {
        setDarkTheme(isInDarkTheme)
    }

    MaterialTheme(
        colorScheme = currentColorScheme,
        typography = Typography,
        content = content
    )
}