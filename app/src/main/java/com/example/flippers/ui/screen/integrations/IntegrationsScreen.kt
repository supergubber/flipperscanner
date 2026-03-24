package com.example.flippers.ui.screen.integrations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.flippers.data.model.Integration
import com.example.flippers.data.model.dummyIntegrations

private fun getIntegrationIcon(iconName: String): ImageVector = when (iconName) {
    "Chat" -> Icons.Default.Chat
    "CloudUpload" -> Icons.Default.CloudUpload
    "Cloud" -> Icons.Default.Cloud
    "Business" -> Icons.Default.Business
    "Hub" -> Icons.Default.Hub
    "Payment" -> Icons.Default.Payment
    "ShoppingCart" -> Icons.Default.ShoppingCart
    else -> Icons.Default.AccountBalance
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntegrationsScreen(
    onNavigateBack: () -> Unit,
    onIntegrationClick: (String) -> Unit
) {
    var integrations by remember { mutableStateOf(dummyIntegrations) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Integrations") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categories = integrations.groupBy { it.category }
            categories.forEach { (category, items) ->
                item {
                    Text(
                        category,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(items) { integration ->
                    IntegrationCard(
                        integration = integration,
                        onToggle = {
                            integrations = integrations.map {
                                if (it.name == integration.name) it.copy(isConnected = !it.isConnected) else it
                            }
                        },
                        onClick = { onIntegrationClick(integration.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun IntegrationCard(
    integration: Integration,
    onToggle: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    getIntegrationIcon(integration.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(integration.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    integration.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            OutlinedButton(
                onClick = onToggle,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(if (integration.isConnected) "Connected" else "Connect")
            }
        }
    }
}
