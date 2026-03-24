package com.example.flippers.ui.screen.qr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flippers.ui.theme.AccentBlue
import com.example.flippers.ui.theme.AccentGreen
import com.example.flippers.ui.theme.AccentOrange
import com.example.flippers.ui.theme.AccentPurple
import com.example.flippers.ui.theme.AccentRed
import com.example.flippers.ui.theme.AccentTeal

private data class QrType(val label: String, val icon: ImageVector, val key: String, val color: Color)

private val qrTypes = listOf(
    QrType("URL", Icons.Default.Language, "URL", AccentBlue),
    QrType("Text", Icons.Default.TextFields, "Text", AccentGreen),
    QrType("WiFi", Icons.Default.Wifi, "WiFi", AccentPurple),
    QrType("Contact", Icons.Default.ContactPhone, "Contact", AccentOrange),
    QrType("Payment", Icons.Default.Payment, "Payment", AccentRed),
    QrType("Email", Icons.Default.Email, "Email", AccentTeal),
    QrType("Phone", Icons.Default.Phone, "Phone", Color(0xFF5C6BC0))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrGeneratorScreen(
    onNavigateBack: () -> Unit,
    onTypeSelected: (String) -> Unit,
    onHistoryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate QR Code", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = { IconButton(onClick = onHistoryClick) { Icon(Icons.Default.History, "History") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Text("Choose type", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(qrTypes) { type ->
                    Surface(
                        modifier = Modifier.fillMaxWidth().clickable { onTypeSelected(type.key) },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(14.dp)).background(type.color.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(type.icon, type.label, Modifier.size(24.dp), tint = type.color)
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(type.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}
