package org.umn.gearhub.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.umn.gearhub.ui.screens.home.HomeScreen
import org.umn.gearhub.ui.screens.search.SearchScreen
import org.umn.gearhub.ui.screens.detail.GearDetailScreen

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val gearIdArg = listOf(navArgument("gearId") { type = NavType.IntType })

    NavHost(navController, startDestination = Routes.HOME, modifier = modifier) {
        composable(Routes.HOME) {
            HomeScreen(
                onGearClick = { id -> navController.navigate(Routes.detail(id)) },
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onBottomNav = { route ->
                    if (route == Routes.HOME || route == Routes.SEARCH) {
                        navController.navigate(route) {
                            popUpTo(Routes.HOME)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable(Routes.SEARCH) {
            SearchScreen(
                onGearClick = { id -> navController.navigate(Routes.detail(id)) },
                onBottomNav = { route ->
                    if (route == Routes.HOME || route == Routes.SEARCH) {
                        navController.navigate(route) {
                            popUpTo(Routes.HOME)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Routes.DETAIL, gearIdArg) { entry ->
            val gearId = entry.arguments?.getInt("gearId") ?: return@composable
            GearDetailScreen(
                gearId = gearId,
                onBack = { navController.popBackStack() },
                onRentClick = { id -> navController.navigate(Routes.rent(id)) }
            )
        }
        composable(Routes.RENT, gearIdArg) { Text("Rent") }
        composable(Routes.KTM, gearIdArg) { Text("KTM") }
    }
}