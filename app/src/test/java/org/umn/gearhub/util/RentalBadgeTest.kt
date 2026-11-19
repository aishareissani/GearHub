package org.umn.gearhub.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.umn.gearhub.data.model.RentalStatus
import java.time.LocalDate

class RentalBadgeTest {
    private val today = LocalDate.of(2026, 11, 8)

    @Test
    fun ongoingRentalShowsDaysLeft() {
        assertEquals(
            RentalBadge("Sisa 2 hari", BadgeTone.ACTIVE),
            rentalBadge(RentalStatus.BERLANGSUNG, today.plusDays(2), today)
        )
    }

    @Test
    fun ongoingRentalDueToday() {
        assertEquals(
            RentalBadge("Kembali hari ini", BadgeTone.ACTIVE),
            rentalBadge(RentalStatus.BERLANGSUNG, today, today)
        )
    }

    @Test
    fun overdueRentalIsNotReportedAsDueToday() {
        assertEquals(
            RentalBadge("Terlambat 3 hari", BadgeTone.OVERDUE),
            rentalBadge(RentalStatus.BERLANGSUNG, today.minusDays(3), today)
        )
    }

    @Test
    fun otherStatusesUseTheirLabel() {
        assertEquals(BadgeTone.WAITING, rentalBadge(RentalStatus.MENUNGGU, today, today).tone)
        assertEquals("Selesai", rentalBadge(RentalStatus.SELESAI, today, today).text)
    }
}
