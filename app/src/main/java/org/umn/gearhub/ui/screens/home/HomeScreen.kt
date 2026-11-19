package org.umn.gearhub.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.umn.gearhub.R
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.dummy.DummyLocations
import org.umn.gearhub.data.model.GPSLocation
import org.umn.gearhub.data.model.GearCategory
import org.umn.gearhub.data.model.User
import org.umn.gearhub.navigation.Routes
import org.umn.gearhub.ui.components.BottomNavBar
import org.umn.gearhub.ui.components.GearCard
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.ui.theme.PillShape
import org.umn.gearhub.util.gearDistanceKm
import org.umn.gearhub.util.gearDistanceLabel
import org.umn.gearhub.util.initialsOf

@Composable
fun HomeScreen(
    selectedLocation: String,
    onLocationClick: () -> Unit,
    onGearClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (GearCategory) -> Unit,
    onBottomNav: (String) -> Unit,
    modifier: Modifier = Modifier,
    currentUser: User? = null,
    activeLocation: GPSLocation? = null,
    isUsingGPS: Boolean = false
) {
    val nearby = remember(selectedLocation, activeLocation) {
        GearRepository.list
            .sortedBy { gearDistanceKm(activeLocation, it) }
            .take(6)
    }
    val categories = GearCategory.entries
    val sheetShape = MaterialTheme.shapes.large.copy(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize
    )
    val initials = currentUser?.let { initialsOf(it.name) } ?: "G"

    Scaffold(
        modifier = modifier,
        bottomBar = { BottomNavBar(currentRoute = Routes.HOME, onNavigate = onBottomNav) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            Box(Modifier.fillMaxWidth()) {
                val yellow = MaterialTheme.colorScheme.secondary
                Canvas(Modifier.matchParentSize().clipToBounds()) {
                    val w = size.width
                    val h = size.height

                    val bigRadius = w * 0.46f
                    val bigCenter = Offset(w * 0.92f, h * 0.10f)
                    drawCircle(
                        brush = Brush.linearGradient(
                            colors = listOf(yellow.copy(alpha = 0.55f), yellow.copy(alpha = 0f)),
                            start = Offset(bigCenter.x, bigCenter.y - bigRadius),
                            end = Offset(bigCenter.x, bigCenter.y + bigRadius)
                        ),
                        radius = bigRadius,
                        center = bigCenter
                    )

                    val smallRadius = w * 0.34f
                    val smallCenter = Offset(w * 0.06f, h * 0.62f)
                    drawCircle(
                        brush = Brush.linearGradient(
                            colors = listOf(yellow.copy(alpha = 0.55f), yellow.copy(alpha = 0f)),
                            start = Offset(smallCenter.x, smallCenter.y - smallRadius),
                            end = Offset(smallCenter.x, smallCenter.y + smallRadius)
                        ),
                        radius = smallRadius,
                        center = smallCenter
                    )
                }

                Column(
                    Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn, null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(
                            Modifier
                                .weight(1f)
                                .clickable(onClick = onLocationClick)
                        ) {
                            Text(
                                "Lokasi pencarian",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    selectedLocation,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown, "Ganti lokasi",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Surface(
                            onClick = { },
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Notifications, "Notifikasi",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    initials,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    Surface(
                        onClick = onSearchClick,
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Search, null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Cari gear…",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1.9f)) {
                            Text(
                                "Punya ide, tapi belum punya gear?",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Temukan perlengkapan yang kamu butuhkan untuk project berikutnya.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = onSearchClick,
                                shape = PillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) { Text("Telusuri", style = MaterialTheme.typography.labelLarge) }
                        }
                        Spacer(Modifier.width(16.dp))
                        Image(
                            painter = painterResource(R.drawable.cat_kamera),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .weight(1f)
                                .height(130.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, sheetShape)
                    .padding(top = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader("Kategori", onArrowClick = onSearchClick)
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(categories) { category ->
                            CategoryItem(
                                category = category,
                                onClick = { onCategoryClick(category) }
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader("Terdekat untukmu", onArrowClick = onSearchClick)
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(nearby, key = { it.id }) { gear ->
                            GearCard(
                                gear = gear,
                                onClick = { onGearClick(gear.id) },
                                modifier = Modifier.width(168.dp),
                                distanceLabel = gearDistanceLabel(
                                    gear = gear,
                                    selectedLocation = selectedLocation,
                                    activeLocation = activeLocation,
                                    isUsingGPS = isUsingGPS
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, onArrowClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onArrowClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Lihat semua")
        }
    }
}

@Composable
private fun CategoryItem(category: GearCategory, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            Modifier
                .size(width = 110.dp, height = 110.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(category.imageRes),
                contentDescription = category.label,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.width(64.dp)
            )
        }
        Text(category.label, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    GearHubTheme {
        HomeScreen(
            selectedLocation = DummyLocations.DEFAULT,
            onLocationClick = {},
            onGearClick = {},
            onSearchClick = {},
            onCategoryClick = {},
            onBottomNav = {}
        )
    }
}