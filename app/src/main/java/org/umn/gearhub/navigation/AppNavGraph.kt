package org.umn.gearhub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.umn.gearhub.data.dummy.DummyLocations
import org.umn.gearhub.ui.screens.auth.AuthViewModel
import org.umn.gearhub.ui.screens.auth.LoginScreen
import org.umn.gearhub.ui.screens.auth.RegisterScreen
import org.umn.gearhub.ui.screens.detail.GearDetailScreen
import org.umn.gearhub.ui.screens.home.HomeScreen
import org.umn.gearhub.ui.screens.location.LocationScreen
import org.umn.gearhub.ui.screens.payment.PaymentScreen
import org.umn.gearhub.ui.screens.payment.SuccessScreen
import org.umn.gearhub.ui.screens.rent.RentScreen
import org.umn.gearhub.ui.screens.search.SearchScreen

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    var selectedLocation by rememberSaveable { mutableStateOf(DummyLocations.DEFAULT) }
    val gearIdArg = listOf(navArgument("gearId") { type = NavType.IntType })
    val paymentArgs = listOf(
        navArgument("gearId") { type = NavType.IntType },
        navArgument("pickup") { type = NavType.LongType },
        navArgument("returnDate") { type = NavType.LongType }
    )
    val successArgs = paymentArgs + navArgument("methodId") { type = NavType.IntType }
    val categoryArg = listOf(
        navArgument("category") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    )

    val onBottomNav: (String) -> Unit = { route ->
        if (route == Routes.HOME) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME) { inclusive = false }
                launchSingleTop = true
            }
        } else if (route == Routes.SEARCH) {
            navController.navigate(Routes.search()) {
                launchSingleTop = true
            }
        }
    }

    NavHost(navController, startDestination = Routes.LOGIN, modifier = modifier) {
        composable(Routes.LOGIN) {
            LoginScreen(
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
                onRegisterSuccess = { navController.popBackStack() },
                onLoginClick = { navController.popBackStack() }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                currentUser = currentUser,
                selectedLocation = selectedLocation,
                onLocationClick = {
                    navController.navigate(Routes.LOCATION) { launchSingleTop = true }
                },
                onGearClick = { id -> navController.navigate(Routes.detail(id)) },
                onSearchClick = { navController.navigate(Routes.search()) },
                onCategoryClick = { category ->
                    navController.navigate(Routes.search(category.name))
                },
                onBottomNav = onBottomNav
            )
        }
        composable(Routes.LOCATION) {
            LocationScreen(
                selectedLocation = selectedLocation,
                onBack = { navController.popBackStack() },
                onConfirm = { location ->
                    selectedLocation = location
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.SEARCH_PATTERN, categoryArg) { entry ->
            SearchScreen(
                selectedLocation = selectedLocation,
                onLocationClick = {
                    navController.navigate(Routes.LOCATION) { launchSingleTop = true }
                },
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
                onContinue = { id, pickup, returnDate ->
                    navController.navigate(Routes.payment(id, pickup, returnDate))
                }
            )
        }
        composable(Routes.PAYMENT, paymentArgs) {
            PaymentScreen(
                onBack = { navController.popBackStack() },
                onPaid = { id, pickup, returnDate, methodId ->
                    navController.navigate(Routes.success(id, pickup, returnDate, methodId)) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.SUCCESS, successArgs) { entry ->
            val args = entry.arguments
            SuccessScreen(
                gearId = args?.getInt("gearId") ?: -1,
                pickupMillis = args?.getLong("pickup") ?: 0L,
                returnMillis = args?.getLong("returnDate") ?: 0L,
                methodId = args?.getInt("methodId") ?: 0,
                onHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}