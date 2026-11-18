package org.umn.gearhub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.umn.gearhub.data.dummy.DummyLocations
import org.umn.gearhub.ui.screens.activity.ActivityScreen
import org.umn.gearhub.ui.screens.addgear.AddGearScreen
import org.umn.gearhub.ui.screens.auth.AuthViewModel
import org.umn.gearhub.ui.screens.auth.LoginScreen
import org.umn.gearhub.ui.screens.auth.RegisterScreen
import org.umn.gearhub.ui.screens.detail.GearDetailScreen
import org.umn.gearhub.ui.screens.home.HomeScreen
import org.umn.gearhub.ui.screens.location.LocationScreen
import org.umn.gearhub.ui.screens.location.LocationViewModel
import org.umn.gearhub.ui.screens.payment.PaymentScreen
import org.umn.gearhub.ui.screens.payment.SuccessScreen
import org.umn.gearhub.ui.screens.profile.ProfileScreen
import org.umn.gearhub.ui.screens.rent.RentScreen
import org.umn.gearhub.ui.screens.search.SearchScreen
import org.umn.gearhub.util.activeLocationOf
import org.umn.gearhub.util.isGpsActive

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val locationViewModel: LocationViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    val locationState by locationViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedLocation by rememberSaveable { mutableStateOf(DummyLocations.DEFAULT) }
    val gpsActive = isGpsActive(locationState.userLocation, locationState.isUsingGPS)
    val activeLocation = activeLocationOf(
        selectedLocation = selectedLocation,
        userLocation = locationState.userLocation,
        isUsingGPS = locationState.isUsingGPS
    )
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
        when (route) {
            Routes.HOME -> navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME) { inclusive = false }
                launchSingleTop = true
            }
            Routes.SEARCH -> navController.navigate(Routes.search()) { launchSingleTop = true }
            Routes.ADD_GEAR -> navController.navigate(Routes.ADD_GEAR) { launchSingleTop = true }
            Routes.ACTIVITY, Routes.PROFILE -> navController.navigate(route) {
                popUpTo(Routes.HOME) { inclusive = false }
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
                activeLocation = activeLocation,
                isUsingGPS = gpsActive,
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
                locationState = locationState,
                onDetect = { granted ->
                    if (granted) {
                        locationViewModel.detect(context)
                    } else {
                        locationViewModel.onPermissionDenied()
                    }
                },
                onBack = { navController.popBackStack() },
                onConfirm = { location, useGps ->
                    selectedLocation = location
                    locationViewModel.setUsingGPS(useGps)
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.SEARCH_PATTERN, categoryArg) { entry ->
            SearchScreen(
                selectedLocation = selectedLocation,
                activeLocation = activeLocation,
                isUsingGPS = gpsActive,
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
                onRentClick = { id -> navController.navigate(Routes.rent(id)) },
                userLocation = activeLocation,
                selectedLocation = selectedLocation,
                isUsingGPS = gpsActive
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
        composable(Routes.ACTIVITY) {
            ActivityScreen(
                onRentalClick = { id -> navController.navigate(Routes.detail(id)) },
                onBottomNav = onBottomNav
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                currentUser = currentUser,
                onAddGear = { navController.navigate(Routes.ADD_GEAR) { launchSingleTop = true } },
                onActivity = { onBottomNav(Routes.ACTIVITY) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBottomNav = onBottomNav
            )
        }
        composable(Routes.ADD_GEAR) {
            AddGearScreen(
                ownerName = currentUser?.name ?: "Pengguna GearHub",
                onBack = { navController.popBackStack() },
                onPublished = { id ->
                    navController.navigate(Routes.detail(id)) {
                        popUpTo(Routes.ADD_GEAR) { inclusive = true }
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