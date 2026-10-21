package org.umn.gearhub.ui.screens.rent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import org.umn.gearhub.data.dummy.DummyBookings
import org.umn.gearhub.data.dummy.DummyGears
import org.umn.gearhub.ui.components.AuthButton
import org.umn.gearhub.ui.components.AuthTextField
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.DateUtils
import org.umn.gearhub.util.RentValidator
import org.umn.gearhub.util.formatDateRange
import org.umn.gearhub.util.formatRupiah
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

private val infoDateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("id-ID"))

private fun LocalDate.formatInfo(): String = format(infoDateFormatter)

@Composable
fun RentScreen(
    onBack: () -> Unit,
    onContinue: (Int, Long, Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RentViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            delay(1000.milliseconds)
            val gear = state.gear
            val pickup = state.pickupMillis
            val returnDate = state.returnMillis
            viewModel.onLoadingFinished()
            if (gear != null && pickup != null && returnDate != null) {
                onContinue(gear.id, pickup, returnDate)
            }
        }
    }

    RentContent(
        state = state,
        onBack = onBack,
        onPickupSelected = viewModel::onPickupSelected,
        onReturnSelected = viewModel::onReturnSelected,
        onPhoneChange = viewModel::onPhoneChange,
        onSubmit = viewModel::onSubmit,
        modifier = modifier
    )
}

@Composable
fun RentContent(
    state: RentUiState,
    onBack: () -> Unit,
    onPickupSelected: (Long) -> Unit,
    onReturnSelected: (Long) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gear = state.gear

    if (gear == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Gear tidak ditemukan", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onBack) { Text("Kembali") }
            }
        }
        return
    }

    var showPickupPicker by rememberSaveable { mutableStateOf(false) }
    var showReturnPicker by rememberSaveable { mutableStateOf(false) }

    val pickupError = if (state.pickupTouched || state.submitted) state.pickupError else null
    val returnError = if (state.returnTouched || state.submitted) state.returnError else null
    val phoneError = if (state.phoneTouched || state.submitted) state.phoneError else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .imePadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                "Ajukan sewa",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        HorizontalDivider(
            Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 2.dp
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                    gear.images.firstOrNull()?.let { res ->
                        Image(
                            painter = painterResource(res),
                            contentDescription = gear.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(gear.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${formatRupiah(gear.pricePerDay)}/hari",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "${gear.location} (±${gear.distanceKm.toString().replace('.', ',')} km)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Pilih tanggal", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Tersedia disewa: ${formatDateRange(gear.availableFrom, gear.availableUntil)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    DateField(
                        label = "Pickup",
                        valueMillis = state.pickupMillis,
                        error = pickupError,
                        onClick = { showPickupPicker = true },
                        modifier = Modifier.weight(1f)
                    )
                    DateField(
                        label = "Return",
                        valueMillis = state.returnMillis,
                        error = returnError,
                        onClick = { if (state.pickupMillis != null) showReturnPicker = true },
                        modifier = Modifier.weight(1f)
                    )
                }
                state.durationDays?.let { days ->
                    Text(
                        "Durasi sewa: $days hari",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Kontak", style = MaterialTheme.typography.titleLarge)
                AuthTextField(
                    label = "Nomor telepon",
                    value = state.phone,
                    onValueChange = onPhoneChange,
                    placeholder = "08xxxxxxxxxx",
                    errorMessage = phoneError,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    onDone = onSubmit
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Ringkasan pembayaran", style = MaterialTheme.typography.titleLarge)
                SummaryRow("Harga", formatRupiah(gear.pricePerDay))
                SummaryRow("Durasi", state.durationDays?.let { "$it hari" } ?: "-")
                SummaryRow("Biaya layanan", formatRupiah(RentUiState.SERVICE_FEE))
                SummaryRow("Deposit jaminan", formatRupiah(RentUiState.DEPOSIT))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Outlined.Info, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Deposit akan dikembalikan ke Saldo GearHub maksimal 1x24 jam setelah checkout, jika kondisi gear sesuai saat pengambilan.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant,
                thickness = 2.dp
            )
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Total",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    state.totalPrice?.let { formatRupiah(it) } ?: "-",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            AuthButton(
                text = "Lanjut",
                isLoading = state.isLoading,
                useMutedColor = state.phone.isEmpty(),
                onClick = onSubmit
            )
        }
    }

    if (showPickupPicker) {
        DatePickerSheet(
            title = "Pilih tanggal pickup",
            info = "Tanggal abu-abu sudah disewa, di luar jadwal, atau tidak bisa dipakai minimal 1 hari.",
            selectedMillis = state.pickupMillis,
            displayMillis = state.pickupMillis,
            isSelectable = { millis ->
                millis >= state.todayMillis &&
                        DummyBookings.isPickupSelectable(gear.id, DateUtils.toLocalDate(millis))
            },
            onConfirm = {
                onPickupSelected(it)
                showPickupPicker = false
            },
            onDismiss = { showPickupPicker = false }
        )
    }

    val pickupMillis = state.pickupMillis
    if (showReturnPicker && pickupMillis != null) {
        val pickupDate = DateUtils.toLocalDate(pickupMillis)
        val limit = DummyBookings.returnLimit(gear.id, pickupDate)
        val info = when {
            limit == null -> ""
            limit.blockedBy != null ->
                "Return maksimal ${limit.maxDate.formatInfo()}. Tanggal ${limit.blockedBy.formatInfo()} sudah disewa orang lain."
            else ->
                "Return maksimal ${limit.maxDate.formatInfo()} (batas tanggal tersedia)."
        }
        DatePickerSheet(
            title = "Pilih tanggal return",
            info = info,
            selectedMillis = state.returnMillis,
            displayMillis = state.returnMillis ?: pickupMillis,
            isSelectable = { millis ->
                millis - pickupMillis <= RentValidator.MAX_DAYS * DateUtils.DAY_MILLIS &&
                        DummyBookings.isReturnSelectable(
                            gear.id,
                            pickupDate,
                            DateUtils.toLocalDate(millis)
                        )
            },
            onConfirm = {
                onReturnSelected(it)
                showReturnPicker = false
            },
            onDismiss = { showReturnPicker = false }
        )
    }
}

