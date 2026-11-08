package org.umn.gearhub.util

import org.umn.gearhub.data.model.RentalStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class BadgeTone { ACTIVE, WAITING, DONE, OVERDUE }

data class RentalBadge(val text: String, val tone: BadgeTone)

fun rentalBadge(status: RentalStatus, returnDate: LocalDate, today: LocalDate): RentalBadge =
    when (status) {
        RentalStatus.BERLANGSUNG -> {
            val left = ChronoUnit.DAYS.between(today, returnDate)
            when {
                left > 0 -> RentalBadge("Sisa $left hari", BadgeTone.ACTIVE)
                left == 0L -> RentalBadge("Kembali hari ini", BadgeTone.ACTIVE)
                else -> RentalBadge("Terlambat ${-left} hari", BadgeTone.OVERDUE)
            }
        }
        RentalStatus.MENUNGGU -> RentalBadge(status.label, BadgeTone.WAITING)
        RentalStatus.SELESAI -> RentalBadge(status.label, BadgeTone.DONE)
    }
