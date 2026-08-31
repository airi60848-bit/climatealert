package com.example.climatealert.presentation.navigation




import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.climatealert.presentation.ui.advisory.AdvisoryScreen
import com.example.climatealert.presentation.ui.advisor.AdvisorChatScreen
import com.example.climatealert.presentation.ui.home.HomeScreen
import com.example.climatealert.presentation.ui.routes.RouteSelectionScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Routes.route) { RouteSelectionScreen(navController) }
        composable(Screen.Advisory.route) { AdvisoryScreen(navController) }
        composable(Screen.Advisor.route) { AdvisorChatScreen(navController) }
    }
}