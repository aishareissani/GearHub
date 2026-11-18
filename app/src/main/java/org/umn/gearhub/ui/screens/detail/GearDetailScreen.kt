package org.umn.gearhub.ui.screens.detail

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.model.Gear
import org.umn.gearhub.ui.components.AvailabilityCalendarDialog
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.ui.theme.PillShape
import org.umn.gearhub.util.formatRupiah
import org.umn.gearhub.util.initialsOf

private val tabs = listOf("Ringkasan", "Spesifikasi", "Kelengkapan")

@Composable
fun GearDetailScreen(
    gearId: Int,
    onBack: () -> Unit,
    onRentClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gear = remember(gearId) { GearRepository.getById(gearId) }

    if (gear == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Gear tidak ditemukan", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onBack) { Text("Kembali") }
            }
        }
        return
    }

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showAvailability by remember { mutableStateOf(false) }

    val priceStyle = MaterialTheme.typography.headlineLarge.toSpanStyle()
    val perDayStyle = MaterialTheme.typography.bodyLarge.toSpanStyle()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Button(
                    onClick = { onRentClick(gear.id) },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Ajukan sewa", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            ImageHeader(gear = gear, onBack = onBack)

            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(gear.name, style = MaterialTheme.typography.titleLarge)

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(priceStyle) {
                                append(formatRupiah(gear.pricePerDay))
                            }
                            withStyle(perDayStyle) {
                                append("/hari")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.Default.Star, null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("${gear.rating} (${gear.reviewCount})", style = MaterialTheme.typography.titleMedium)
                }

                Text(
                    "Cek ketersediaan",
                    style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { showAvailability = true }
                )
            }

            HorizontalDivider(
                Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                thickness = 2.dp
            )

            OwnerRow(gear)

            HorizontalDivider(
                Modifier.padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                thickness = 2.dp
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, label ->
                    DetailTab(label = label, selected = selectedTab == index) { selectedTab = index }
                }
            }

            val items = when (selectedTab) {
                0 -> gear.description
                1 -> gear.specs
                else -> gear.included
            }

            Column(
                Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items.forEach { BulletItem(it) }
            }
        }
    }

    if (showAvailability) {
        AvailabilityCalendarDialog(
            gearId = gear.id,
            onDismiss = { showAvailability = false }
        )
    }
}

@Composable
private fun ImageHeader(gear: Gear, onBack: () -> Unit) {
    val pageCount = gear.images.size.coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val headerShape = MaterialTheme.shapes.large.copy(
        topStart = ZeroCornerSize,
        topEnd = ZeroCornerSize
    )

    Box(
        Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, headerShape)
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            gear.images.getOrNull(page)?.let { res ->
                Image(
                    painter = painterResource(res),
                    contentDescription = gear.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 90.dp, vertical = 64.dp)
                )
            }
        }

        Surface(
            onClick = onBack,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 8.dp, top = 8.dp)
                .size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
            }
        }

        if (pageCount > 1) {
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(pageCount) { index ->
                    val active = pagerState.currentPage == index
                    Box(
                        Modifier
                            .size(width = 24.dp, height = 4.dp)
                            .background(
                                if (active) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                PillShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun OwnerRow(gear: Gear) {
    val initials = initialsOf(gear.ownerName)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    initials,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(gear.ownerName, style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    "${gear.location} (±${gear.distanceKm.toString().replace('.', ',')} km)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        IconButton(onClick = { }) {
            Icon(Icons.Outlined.Phone, contentDescription = "Telepon")
        }
        IconButton(onClick = { }) {
            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Chat")
        }
    }
}

@Composable
private fun DetailTab(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = PillShape,
        color = if (selected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    ) {
        Text(
            label,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun BulletItem(text: String) {
    Row {
        Text("•", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun GearDetailScreenPreview() {
    GearHubTheme {
        GearDetailScreen(gearId = 1, onBack = {}, onRentClick = {})
    }
}