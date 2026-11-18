package org.umn.gearhub.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.dummy.DummyLocations
import org.umn.gearhub.data.model.GearCategory
import org.umn.gearhub.navigation.Routes
import org.umn.gearhub.ui.components.BottomNavBar
import org.umn.gearhub.ui.components.CategoryChip
import org.umn.gearhub.ui.components.GearCard
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.ui.theme.PillShape

@Composable
fun SearchScreen(
    selectedLocation: String,
    onLocationClick: () -> Unit,
    onGearClick: (Int) -> Unit,
    onBottomNav: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialCategory: String? = null
) {
    val focusManager = LocalFocusManager.current
    var query by rememberSaveable { mutableStateOf("") }
    var selectedCategoryName by rememberSaveable(initialCategory) { mutableStateOf(initialCategory) }

    val selectedCategory = GearCategory.entries.firstOrNull { it.name == selectedCategoryName }

    val results = remember(query, selectedCategoryName, selectedLocation) {
        GearRepository.list
            .filter { gear ->
                (selectedCategory == null || gear.category == selectedCategory) &&
                        gear.name.contains(query.trim(), ignoreCase = true)
            }
            .sortedWith(
                compareByDescending<org.umn.gearhub.data.model.Gear> { it.location == selectedLocation }
                    .thenBy { it.distanceKm }
            )
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = { BottomNavBar(currentRoute = Routes.SEARCH, onNavigate = onBottomNav) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .statusBarsPadding()
        ) {
            Column(
                Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        "Pencarian",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.clickable(onClick = onLocationClick),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedLocation, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Ganti lokasi")
                    }
                }

                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                                if (query.isEmpty()) {
                                    Text(
                                        "Cari gear…",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                innerTextField()
                            }
                            if (query.isNotEmpty()) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Hapus",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { query = "" }
                                )
                            }
                        }
                    }
                )
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    CategoryChip(
                        label = "Semua",
                        selected = selectedCategory == null,
                        onClick = { selectedCategoryName = null }
                    )
                }
                items(GearCategory.entries) { category ->
                    CategoryChip(
                        label = category.label,
                        selected = selectedCategory == category,
                        onClick = { selectedCategoryName = category.name }
                    )
                }
            }

            if (results.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Gear tidak ditemukan", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Coba kata kunci atau kategori lain",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(results, key = { it.id }) { gear ->
                        GearCard(
                            gear = gear,
                            onClick = { onGearClick(gear.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun SearchScreenPreview() {
    GearHubTheme {
        SearchScreen(
            selectedLocation = DummyLocations.DEFAULT,
            onLocationClick = {},
            onGearClick = {},
            onBottomNav = {}
        )
    }
}