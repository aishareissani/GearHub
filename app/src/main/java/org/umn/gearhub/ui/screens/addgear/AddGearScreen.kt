package org.umn.gearhub.ui.screens.addgear

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.umn.gearhub.data.repository.BookingRepository
import org.umn.gearhub.data.repository.GearRepository
import org.umn.gearhub.data.dummy.DummyLocations
import org.umn.gearhub.data.model.Gear
import org.umn.gearhub.data.model.GearCategory
import org.umn.gearhub.ui.components.AuthButton
import org.umn.gearhub.ui.components.AuthTextField
import org.umn.gearhub.ui.components.CategoryChip
import org.umn.gearhub.ui.theme.GearHubTheme
import org.umn.gearhub.util.AddGearValidator
import org.umn.gearhub.util.formatRupiah

@Composable
fun AddGearScreen(
    ownerName: String,
    onBack: () -> Unit,
    onPublished: (gearId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var categoryName by rememberSaveable { mutableStateOf<String?>(null) }
    var price by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf<String?>(null) }
    var description by rememberSaveable { mutableStateOf("") }
    var included by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var isPublishing by rememberSaveable { mutableStateOf(false) }
    var publishError by rememberSaveable { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val category = categoryName?.let { GearCategory.valueOf(it) }

    // Required-field errors wait for submit; format errors show as soon as the user types.
    fun shown(value: String, error: String?) = if (submitted || value.isNotEmpty()) error else null
    val nameError = shown(name, AddGearValidator.validateName(name))
    val priceError = shown(price, AddGearValidator.validatePrice(price))
    val descriptionError = shown(description, AddGearValidator.validateDescription(description))
    val includedError = shown(included, AddGearValidator.validateIncluded(included))
    val categoryError = if (submitted) AddGearValidator.validateCategory(categoryName) else null
    val locationError = if (submitted) AddGearValidator.validateLocation(location) else null

    val priceGuide = category?.let { cat ->
        val prices = GearRepository.list.filter { it.category == cat }.map { it.pricePerDay }
        if (prices.isEmpty()) null
        else "Harga wajar ${cat.label}: ${formatRupiah(prices.min())} – ${formatRupiah(prices.max())}/hari"
    }

    val submit: () -> Unit = {
        submitted = true
        val isValid = listOf(
            AddGearValidator.validateName(name),
            AddGearValidator.validateCategory(categoryName),
            AddGearValidator.validatePrice(price),
            AddGearValidator.validateLocation(location),
            AddGearValidator.validateDescription(description),
            AddGearValidator.validateIncluded(included)
        ).all { it == null }
        val pickupLocation = location
        if (isValid && category != null && pickupLocation != null && !isPublishing) {
            val gear = Gear(
                id = 0,
                name = name.trim(),
                category = category,
                pricePerDay = price.toInt(),
                rating = 0.0,
                reviewCount = 0,
                distanceKm = 0.5,
                ownerName = ownerName,
                location = pickupLocation,
                description = listOf(description.trim()),
                specs = emptyList(),
                included = AddGearValidator.splitItems(included),
                availableFrom = BookingRepository.daysFromToday(0),
                availableUntil = BookingRepository.daysFromToday(60)
            )
            isPublishing = true
            publishError = null
            scope.launch {
                runCatching { GearRepository.add(gear) }
                    .onSuccess { onPublished(it.id) }
                    .onFailure { publishError = "Gagal menerbitkan gear. Coba lagi." }
                isPublishing = false
            }
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .imePadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                "Tambah gear",
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
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AuthTextField(
                label = "Nama gear",
                value = name,
                onValueChange = { name = it },
                placeholder = "Contoh: Sony A6400 Kit 16-50mm",
                errorMessage = nameError,
                capitalization = KeyboardCapitalization.Words
            )

            ChipGroup("Kategori", categoryError) {
                GearCategory.entries.forEach { cat ->
                    CategoryChip(cat.label, selected = category == cat, onClick = { categoryName = cat.name })
                }
            }

            Column {
                AuthTextField(
                    label = "Harga sewa per hari (Rp)",
                    value = price,
                    onValueChange = { price = it.filter(Char::isDigit) },
                    placeholder = "Contoh: 75000",
                    errorMessage = priceError,
                    keyboardType = KeyboardType.Number
                )
                if (priceError == null && priceGuide != null) {
                    Text(
                        priceGuide,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            ChipGroup("Lokasi pengambilan", locationError) {
                DummyLocations.list.forEach { loc ->
                    CategoryChip(loc, selected = location == loc, onClick = { location = loc })
                }
            }

            AuthTextField(
                label = "Deskripsi & kondisi",
                value = description,
                onValueChange = { description = it },
                placeholder = "Contoh: Mulus, shutter count 5rb",
                errorMessage = descriptionError,
                capitalization = KeyboardCapitalization.Sentences
            )

            AuthTextField(
                label = "Kelengkapan (pisahkan dengan koma)",
                value = included,
                onValueChange = { included = it },
                placeholder = "Unit, baterai, charger, tas",
                errorMessage = includedError,
                imeAction = ImeAction.Done,
                onDone = submit
            )
        }

        publishError?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        AuthButton(
            text = "Terbitkan gear",
            isLoading = isPublishing,
            onClick = submit,
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun ChipGroup(label: String, error: String?, chips: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) { chips() }
        error?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun AddGearScreenPreview() {
    GearHubTheme { AddGearScreen(ownerName = "Jane Doe", onBack = {}, onPublished = {}) }
}
