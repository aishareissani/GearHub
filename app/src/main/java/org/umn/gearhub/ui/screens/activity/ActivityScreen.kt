package org.umn.gearhub.ui.screens.activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.repository.RentalRepository
import org.umn.gearhub.data.model.Rental
import org.umn.gearhub.data.model.RentalStatus
import org.umn.gearhub.navigation.Routes
import org.umn.gearhub.ui.components.BottomNavBar
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.formatDateRange
import org.umn.gearhub.util.formatRupiah
import org.umn.gearhub.util.BadgeTone
import org.umn.gearhub.util.rentalBadge
import java.time.LocalDate

@Composable
fun ActivityScreen(
    onRentalClick: (gearId: Int) -> Unit,
    onBottomNav: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = RentalStatus.entries
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val rentals = RentalRepository.byStatus(statuses[selectedTab])

    Scaffold(
        modifier = modifier,
        bottomBar = { BottomNavBar(currentRoute = Routes.ACTIVITY, onNavigate = onBottomNav) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            Text(
                "Aktivitas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp)
            )
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                statuses.forEachIndexed { index, status ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                "${status.label} (${RentalRepository.byStatus(status).size})",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    )
                }
            }
            if (rentals.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (RentalRepository.loadFailed) "Gagal memuat riwayat sewa" else emptyMessage(statuses[selectedTab]),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(rentals, key = { it.id }) { rental ->
                        RentalCard(rental = rental, onClick = { onRentalClick(rental.gearId) })
                    }
                }
            }
        }
    }
}

private fun emptyMessage(status: RentalStatus) = when (status) {
    RentalStatus.BERLANGSUNG -> "Belum ada sewa yang berlangsung"
    RentalStatus.MENUNGGU -> "Tidak ada sewa yang menunggu"
    RentalStatus.SELESAI -> "Belum ada sewa yang selesai"
}

@Composable
private fun RentalCard(rental: Rental, onClick: () -> Unit) {
    val gear = GearRepository.getById(rental.gearId) ?: return
    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(72.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                gear.imageRes?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = gear.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    gear.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    formatDateRange(rental.pickupDate, rental.returnDate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        formatRupiah(RentalRepository.total(rental)),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.weight(1f)
                    )
                    StatusBadge(rental)
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(rental: Rental) {
    val badge = rentalBadge(rental.status, rental.returnDate, LocalDate.now())
    val colors = MaterialTheme.colorScheme
    val (container, content) = when (badge.tone) {
        BadgeTone.ACTIVE -> colors.primaryContainer to colors.onPrimaryContainer
        BadgeTone.WAITING -> colors.secondaryContainer to colors.onSecondaryContainer
        BadgeTone.DONE -> colors.surfaceVariant to colors.onSurfaceVariant
        BadgeTone.OVERDUE -> colors.errorContainer to colors.onErrorContainer
    }
    Surface(color = container, contentColor = content, shape = MaterialTheme.shapes.large) {
        Text(
            badge.text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun ActivityScreenPreview() {
    GearHubTheme { ActivityScreen(onRentalClick = {}, onBottomNav = {}) }
}
