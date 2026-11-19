package org.umn.gearhub.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.dummy.DummyPayments
import org.umn.gearhub.data.model.PaymentMethod
import org.umn.gearhub.ui.components.AuthButton
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.DateUtils
import org.umn.gearhub.util.formatRupiah

@Composable
fun PaymentScreen(
    onBack: () -> Unit,
    onPaid: (gearId: Int, pickup: Long, returnDate: Long, methodId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isPaid) {
        val gear = state.gear
        if (state.isPaid && gear != null) {
            onPaid(gear.id, state.pickupMillis, state.returnMillis, state.selectedMethodId)
        }
    }

    PaymentContent(
        state = state,
        onBack = onBack,
        onMethodSelected = viewModel::onMethodSelected,
        onPay = viewModel::onPay,
        modifier = modifier
    )
}

@Composable
fun PaymentContent(
    state: PaymentUiState,
    onBack: () -> Unit,
    onMethodSelected: (Int) -> Unit,
    onPay: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.gear == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Data pembayaran tidak ditemukan", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onBack) { Text("Kembali") }
            }
        }
        return
    }

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
            IconButton(onClick = onBack, enabled = !state.isLoading) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                "Pembayaran",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Metode", style = MaterialTheme.typography.titleLarge)
            DummyPayments.list.forEach { method ->
                MethodCard(
                    method = method,
                    selected = state.selectedMethodId == method.id,
                    enabled = state.isMethodEnabled(method.id),
                    onClick = { onMethodSelected(method.id) }
                )
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
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(formatRupiah(state.totalPrice), style = MaterialTheme.typography.titleLarge)
            }
            state.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            AuthButton(text = "Bayar", isLoading = state.isLoading, onClick = onPay)
        }
    }
}

@Composable
private fun MethodCard(
    method: PaymentMethod,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val container = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.surfaceVariant

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        color = container,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(method.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    if (enabled) method.description else "${method.description} - Saldo tidak cukup",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            RadioButton(
                selected = selected,
                onClick = null,
                enabled = enabled,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun PaymentContentPreview() {
    val today = DateUtils.todayUtcMillis()
    GearHubTheme {
        PaymentContent(
            state = PaymentUiState(
                gear = GearRepository.list.first(),
                pickupMillis = today + DateUtils.DAY_MILLIS,
                returnMillis = today + 4 * DateUtils.DAY_MILLIS
            ),
            onBack = {},
            onMethodSelected = {},
            onPay = {}
        )
    }
}