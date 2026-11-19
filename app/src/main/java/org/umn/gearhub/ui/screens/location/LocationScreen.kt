package org.umn.gearhub.ui.screens.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import org.umn.gearhub.data.dummy.DummyLocations
import org.umn.gearhub.ui.components.AuthButton
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.isWithinCoverage
import org.umn.gearhub.util.nearestAreaDistanceKm
import org.umn.gearhub.util.nearestAreaName
import java.util.Locale

@Composable
fun LocationScreen(
    selectedLocation: String,
    locationState: LocationUiState,
    onDetect: (granted: Boolean) -> Unit,
    onBack: () -> Unit,
    onConfirm: (location: String, useGps: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var pendingLocation by rememberSaveable { mutableStateOf(selectedLocation) }
    var pendingUseGps by rememberSaveable { mutableStateOf(locationState.isUsingGPS) }
    var waitingResult by rememberSaveable { mutableStateOf(false) }
    var outOfCoverage by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        onDetect(result.values.any { it })
    }

    LaunchedEffect(locationState.fixCount) {
        if (waitingResult && !locationState.isLoading) {
            val gps = locationState.userLocation
            if (locationState.error == null && gps != null) {
                if (isWithinCoverage(gps)) {
                    val area = nearestAreaName(gps)
                    if (area.isNotEmpty()) {
                        pendingLocation = area
                        pendingUseGps = true
                        outOfCoverage = false
                    }
                } else {
                    pendingLocation = DummyLocations.DEFAULT
                    pendingUseGps = false
                    outOfCoverage = true
                }
            }
            waitingResult = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali"
                )
            }

            Text(
                "Pilih lokasi",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        HorizontalDivider(
            Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 2.dp
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MapPlaceholder(label = pendingLocation)

            if (pendingUseGps) {
                locationState.userLocation?.let { gps ->
                    Text(
                        "Koordinat: ${"%.5f".format(Locale.US, gps.latitude)}, " +
                                "${"%.5f".format(Locale.US, gps.longitude)} " +
                                "(akurasi ±${gps.accuracy.toInt()} m)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (outOfCoverage) {
                locationState.userLocation?.let { gps ->
                    Text(
                        "Posisi GPS kamu di luar jangkauan GearHub " +
                                "(±${"%.0f".format(Locale.US, nearestAreaDistanceKm(gps))} km " +
                                "dari area terdekat). Lokasi dikembalikan ke ${DummyLocations.DEFAULT}.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            locationState.error?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedButton(
                onClick = {
                    if (!locationState.isLoading) {
                        waitingResult = true
                        val fineGranted = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                        val coarseGranted = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                        if (fineGranted || coarseGranted) {
                            onDetect(true)
                        } else {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    }
                },
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (locationState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.MyLocation,
                        null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Gunakan lokasi saya",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Column {
                Text(
                    "Lokasi tersedia",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))

                DummyLocations.list.forEach { location ->
                    LocationRow(
                        name = location,
                        selected = location == pendingLocation,
                        onClick = {
                            pendingLocation = location
                            pendingUseGps = false
                            outOfCoverage = false
                        }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 16.dp)
                .navigationBarsPadding()
        ) {
            AuthButton(
                text = "Konfirmasi lokasi",
                isLoading = false,
                onClick = {
                    if (!locationState.isLoading) {
                        onConfirm(pendingLocation, pendingUseGps)
                    }
                }
            )
        }
    }
}

@Composable
private fun MapPlaceholder(label: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-40).dp)
                .size(180.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
        )

        Box(
            Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-50).dp, y = 50.dp)
                .size(160.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f))
        )

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Default.LocationOn,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )

            Text(label, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun LocationRow(
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.LocationOn,
            null,
            tint = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(12.dp))

        Text(
            name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        if (selected) {
            Icon(
                Icons.Default.Check,
                "Dipilih",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun LocationScreenPreview() {
    GearHubTheme {
        LocationScreen(
            selectedLocation = DummyLocations.DEFAULT,
            locationState = LocationUiState(),
            onDetect = {},
            onBack = {},
            onConfirm = { _, _ -> }
        )
    }
}