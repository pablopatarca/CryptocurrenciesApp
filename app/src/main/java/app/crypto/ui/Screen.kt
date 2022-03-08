package app.crypto.ui

sealed class Screen(val title: String, val route: String) {
    object MainScreen: Screen("Main", "main_screen")
    object DetailsScreen: Screen("Details", "details_screen")
}
