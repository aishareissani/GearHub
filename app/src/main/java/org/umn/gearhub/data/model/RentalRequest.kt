package org.umn.gearhub.data.model

data class RentalRequest(
    val gearId: Int,
    val startDate: String,      // "Rabu, 8 Oktober" (atau LocalDate)
    val endDate: String,
    val durationDays: Int,
    val rentalPrice: Int,       // pricePerDay * durationDays
    val serviceFee: Int = 5_000,
    val securityDeposit: Int = 100_000
) {
    val totalPrice: Int get() = rentalPrice + serviceFee + securityDeposit
}