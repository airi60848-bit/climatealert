package com.example.climatealert.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Routes : Screen("routes")
    data object Advisory : Screen("advisory")
    data object Advisor : Screen("advisor")
}