package org.umn.gearhub.data.model

import java.time.LocalDate

enum class RentalStatus(val label: String) {
    BERLANGSUNG("Berlangsung"),
    MENUNGGU("Menunggu"),
    SELESAI("Selesai")
}

data class Rental(
    val id: Int,
    val gearId: Int,
    val pickupDate: LocalDate,
    val returnDate: LocalDate,
    val status: RentalStatus
)
