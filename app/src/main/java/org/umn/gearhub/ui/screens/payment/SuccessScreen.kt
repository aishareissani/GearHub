package org.umn.gearhub.ui.screens.payment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.dummy.DummyPayments
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.DateUtils
import org.umn.gearhub.util.PriceCalculator
import org.umn.gearhub.util.formatRupiah

@Composable
fun SuccessScreen(
    gearId: Int,
    pickupMillis: Long,
    returnMillis: Long,
    methodId: Int,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onHome)

    val gear = GearRepository.getById(gearId)
    val method = DummyPayments.getById(methodId)
    val total = gear?.let { PriceCalculator.total(it.pricePerDay, pickupMillis, returnMillis) } ?: 0
    val days = PriceCalculator.durationDays(pickupMillis, returnMillis)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Pembayaran berhasil", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text(
                "Pengajuan sewa kamu sudah kami terima.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))

            if (gear != null) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(gear.name, style = MaterialTheme.typography.titleMedium)
                        InfoRow("Pickup", DateUtils.formatLong(pickupMillis))
                        InfoRow("Return", DateUtils.formatLong(returnMillis))
                        InfoRow("Durasi", "$days hari")
                        InfoRow("Metode", method?.name ?: "-")
                        InfoRow("Total", formatRupiah(total))
                    }
                }
            }
        }

        Button(
            onClick = onHome,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .navigationBarsPadding()
                .height(52.dp)
        ) {
            Text("Kembali ke beranda", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(value, style = MaterialTheme.typography.titleSmall)
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun SuccessScreenPreview() {
    val today = DateUtils.todayUtcMillis()
    GearHubTheme {
        SuccessScreen(
            gearId = 1,
            pickupMillis = today + DateUtils.DAY_MILLIS,
            returnMillis = today + 4 * DateUtils.DAY_MILLIS,
            methodId = 2,
            onHome = {}
        )
    }
}