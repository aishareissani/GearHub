package org.umn.gearhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import org.umn.gearhub.ui.theme.GearHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GearHubTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ThemeTestScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ThemeTestScreen(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    val c = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(c.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ---------- WARNA ----------
        SectionTitle("Colors")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Swatch("primary", c.primary, c.onPrimary, Modifier.weight(1f))
            Swatch("secondary", c.secondary, c.onSecondary, Modifier.weight(1f))
            Swatch("error", c.error, c.onError, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Swatch("background", c.background, c.onBackground, Modifier.weight(1f))
            Swatch("surface", c.surface, c.onSurface, Modifier.weight(1f))
            Swatch("surfaceVar", c.surfaceVariant, c.onSurfaceVariant, Modifier.weight(1f))
        }

        // ---------- TIPOGRAFI ----------
        SectionTitle("Typography")
        Text("Headline Large 32", style = MaterialTheme.typography.headlineLarge)
        Text("Headline Medium 28", style = MaterialTheme.typography.headlineMedium)
        Text("Headline Small 24", style = MaterialTheme.typography.headlineSmall)
        Text("Title Large 22", style = MaterialTheme.typography.titleLarge)
        Text("Title Medium 16", style = MaterialTheme.typography.titleMedium)
        Text("Title Small 14", style = MaterialTheme.typography.titleSmall)
        Text("Body Large 16 - Tenda Consina Magnum 4P", style = MaterialTheme.typography.bodyLarge)
        Text("Body Medium 14 - Rp 75.000 / hari", style = MaterialTheme.typography.bodyMedium)
        Text("Body Small 12 - 1,2 km dari lokasimu", style = MaterialTheme.typography.bodySmall)
        Text("Label Large 14", style = MaterialTheme.typography.labelLarge)
        Text("Label Medium 12", style = MaterialTheme.typography.labelMedium)
        Text("Label Small 11", style = MaterialTheme.typography.labelSmall)

        // ---------- SHAPE & KOMPONEN ----------
        SectionTitle("Shapes & Components")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium, // 12dp
            colors = CardDefaults.cardColors(containerColor = c.surface)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Card 12dp", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Isi kartu pakai bodyMedium dan warna onSurface.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text("Tenda") }) // 4dp
                    AssistChip(onClick = {}, label = { Text("Carrier") })
                }
            }
        }

        Button(
            onClick = {},
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ajukan Sewa") }

        OutlinedButton(
            onClick = {},
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Batal") }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Catatan") },
            supportingText = { Text(if (text.isBlank()) "Wajib diisi" else "Oke") },
            isError = text.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun Swatch(name: String, color: Color, onColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(72.dp)
            .background(color, MaterialTheme.shapes.small)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = onColor,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, heightDp = 1400)
@Composable
fun ThemeTestPreview() {
    GearHubTheme {
        ThemeTestScreen()
    }
}