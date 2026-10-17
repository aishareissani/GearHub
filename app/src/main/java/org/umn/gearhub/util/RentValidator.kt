package org.umn.gearhub.util

object RentValidator {
    const val MAX_DAYS = 14

    fun validatePickup(pickup: Long?, today: Long): String? = when {
        pickup == null -> "Tanggal pickup wajib dipilih"
        pickup < today -> "Tanggal pickup tidak boleh sebelum hari ini"
        else -> null
    }

    fun validateReturn(pickup: Long?, returnDate: Long?): String? {
        if (returnDate == null) return "Tanggal return wajib dipilih"
        if (pickup == null) return null
        val days = (returnDate - pickup) / DateUtils.DAY_MILLIS
        return when {
            days < 1 -> "Tanggal return harus setelah tanggal pickup"
            days > MAX_DAYS -> "Durasi sewa maksimal $MAX_DAYS hari"
            else -> null
        }
    }
}