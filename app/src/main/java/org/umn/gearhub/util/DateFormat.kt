package org.umn.gearhub.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val localeId: Locale = Locale.forLanguageTag("id-ID")
private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", localeId)
private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", localeId)

fun formatDate(date: LocalDate): String = date.format(dateFormatter)

fun formatDateRange(start: LocalDate, end: LocalDate): String =
    "${formatDate(start)} – ${formatDate(end)}"

fun formatMonthTitle(month: YearMonth): String = month.format(monthFormatter)