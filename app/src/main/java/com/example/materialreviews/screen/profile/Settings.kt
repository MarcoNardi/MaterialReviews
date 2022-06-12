package com.example.materialreviews.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.materialreviews.ui.theme.*


// Liste di colori per visualizzare il tema
data class ColorWithName(
    var name: String,   // Nome del colore
    var color: Color,   // Il colore in questione
    var onColor: Color  // Il colore delle scritte
)

/**
 * Produce una lista di oggetti ColorWithName. Ogni elemento e` associato ad uno dei colori del ColorScheme passato come parametro
 */
fun getColorSchemeWithName(colorScheme: ColorScheme) : List<ColorWithName> {
    val colorWithNameList = listOf(
        //ColorWithName("", colorScheme., colorScheme.),
        ColorWithName("Primary", colorScheme.primary, colorScheme.onPrimary),
        ColorWithName("On Primary", colorScheme.onPrimary, colorScheme.primary),
        ColorWithName("Primary Container", colorScheme.primaryContainer, colorScheme.onPrimaryContainer),
        ColorWithName("On Primary Container", colorScheme.onPrimaryContainer, colorScheme.primaryContainer),
        ColorWithName("Inverse Primary", colorScheme.inversePrimary, colorScheme.primary),

        ColorWithName("Secondary", colorScheme.secondary, colorScheme.onSecondary),
        ColorWithName("On Secondary", colorScheme.onSecondary, colorScheme.secondary),
        ColorWithName("Secondary Container", colorScheme.secondaryContainer, colorScheme.onSecondaryContainer),
        ColorWithName("On Secondary Container", colorScheme.onSecondaryContainer, colorScheme.secondaryContainer),

        ColorWithName("Tertiary", colorScheme.tertiary, colorScheme.onTertiary),
        ColorWithName("On Tertiary", colorScheme.onTertiary, colorScheme.tertiary),
        ColorWithName("Tertiary Container", colorScheme.tertiaryContainer, colorScheme.onTertiaryContainer),
        ColorWithName("On Tertiary Container", colorScheme.onTertiaryContainer, colorScheme.tertiaryContainer),

        ColorWithName("Background", colorScheme.background, colorScheme.onBackground),
        ColorWithName("On Background", colorScheme.onBackground, colorScheme.background),
        ColorWithName("Outline", colorScheme.outline, colorScheme.background),

        ColorWithName("Surface", colorScheme.surface, colorScheme.onSurface),
        ColorWithName("On Surface", colorScheme.onSurface, colorScheme.surface),
        ColorWithName("Surface Variant", colorScheme.surfaceVariant, colorScheme.onSurfaceVariant),
        ColorWithName("On Surface Variant", colorScheme.onSurfaceVariant, colorScheme.surfaceVariant),
        ColorWithName("Inverse Surface", colorScheme.inverseSurface, colorScheme.inverseOnSurface),
        ColorWithName("Inverse On Surface", colorScheme.inverseOnSurface, colorScheme.inverseSurface),
        ColorWithName("Surface Tint", colorScheme.surfaceTint, colorScheme.surface),

        ColorWithName("Error", colorScheme.error, colorScheme.onError),
        ColorWithName("On Error", colorScheme.onError, colorScheme.error),
        ColorWithName("Error Container", colorScheme.errorContainer, colorScheme.onErrorContainer),
        ColorWithName("On Error Container", colorScheme.onErrorContainer, colorScheme.errorContainer),
    )

    return colorWithNameList
}

/**
 * Schermata per le impostazioni
 */
@ExperimentalMaterial3Api
@Preview
@Composable
fun SettingsScreen() {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ThemeSelector()
        DynamicColorSelector()

        ColorSchemeVisualizer()
    }
}

/**
 * Visualizza il tema, mostrando i singoli colori
 */
@ExperimentalMaterial3Api
@Preview
@Composable
fun ColorSchemeVisualizer(colorBoxHeight: Dp = 36.dp) {
    val currentColorScheme: ColorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Schema di colori in uso", style = MaterialTheme.typography.titleMedium)

        val currentColorSchemeWithName = getColorSchemeWithName(currentColorScheme)

        ColorSet(currentColorSchemeWithName.subList(0, 5), colorBoxHeight)
        ColorSet(currentColorSchemeWithName.subList(5, 9), colorBoxHeight)
        ColorSet(currentColorSchemeWithName.subList(9, 13), colorBoxHeight)
        ColorSet(currentColorSchemeWithName.subList(13, 16), colorBoxHeight)
        ColorSet(currentColorSchemeWithName.subList(16, 23), colorBoxHeight)
        ColorSet(currentColorSchemeWithName.subList(23, 27), colorBoxHeight)
    }
}

/**
 * Produce una singola Box con il nome del colore e lo sfondo del colore in questione
 * @param colorItem il colore in questione.
 * @param width larghezza della Box: viene passata dal parent
 */
@Composable
fun ColorBox(colorItem: ColorWithName, width: Dp, height: Dp) {
    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .background(colorItem.color)
            .padding(start = 4.dp)
    ) {
        Text(
            text = colorItem.name,
            color = colorItem.onColor
        )
    }
}

/**
 * Data una lista di ColorAndName, raggruppa i colori in una unica Box
 */
@Composable
fun ColorSet(listOfColors: List<ColorWithName>, height: Dp) {

    // Forma del wrapper
    val boxShape = RoundedCornerShape(8.dp)

    // Grid che contiene i colori
    Box(
        modifier = Modifier
            .border(1.dp, currentColorScheme.outline, boxShape)
            .clip(shape = boxShape)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Ogni ColorBox occupa metà larghezza
            val boxMaxWidth = maxWidth

            Column() {

                // Prendo i colori a coppie e li metto "in riga per due"
                for (i in 0..listOfColors.size-1 step 2) {
                    val j = if (i >= listOfColors.size-1) 1 else 2
                    val colorCouple = listOfColors.subList(i, i+j)

                    Row() {
                        for(color in colorCouple) {
                            ColorBox(colorItem = color, width = boxMaxWidth/j, height = height)
                        }
                    }
                }
            }
        }
    }
}