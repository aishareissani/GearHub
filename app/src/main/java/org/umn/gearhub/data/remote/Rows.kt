package org.umn.gearhub.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.umn.gearhub.R
import org.umn.gearhub.data.model.Booking
import org.umn.gearhub.data.model.Gear
import org.umn.gearhub.data.model.GearCategory
import org.umn.gearhub.data.model.Rental
import java.time.LocalDate

@Serializable
data class GearRow(
    val id: Int? = null,
    val name: String,
    val category: String,
    @SerialName("price_per_day") val pricePerDay: Int,
    val rating: Double,
    @SerialName("review_count") val reviewCount: Int,
    @SerialName("distance_km") val distanceKm: Double,
    @SerialName("owner_name") val ownerName: String,
    val location: String,
    val description: List<String>,
    val specs: List<String>,
    val included: List<String>,
    @SerialName("available_from") val availableFrom: String,
    @SerialName("available_until") val availableUntil: String,
    val images: List<String>
) {
    fun toGear() = Gear(
        id = requireNotNull(id) { "gears row without id" },
        name = name,
        category = GearCategory.valueOf(category),
        pricePerDay = pricePerDay,
        rating = rating,
        reviewCount = reviewCount,
        distanceKm = distanceKm,
        ownerName = ownerName,
        location = location,
        description = description,
        specs = specs,
        included = included,
        availableFrom = LocalDate.parse(availableFrom),
        availableUntil = LocalDate.parse(availableUntil),
        images = images.mapNotNull(::drawableId)
    )

    companion object {
        fun from(gear: Gear) = GearRow(
            name = gear.name,
            category = gear.category.name,
            pricePerDay = gear.pricePerDay,
            rating = gear.rating,
            reviewCount = gear.reviewCount,
            distanceKm = gear.distanceKm,
            ownerName = gear.ownerName,
            location = gear.location,
            description = gear.description,
            specs = gear.specs,
            included = gear.included,
            availableFrom = gear.availableFrom.toString(),
            availableUntil = gear.availableUntil.toString(),
            images = emptyList()
        )
    }
}

// Seeded gear photos ship as drawables, so the database stores their resource names.
private fun drawableId(name: String): Int? =
    runCatching { R.drawable::class.java.getField(name).getInt(null) }.getOrNull()

@Serializable
data class BookingRow(
    val id: Int,
    @SerialName("gear_id") val gearId: Int,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String
) {
    fun toBooking() = Booking(id, gearId, LocalDate.parse(startDate), LocalDate.parse(endDate))
}

@Serializable
data class RentalRow(
    val id: Int? = null,
    @SerialName("gear_id") val gearId: Int,
    @SerialName("pickup_date") val pickupDate: String,
    @SerialName("return_date") val returnDate: String,
    @SerialName("payment_method_id") val paymentMethodId: Int
) {
    fun toRental() = Rental(
        id = requireNotNull(id) { "rentals row without id" },
        gearId = gearId,
        pickupDate = LocalDate.parse(pickupDate),
        returnDate = LocalDate.parse(returnDate)
    )
}
