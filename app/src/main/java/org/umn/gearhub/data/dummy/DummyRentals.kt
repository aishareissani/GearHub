package org.umn.gearhub.data.dummy

import org.umn.gearhub.data.model.Rental
import org.umn.gearhub.data.model.RentalStatus
import org.umn.gearhub.util.PriceCalculator
import java.time.temporal.ChronoUnit

object DummyRentals {
    private fun day(offset: Int) = DummyBookings.daysFromToday(offset)

    val list = listOf(
        Rental(1, 2, day(-1), day(2), RentalStatus.BERLANGSUNG),
        Rental(2, 7, day(-3), day(1), RentalStatus.BERLANGSUNG),
        Rental(3, 1, day(4), day(6), RentalStatus.MENUNGGU),
        Rental(4, 9, day(9), day(10), RentalStatus.MENUNGGU),
        Rental(5, 4, day(-20), day(-17), RentalStatus.SELESAI),
        Rental(6, 5, day(-35), day(-33), RentalStatus.SELESAI),
        Rental(7, 10, day(-60), day(-56), RentalStatus.SELESAI)
    )

    fun byStatus(status: RentalStatus): List<Rental> = list.filter { it.status == status }

    fun total(rental: Rental): Int {
        val price = DummyGears.getById(rental.gearId)?.pricePerDay ?: 0
        val days = ChronoUnit.DAYS.between(rental.pickupDate, rental.returnDate).toInt()
        return price * days + PriceCalculator.SERVICE_FEE + PriceCalculator.DEPOSIT
    }
}