@Composable
private fun DateField(
    label: String,
    valueMillis: Long?,
    error: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isError = error != null
    val lineColor = if (isError) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.CalendarMonth, null,
                tint = if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    valueMillis?.let { DateUtils.formatLong(it) } ?: "Pilih tanggal",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (valueMillis == null) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        HorizontalDivider(color = lineColor, thickness = if (isError) 2.dp else 1.dp)
        if (isError) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerSheet(
    title: String,
    info: String,
    selectedMillis: Long?,
    displayMillis: Long?,
    isSelectable: (Long) -> Boolean,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedMillis,
        initialDisplayedMonthMillis = displayMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                isSelectable(utcTimeMillis)
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { pickerState.selectedDateMillis?.let(onConfirm) },
                enabled = pickerState.selectedDateMillis != null
            ) { Text("Pilih") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    ) {
        DatePicker(
            state = pickerState,
            title = {
                Text(title, modifier = Modifier.padding(start = 24.dp, top = 16.dp))
            },
            headline = {
                Column(
                    Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        pickerState.selectedDateMillis?.let { DateUtils.formatLong(it) } ?: "Belum dipilih",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        info,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun RentContentPreview() {
    val today = DateUtils.todayUtcMillis()
    GearHubTheme {
        RentContent(
            state = RentUiState(
                gear = DummyGears.list.first(),
                todayMillis = today,
                pickupMillis = today + DateUtils.DAY_MILLIS,
                returnMillis = today + 4 * DateUtils.DAY_MILLIS,
                phone = "081234567890"
            ),
            onBack = {},
            onPickupSelected = {},
            onReturnSelected = {},
            onPhoneChange = {},
            onSubmit = {}
        )
    }
}