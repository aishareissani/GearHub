package org.umn.gearhub.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class RentalStatusTest {
    private val today = LocalDate.now()

    private fun rental(pickupOffset: Long, returnOffset: Long) =
        Rental(1, 1, today.plusDays(pickupOffset), today.plusDays(returnOffset))

    @Test
    fun futureRentalIsWaiting() = assertEquals(RentalStatus.MENUNGGU, rental(1, 3).status)

    @Test
    fun rentalStartingTodayIsOngoing() = assertEquals(RentalStatus.BERLANGSUNG, rental(0, 2).status)

    @Test
    fun rentalReturnedTodayIsStillOngoing() = assertEquals(RentalStatus.BERLANGSUNG, rental(-2, 0).status)

    @Test
    fun pastRentalIsDone() = assertEquals(RentalStatus.SELESAI, rental(-5, -1).status)
}
