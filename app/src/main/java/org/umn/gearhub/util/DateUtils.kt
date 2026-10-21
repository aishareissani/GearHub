package org.umn.gearhub.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    const val DAY_MILLIS = 86_400_000L

    private val utc = TimeZone.getTimeZone("UTC")
    private val indonesia = Locale.forLanguageTag("id-ID")

    private val shortMonths = listOf(
        "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
        "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
    )

    fun todayUtcMillis(): Long {
        val local = Calendar.getInstance()
        val utcCalendar = Calendar.getInstance(utc)
        utcCalendar.clear()
        utcCalendar.set(
            local.get(Calendar.YEAR),
            local.get(Calendar.MONTH),
            local.get(Calendar.DAY_OF_MONTH)
        )
        return utcCalendar.timeInMillis
    }

    fun toLocalDate(utcMillis: Long): LocalDate =
        Instant.ofEpochMilli(utcMillis).atZone(ZoneOffset.UTC).toLocalDate()

    fun formatLong(millis: Long): String {
        val dayFormatter = SimpleDateFormat("EEEE, d", indonesia)
        dayFormatter.timeZone = utc
        val calendar = Calendar.getInstance(utc)
        calendar.timeInMillis = millis
        val month = shortMonths[calendar.get(Calendar.MONTH)]
        return "${dayFormatter.format(Date(millis))} $month"
    }
}