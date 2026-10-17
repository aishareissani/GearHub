package org.umn.gearhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.umn.gearhub.navigation.AppNavGraph
import org.umn.gearhub.ui.screens.detail.GearDetailScreen
import org.umn.gearhub.ui.screens.search.SearchScreen
import org.umn.gearhub.ui.theme.GearHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GearHubTheme {
                AppNavGraph()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
    GearHubTheme {
        AppNavGraph()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchPreview() {
    GearHubTheme {
        SearchScreen(onGearClick = {}, onBottomNav = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GearDetailRentedPreview() {
    GearHubTheme {
        GearDetailScreen(gearId = 3, onBack = {}, onRentClick = {})
    }
}