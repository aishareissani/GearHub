package org.umn.gearhub.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.umn.gearhub.data.dummy.DummyAreas
import org.umn.gearhub.data.dummy.DummyLocations
import org.umn.gearhub.data.model.GPSLocation
import org.umn.gearhub.data.model.Gear
import java.util.Locale

const val COVERAGE_KM = 50.0

fun getLocation(
    context: Context,
    onSuccess: (GPSLocation) -> Unit,
    onError: (String) -> Unit
) {
    val fineGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarseGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    if (!fineGranted && !coarseGranted) {
        onError("Izin lokasi belum diberikan")
        return
    }

    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    val priority = if (fineGranted) {
        Priority.PRIORITY_HIGH_ACCURACY
    } else {
        Priority.PRIORITY_BALANCED_POWER_ACCURACY
    }

    try {
        fusedClient.getCurrentLocation(priority, CancellationTokenSource().token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    onSuccess(
                        GPSLocation(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            accuracy = location.accuracy
                        )
                    )
                } else {
                    onError("Lokasi tidak tersedia. Periksa pengaturan GPS kamu.")
                }
            }
            .addOnFailureListener { error ->
                onError("Gagal mengambil lokasi: ${error.message}")
            }
    } catch (e: SecurityException) {
        onError("Akses lokasi ditolak")
    }
}

fun distanceKmBetween(from: GPSLocation, gear: Gear): Double {
    val target = if (gear.latitude != 0.0 || gear.longitude != 0.0) {
        Pair(gear.latitude, gear.longitude)
    } else {
        DummyAreas.coordinateOf(gear.location)
    } ?: return gear.distanceKm
    val result = FloatArray(1)
    Location.distanceBetween(
        from.latitude, from.longitude,
        target.first, target.second,
        result
    )
    return result[0] / 1000.0
}

fun gearDistanceKm(from: GPSLocation?, gear: Gear): Double =
    if (from == null) gear.distanceKm else distanceKmBetween(from, gear)

fun areaLocation(areaName: String): GPSLocation? =
    DummyAreas.coordinateOf(areaName)?.let {
        GPSLocation(latitude = it.first, longitude = it.second, accuracy = 0f)
    }

private fun nearestAreaEntry(from: GPSLocation): Pair<String, Double>? {
    val candidates = DummyLocations.list
        .filter { DummyAreas.coordinateOf(it) != null }
        .ifEmpty { DummyAreas.names }
    val result = FloatArray(1)
    return candidates
        .mapNotNull { name ->
            DummyAreas.coordinateOf(name)?.let { coordinate ->
                Location.distanceBetween(
                    from.latitude, from.longitude,
                    coordinate.first, coordinate.second,
                    result
                )
                name to (result[0] / 1000.0)
            }
        }
        .minByOrNull { it.second }
}

fun nearestAreaName(from: GPSLocation): String =
    nearestAreaEntry(from)?.first.orEmpty()

fun nearestAreaDistanceKm(from: GPSLocation): Double =
    nearestAreaEntry(from)?.second ?: Double.MAX_VALUE

fun isWithinCoverage(from: GPSLocation): Boolean =
    nearestAreaDistanceKm(from) <= COVERAGE_KM

fun isGpsActive(userLocation: GPSLocation?, isUsingGPS: Boolean): Boolean =
    isUsingGPS && userLocation != null && isWithinCoverage(userLocation)

fun activeLocationOf(
    selectedLocation: String,
    userLocation: GPSLocation?,
    isUsingGPS: Boolean
): GPSLocation? =
    if (userLocation != null && isGpsActive(userLocation, isUsingGPS)) {
        userLocation
    } else {
        areaLocation(selectedLocation)
    }

fun gearDistanceLabel(
    gear: Gear,
    selectedLocation: String,
    activeLocation: GPSLocation?,
    isUsingGPS: Boolean
): String {
    if (!isUsingGPS && gear.location == selectedLocation) return "Di area ini"
    return "%.1f km".format(
        Locale.forLanguageTag("id-ID"),
        gearDistanceKm(activeLocation, gear)
    )
}