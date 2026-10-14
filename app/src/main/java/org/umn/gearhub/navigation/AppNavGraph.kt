package org.umn.gearhub.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.umn.gearhub.data.dummy.DummyGears
import android.R.attr.type

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val gearIdArg = listOf(navArgument("gearId") { type = NavType.IntType })

    NavHost(navController, startDestination = Routes.HOME, modifier = modifier) {
        composable(Routes.HOME) { Text("Home") }
        composable(Routes.SEARCH) { Text("Search") }
        composable(Routes.DETAIL, gearIdArg) { entry ->
            val gearId = entry.arguments?.getInt("gearId") ?: return@composable
            val gear = DummyGears.getById(gearId)
            Text("Detail: ${gear?.name}")
        }
        composable(Routes.RENT, gearIdArg) { Text("Rent") }
        composable(Routes.KTM, gearIdArg) { Text("KTM") }
    }
}