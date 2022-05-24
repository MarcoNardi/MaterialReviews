package com.example.materialreviews

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.materialreviews.ui.theme.DarkThemeSwitch
import com.example.materialreviews.ui.theme.DynamicColorsSwitch
import com.example.materialreviews.ui.theme.SyncWithSystemSwitch
import com.example.materialreviews.ui.theme.currentColorScheme


// Liste di colori per visualizzare il tema
data class ColorWithName(
    var name: String,
    var color: Color,
    var onColor: Color
)

fun getColorSchemeWithName(colorScheme: ColorScheme) : List<ColorWithName> {
    val colorWithNameList = listOf(
        //ColorWithName("", colorScheme., colorScheme.),
        ColorWithName("Primary", colorScheme.primary, colorScheme.onPrimary),
        ColorWithName("On Primary", colorScheme.onPrimary, colorScheme.primary),
        ColorWithName("Primary Container", colorScheme.primaryContainer, colorScheme.onPrimaryContainer),
        ColorWithName("On Primary Container", colorScheme.onPrimaryContainer, colorScheme.primaryContainer),

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
        ColorWithName("Surface", colorScheme.surface, colorScheme.onSurface),
        ColorWithName("On Surface", colorScheme.onSurface, colorScheme.surface),

        ColorWithName("Error", colorScheme.error, colorScheme.onError),
        ColorWithName("On Error", colorScheme.onError, colorScheme.error),
        ColorWithName("Error Container", colorScheme.errorContainer, colorScheme.onErrorContainer),
        ColorWithName("On Error Container", colorScheme.onErrorContainer, colorScheme.errorContainer),
    )

    return colorWithNameList
}

@Preview
@Composable
fun ThemeGrid() {
    val currentColorScheme: ColorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SyncWithSystemSwitch()
        DarkThemeSwitch()
        DynamicColorsSwitch()

        val currentColorSchemeWithName = getColorSchemeWithName(currentColorScheme)

        ColorSet(currentColorSchemeWithName.subList(0,4), "Colori primari")
        ColorSet(currentColorSchemeWithName.subList(4,8), "Colori secondari")
        ColorSet(currentColorSchemeWithName.subList(8,12), "Colori terziari")
        ColorSet(currentColorSchemeWithName.subList(12,16), "Colori per sfondi e superfici")
        ColorSet(currentColorSchemeWithName.subList(16,20), "Colori per messaggi di errore")
    }
}

@Composable
fun ColorBox(colorItem: ColorWithName, width: Dp) {
    Box(
        modifier = Modifier
            .height(35.dp)
            .width(width)
            .background(colorItem.color)
            .padding(start = 5.dp)
    ) {
        Text(
            text = colorItem.name,
            color = colorItem.onColor
        )
    }
}

@Composable
fun ColorSet(listOfColors: List<ColorWithName>, title: String) {
    // Accetta solo liste di 3 o 4 colori
    if (listOfColors.size < 3 || listOfColors.size > 4) {
        throw IllegalArgumentException("La lista di colori non è adatta")
    }

    val textSize: TextStyle = MaterialTheme.typography.titleMedium

    Text(text = title, style = textSize)

    val boxShape = RoundedCornerShape(8.dp)
       
    BoxWithConstraints(
        modifier = Modifier
            .border(1.dp, currentColorScheme.onSurface, boxShape)
            .clip(shape = boxShape)
    ) {
        val boxMaxWidth = maxWidth
        Column() {
            Row() {
                ColorBox(listOfColors[0], boxMaxWidth/2)
                ColorBox(listOfColors[2], boxMaxWidth/2)
            }
            Row() {
                ColorBox(listOfColors[1], boxMaxWidth/2)
                if (listOfColors.size == 4) {
                    ColorBox(listOfColors[3], boxMaxWidth/2)
                }
            }
        }
    }
}