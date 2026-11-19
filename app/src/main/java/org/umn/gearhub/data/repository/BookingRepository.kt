package org.umn.gearhub.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.jan.supabase.postgrest.from
import org.umn.gearhub.data.model.Booking
import org.umn.gearhub.data.remote.BookingRow
import org.umn.gearhub.data.remote.supabase
import java.time.LocalDate

data class ReturnLimit(
    val maxDate: LocalDate,
    val blockedBy: LocalDate?
)

object BookingRepository {
    private val today: LocalDate get() = LocalDate.now()

    fun daysFromToday(offset: Int): LocalDate = today.plusDays(offset.toLong())

    var list: List<Booking> by mutableStateOf(emptyList())
        private set

    suspend fun load() {
        list = supabase.from("bookings").select().decodeList<BookingRow>().map(BookingRow::toBooking)
    }

    fun isBooked(gearId: Int, date: LocalDate): Boolean =
        list.any {
            it.gearId == gearId && !date.isBefore(it.startDate) && !date.isAfter(it.endDate)
        }


    fun isWithinOwnerSchedule(gearId: Int, start: LocalDate, end: LocalDate): Boolean {
        val gear = GearRepository.getById(gearId) ?: return false
        return !start.isBefore(gear.availableFrom) && !end.isAfter(gear.availableUntil)
    }

    fun isDateAvailable(gearId: Int, date: LocalDate): Boolean =
        !date.isBefore(today) &&
                isWithinOwnerSchedule(gearId, date, date) &&
                !isBooked(gearId, date)

    fun isPickupSelectable(gearId: Int, date: LocalDate): Boolean {
        val gear = GearRepository.getById(gearId) ?: return false
        val next = date.plusDays(1)
        return isDateAvailable(gearId, date) &&
                !next.isAfter(gear.availableUntil) &&
                !isBooked(gearId, next)
    }

    fun returnLimit(gearId: Int, pickup: LocalDate): ReturnLimit? {
        val gear = GearRepository.getById(gearId) ?: return null
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