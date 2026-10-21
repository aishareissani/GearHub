package org.umn.gearhub.data.model

import java.time.LocalDate

data class Booking(
    val id: Int,
    val gearId: Int,
    val startDate: LocalDate,
    val endDate: LocalDate
)