package com.example.flippers.ui.screen.qr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flippers.ui.components.ProScanButton
import com.example.flippers.ui.components.ProScanTextField
import com.example.flippers.viewmodel.QrGeneratorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrContentInputScreen(
    qrType: String,
    viewModel: QrGeneratorViewModel,
    onNavigateBack: () -> Unit,
    onGenerate: () -> Unit
) {
    val fields by viewModel.contentFields.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$qrType QR Code") },
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter $qrType Details",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (qrType) {
                "URL" -> {
                    ProScanTextField(
                        value = fields["url"] ?: "",
                        onValueChange = { viewModel.updateField("url", it) },
                        label = "Enter URL",
                        leadingIcon = Icons.Default.Language
                    )
                }
                "Text" -> {
                    ProScanTextField(
                        value = fields["text"] ?: "",
                        onValueChange = { viewModel.updateField("text", it) },
                        label = "Enter Text",
                        leadingIcon = Icons.Default.TextFields,
                        singleLine = false
                    )
                }
                "WiFi" -> {
                    ProScanTextField(
                        value = fields["ssid"] ?: "",
                        onValueChange = { viewModel.updateField("ssid", it) },
                        label = "Network Name (SSID)",
                        leadingIcon = Icons.Default.Wifi
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ProScanTextField(
                        value = fields["password"] ?: "",
                        onValueChange = { viewModel.updateField("password", it) },
                        label = "Password",
                        leadingIcon = Icons.Default.Lock
                    )
                }
                "Contact" -> {
                    ProScanTextField(
                        value = fields["name"] ?: "",
                        onValueChange = { viewModel.updateField("name", it) },
                        label = "Full Name",
                        leadingIcon = Icons.Default.Person
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ProScanTextField(
                        value = fields["phone"] ?: "",
                        onValueChange = { viewModel.updateField("phone", it) },
                        label = "Phone Number",
                        leadingIcon = Icons.Default.Phone
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ProScanTextField(
                        value = fields["email"] ?: "",
                        onValueChange = { viewModel.updateField("email", it) },
                        label = "Email",
                        leadingIcon = Icons.Default.Email
                    )
                }
                "Email" -> {
                    ProScanTextField(
                        value = fields["email"] ?: "",
                        onValueChange = { viewModel.updateField("email", it) },
                        label = "Email Address",
                        leadingIcon = Icons.Default.Email
                    )
                }
                "Phone" -> {
                    ProScanTextField(
                        value = fields["phone"] ?: "",
                        onValueChange = { viewModel.updateField("phone", it) },
                        label = "Phone Number",
                        leadingIcon = Icons.Default.Phone
                    )
                }
                "Payment" -> {
                    ProScanTextField(
                        value = fields["upi"] ?: "",
                        onValueChange = { viewModel.updateField("upi", it) },
                        label = "UPI ID",
                        leadingIcon = Icons.Default.Payment
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            ProScanButton(
                text = "Generate QR Code",
                onClick = onGenerate
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
