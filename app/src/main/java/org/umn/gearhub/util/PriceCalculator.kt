package org.umn.gearhub.util

object PriceCalculator {
    const val SERVICE_FEE = 5_000
    const val DEPOSIT = 100_000

    fun durationDays(pickupMillis: Long, returnMillis: Long): Int =
        ((returnMillis - pickupMillis) / DateUtils.DAY_MILLIS).toInt()

    fun total(pricePerDay: Int, pickupMillis: Long, returnMillis: Long): Int =
        pricePerDay * durationDays(pickupMillis, returnMillis) + SERVICE_FEE + DEPOSIT
}