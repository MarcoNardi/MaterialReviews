package com.example.materialreviews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.materialreviews.db.*
import com.example.materialreviews.navigation.NavigationManager
import com.example.materialreviews.screen.profile.OnBoarding
import com.example.materialreviews.ui.theme.MaterialReviewsTheme
import com.example.materialreviews.util.MyPreferences

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        PRIORITA DA ALTO A BASSO
        TODO RELAZIONE: RIDURRE PARTE INIZIALE, PARTE MATERIAL 3 CON RIFERIMENTI ALLA NOSTRA APP
        TODO lag mie recensioni
        TODO Pulizia
        TODO CARD PER QUANDO NON CI SONO PREFERITI
        TODO schermo orizzontale
        */
        super.onCreate(savedInstanceState)
        setContent {
            M3App()
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun M3App() {
    MaterialReviewsTheme() {
            NavigationManager()

    }
}