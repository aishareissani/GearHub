package org.umn.gearhub.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import org.umn.gearhub.data.model.Rental
import org.umn.gearhub.data.model.RentalStatus
import org.umn.gearhub.data.remote.RentalRow
import org.umn.gearhub.data.remote.supabase
import org.umn.gearhub.util.PriceCalculator
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object RentalRepository {
    var list: List<Rental> by mutableStateOf(emptyList())
        private set

    fun byStatus(status: RentalStatus): List<Rental> = list.filter { it.status == status }

    fun total(rental: Rental): Int {
        val price = GearRepository.getById(rental.gearId)?.pricePerDay ?: 0
        val days = ChronoUnit.DAYS.between(rental.pickupDate, rental.returnDate).toInt()
        return price * days + PriceCalculator.SERVICE_FEE + PriceCalculator.DEPOSIT
    }

    /** Row-level security returns only the signed-in user's rentals. */
    suspend fun load() {
        list = supabase.from("rentals")
            .select { order("pickup_date", Order.DESCENDING) }
            .decodeList<RentalRow>()
            .map(RentalRow::toRental)
    }

    suspend fun create(gearId: Int, pickup: LocalDate, returnDate: LocalDate, methodId: Int) {
        val row = RentalRow(
            gearId = gearId,
            pickupDate = pickup.toString(),
            returnDate = returnDate.toString(),
            paymentMethodId = methodId
        )
        val saved = supabase.from("rentals").insert(row) { select() }.decodeSingle<RentalRow>()
        list = listOf(saved.toRental()) + list
        BookingRepository.load()
    }

    fun clear() {
        list = emptyList()
    }
}
