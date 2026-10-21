package org.umn.gearhub.data.model

import java.time.LocalDate

data class Gear(
    val id: Int,
    val name: String,
    val category: GearCategory,
    val pricePerDay: Int,
    val rating: Double,
    val reviewCount: Int,
    val distanceKm: Double,
    val ownerName: String,
    val location: String,
    val description: List<String>,
    val specs: List<String>,
    val included: List<String>,
    val availableFrom: LocalDate,
    val availableUntil: LocalDate,
    val images: List<Int> = emptyList()
) {
    val imageRes: Int? get() = images.firstOrNull()
}