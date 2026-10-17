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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import org.umn.gearhub.data.dummy.DummyGears
import org.umn.gearhub.ui.components.AuthButton
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.DateUtils
import org.umn.gearhub.util.RentValidator
import org.umn.gearhub.util.formatRupiah

@Composable
fun RentScreen(
    onBack: () -> Unit,
    onContinue: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RentViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            delay(1000)
            viewModel.onLoadingFinished()
            state.gear?.let { onContinue(it.id) }
        }
    }

    RentContent(
        state = state,
        onBack = onBack,
        onPickupSelected = viewModel::onPickupSelected,
        onReturnSelected = viewModel::onReturnSelected,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
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
                        onClick = { showReturnPicker = true },
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
            AuthButton(text = "Lanjut", isLoading = state.isLoading, onClick = onSubmit)
        }
    }

    if (showPickupPicker) {
        DatePickerSheet(
            selectedMillis = state.pickupMillis,
            displayMillis = state.pickupMillis,
            isSelectable = { it >= state.todayMillis },
            onConfirm = {
                onPickupSelected(it)
                showPickupPicker = false
            },
            onDismiss = { showPickupPicker = false }
        )
    }

    if (showReturnPicker) {
        val minReturn = (state.pickupMillis ?: state.todayMillis) + DateUtils.DAY_MILLIS
        val maxReturn = state.pickupMillis?.let {
            it + RentValidator.MAX_DAYS * DateUtils.DAY_MILLIS
        }
        DatePickerSheet(
            selectedMillis = state.returnMillis,
            displayMillis = state.returnMillis ?: state.pickupMillis,
            isSelectable = { it >= minReturn && (maxReturn == null || it <= maxReturn) },
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
        DatePicker(state = pickerState)
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
                returnMillis = today + 4 * DateUtils.DAY_MILLIS
            ),
            onBack = {},
            onPickupSelected = {},
            onReturnSelected = {},
            onSubmit = {}
        )
    }
}