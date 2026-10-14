package org.umn.gearhub.data.model

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
    val status: GearStatus,
    val imageRes: Int? = null
)