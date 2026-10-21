package org.umn.gearhub.data.dummy

import org.umn.gearhub.data.model.Booking
import java.time.LocalDate

data class ReturnLimit(
    val maxDate: LocalDate,
    val blockedBy: LocalDate?
)

object DummyBookings {
    private val today: LocalDate = LocalDate.now()

    fun daysFromToday(offset: Int): LocalDate = today.plusDays(offset.toLong())

    val list = listOf(
        Booking(1, 1, daysFromToday(4), daysFromToday(8)),
        Booking(2, 1, daysFromToday(20), daysFromToday(23)),
        Booking(3, 2, daysFromToday(2), daysFromToday(5)),
        Booking(4, 2, daysFromToday(12), daysFromToday(16)),
        Booking(5, 3, daysFromToday(8), daysFromToday(10)),
        Booking(6, 4, daysFromToday(3), daysFromToday(6)),
        Booking(7, 5, daysFromToday(10), daysFromToday(14)),
        Booking(8, 6, daysFromToday(6), daysFromToday(9)),
        Booking(9, 6, daysFromToday(25), daysFromToday(28)),
        Booking(10, 7, daysFromToday(5), daysFromToday(8)),
        Booking(11, 9, daysFromToday(12), daysFromToday(13)),
        Booking(12, 10, daysFromToday(7), daysFromToday(11))
    )

    fun isBooked(gearId: Int, date: LocalDate): Boolean =
        list.any {
            it.gearId == gearId && !date.isBefore(it.startDate) && !date.isAfter(it.endDate)
        }


    fun isWithinOwnerSchedule(gearId: Int, start: LocalDate, end: LocalDate): Boolean {
        val gear = DummyGears.getById(gearId) ?: return false
        return !start.isBefore(gear.availableFrom) && !end.isAfter(gear.availableUntil)
    }

    fun isDateAvailable(gearId: Int, date: LocalDate): Boolean =
        !date.isBefore(today) &&
                isWithinOwnerSchedule(gearId, date, date) &&
                !isBooked(gearId, date)

    fun isPickupSelectable(gearId: Int, date: LocalDate): Boolean {
        val gear = DummyGears.getById(gearId) ?: return false
        val next = date.plusDays(1)
        return isDateAvailable(gearId, date) &&
                !next.isAfter(gear.availableUntil) &&
                !isBooked(gearId, next)
    }

    fun returnLimit(gearId: Int, pickup: LocalDate): ReturnLimit? {
        val gear = DummyGears.getById(gearId) ?: return null
        val nextStart = list
            .filter { it.gearId == gearId && it.startDate.isAfter(pickup) }
            .minOfOrNull { it.startDate }
        val beforeNext = nextStart?.minusDays(1)
        return if (beforeNext != null && beforeNext.isBefore(gear.availableUntil)) {
            ReturnLimit(maxDate = beforeNext, blockedBy = nextStart)
        } else {
            ReturnLimit(maxDate = gear.availableUntil, blockedBy = null)
        }
    }

    fun isReturnSelectable(gearId: Int, pickup: LocalDate, date: LocalDate): Boolean {
        val limit = returnLimit(gearId, pickup) ?: return false
        return date.isAfter(pickup) && !date.isAfter(limit.maxDate)
    }
}