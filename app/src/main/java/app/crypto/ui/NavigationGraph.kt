package app.crypto.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import app.crypto.ui.details.CryptoDetailScreen
import app.crypto.ui.main.CryptoList
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun NavigationGraph(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            CryptoList(
                state = viewModel.state.collectAsState(),
                onClick = {
                    navController.navigate(
                        Screen.DetailsScreen.route + "?id=${it.id}"
                    )
                }
            )
        }
        composable(
            route = Screen.DetailsScreen.route + "?id={id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            CryptoDetailScreen()
        }
    }
}