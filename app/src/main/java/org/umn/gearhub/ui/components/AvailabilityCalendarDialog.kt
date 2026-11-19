package org.umn.gearhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.umn.gearhub.data.repository.BookingRepository
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.formatMonthTitle
import java.time.YearMonth

private val weekdayLabels = listOf("SUN", "MO", "TUE", "WED", "THU", "FRI", "SAT")

@Composable
fun AvailabilityCalendarDialog(
    gearId: Int,
    onDismiss: () -> Unit
) {
    val gear = remember(gearId) { GearRepository.getById(gearId) }
    if (gear == null) {
        return
    }

    val firstMonth = remember(gear) {
        val today = BookingRepository.daysFromToday(0)
        YearMonth.from(if (gear.availableFrom.isAfter(today)) gear.availableFrom else today)
    }
    val lastMonth = remember(gear) {
        val last = YearMonth.from(gear.availableUntil)
        if (last < firstMonth) firstMonth else last
    }
    var visibleMonth by remember(firstMonth) { mutableStateOf(firstMonth) }

    val weeks = remember(visibleMonth) {
        buildList {
            repeat(visibleMonth.atDay(1).dayOfWeek.value % 7) { add(null) }
            for (day in 1..visibleMonth.lengthOfMonth()) add(visibleMonth.atDay(day))
            while (size < 42) add(null)
        }.chunked(7)
    }

    val canGoPrevious = visibleMonth > firstMonth
    val canGoNext = visibleMonth < lastMonth

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Cek ketersediaan",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { visibleMonth = visibleMonth.minusMonths(1) },
                        enabled = canGoPrevious
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Bulan sebelumnya")
                    }
                    Text(
                        formatMonthTitle(visibleMonth),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { visibleMonth = visibleMonth.plusMonths(1) },
                        enabled = canGoNext
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Bulan berikutnya")
                    }
                }

                Row(Modifier.fillMaxWidth()) {
                    weekdayLabels.forEach { label ->
                        Text(
                            label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Column {
                    weeks.forEach { week ->
                        Row(Modifier.fillMaxWidth()) {
                            week.forEach { date ->
                                if (date == null) {
                                    Spacer(
                                        Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                    )
                                } else {
                                    DayCell(
                                        day = date.dayOfMonth,
                                        isAvailable = BookingRepository.isDateAvailable(gearId, date),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    LegendItem(MaterialTheme.colorScheme.tertiary, "Tersedia")
                    LegendItem(MaterialTheme.colorScheme.error, "Tidak tersedia")
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    val container = if (isAvailable) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
    val content = if (isAvailable) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onError

    Box(
        modifier = modifier.height(44.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(container, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                day.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = content
            )
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun AvailabilityCalendarDialogPreview() {
    GearHubTheme {
        AvailabilityCalendarDialog(gearId = 1, onDismiss = {})
    }
}