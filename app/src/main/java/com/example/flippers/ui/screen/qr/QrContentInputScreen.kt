package com.example.flippers.ui.screen.qr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flippers.ui.components.ProScanButton
import com.example.flippers.ui.components.ProScanTextField
import com.example.flippers.ui.theme.AccentBlue
import com.example.flippers.ui.theme.AccentGreen
import com.example.flippers.ui.theme.AccentOrange
import com.example.flippers.ui.theme.AccentPurple
import com.example.flippers.ui.theme.AccentRed
import com.example.flippers.ui.theme.AccentTeal
import com.example.flippers.viewmodel.QrGeneratorViewModel

private fun typeIcon(type: String): ImageVector = when (type) {
    "URL" -> Icons.Default.Language
    "Text" -> Icons.Default.TextFields
    "WiFi" -> Icons.Default.Wifi
    "Contact" -> Icons.Default.ContactPhone
    "Email" -> Icons.Default.Email
    "Phone" -> Icons.Default.Phone
    "Payment" -> Icons.Default.Payment
    else -> Icons.Default.Language
}

private fun typeColor(type: String): Color = when (type) {
    "URL" -> AccentBlue
    "Text" -> AccentGreen
    "WiFi" -> AccentPurple
    "Contact" -> AccentOrange
    "Email" -> AccentTeal
    "Phone" -> Color(0xFF5C6BC0)
    "Payment" -> AccentRed
    else -> AccentBlue
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrContentInputScreen(
    qrType: String,
    viewModel: QrGeneratorViewModel,
    onNavigateBack: () -> Unit,
    onGenerate: () -> Unit
) {
    val fields by viewModel.contentFields.collectAsState()
    val accentColor = typeColor(qrType)
    val icon = typeIcon(qrType)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$qrType QR Code", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Type header with icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Enter $qrType Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

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
