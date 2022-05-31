package com.example.materialreviews.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home

enum class MaterialReviewsScreen ( ) {
    Explore(  ),
    Reviews( ),
    Favourites(),
    Profile(),
    Settings(),
    EditProfile();

    companion object {
        fun fromRoute(route: String?): MaterialReviewsScreen =
            when (route?.substringBefore("/")) {
                Explore.name -> Explore
                Reviews.name -> Reviews
                Favourites.name -> Favourites
                Profile.name -> Profile
                Settings.name -> Settings
                EditProfile.name -> EditProfile
                null -> Explore
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }

    }
}
