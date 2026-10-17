package org.umn.gearhub.navigation

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.umn.gearhub.ui.screens.auth.AuthViewModel
import org.umn.gearhub.ui.screens.auth.LoginScreen
import org.umn.gearhub.ui.screens.auth.RegisterScreen
import org.umn.gearhub.ui.screens.detail.GearDetailScreen
import org.umn.gearhub.ui.screens.home.HomeScreen
import org.umn.gearhub.ui.screens.rent.RentScreen
import org.umn.gearhub.ui.screens.search.SearchScreen

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    val gearIdArg = listOf(navArgument("gearId") { type = NavType.IntType })
    val categoryArg = listOf(
        navArgument("category") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    )

    val onBottomNav: (String) -> Unit = { route ->
        if (route == Routes.HOME || route == Routes.SEARCH) {
            navController.navigate(route) {
                popUpTo(Routes.HOME) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(navController, startDestination = Routes.LOGIN, modifier = modifier) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onBack = { (context as? Activity)?.finish() },
                onLoginSuccess = { user ->
                    authViewModel.login(user)
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER) { launchSingleTop = true }
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.popBackStack() },
                onLoginClick = { navController.popBackStack() }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                currentUser = currentUser,
                onGearClick = { id -> navController.navigate(Routes.detail(id)) },
                onSearchClick = { navController.navigate(Routes.search()) },
                onCategoryClick = { category ->
                    navController.navigate(Routes.search(category.name))
                },
                onBottomNav = onBottomNav
            )
        }
        composable(Routes.SEARCH_PATTERN, categoryArg) { entry ->
            SearchScreen(
                initialCategory = entry.arguments?.getString("category"),
                onGearClick = { id -> navController.navigate(Routes.detail(id)) },
                onBottomNav = onBottomNav
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
        composable(Routes.RENT, gearIdArg) {
            RentScreen(
                onBack = { navController.popBackStack() },
                onContinue = { id -> navController.navigate(Routes.payment(id)) }
            )
        }
        composable(Routes.PAYMENT, gearIdArg) { Text("Pembayaran") }
        composable(Routes.KTM, gearIdArg) { Text("KTM") }
    }
}