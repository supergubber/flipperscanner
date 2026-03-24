package com.example.flippers.ui.screen.integrations

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.flippers.ui.components.ProScanButton
import com.example.flippers.ui.components.ProScanTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntegrationDetailScreen(
    integrationName: String,
    onNavigateBack: () -> Unit
) {
    var apiKey by remember { mutableStateOf("") }
    var webhookUrl by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$integrationName Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                "Configure $integrationName",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Enter your API credentials to connect $integrationName with ProScan.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProScanTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = "API Key",
                leadingIcon = Icons.Default.Key
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProScanTextField(
                value = webhookUrl,
                onValueChange = { webhookUrl = it },
                label = "Webhook URL",
                leadingIcon = Icons.Default.Link
            )

            Spacer(modifier = Modifier.height(32.dp))

            ProScanButton(
                text = "Save Configuration",
                onClick = {
                    Toast.makeText(context, "Configuration saved (demo)", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
