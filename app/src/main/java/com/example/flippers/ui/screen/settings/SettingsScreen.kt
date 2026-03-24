package com.example.flippers.ui.screen.settings

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flippers.ui.theme.AccentBlue
import com.example.flippers.ui.theme.AccentGreen
import com.example.flippers.ui.theme.AccentOrange
import com.example.flippers.ui.theme.AccentPurple
import com.example.flippers.ui.theme.AccentRed
import com.example.flippers.ui.theme.AccentTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onProfileClick: () -> Unit,
    onSubscriptionClick: () -> Unit,
    onPaymentMethodsClick: () -> Unit,
    onUsageDashboardClick: () -> Unit = {},
    onAdminDashboardClick: () -> Unit = {},
    onIntegrationsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var notificationsOn by remember { mutableStateOf(true) }
    var autoSaveOn by remember { mutableStateOf(true) }
    var cloudBackupOn by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Account
            GroupLabel("Account")
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettRow(Icons.Default.Person, "Profile", AccentBlue, onClick = onProfileClick)
                    SettRow(Icons.Default.Star, "Subscription", AccentOrange, onClick = onSubscriptionClick)
                    SettRow(Icons.Default.CreditCard, "Payment Methods", AccentGreen, onClick = onPaymentMethodsClick)
                    SettRow(Icons.Default.BarChart, "Usage Dashboard", AccentPurple, onClick = onUsageDashboardClick, isLast = true)
                }
            }

            GroupLabel("Enterprise")
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettRow(Icons.Default.AdminPanelSettings, "Admin Dashboard", AccentRed, onClick = onAdminDashboardClick)
                    SettRow(Icons.Default.Extension, "Integrations", AccentTeal, onClick = onIntegrationsClick, isLast = true)
                }
            }

            GroupLabel("App")
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettRow(Icons.Default.Language, "Language", AccentBlue, sub = "English") { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                    SettRow(Icons.Default.DarkMode, "Theme", AccentPurple, sub = "System") { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                    ToggleRow(Icons.Default.Notifications, "Notifications", AccentOrange, notificationsOn) { notificationsOn = it }
                }
            }

            GroupLabel("Scanning")
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettRow(Icons.Default.Scanner, "Default Mode", AccentGreen, sub = "Document") { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                    ToggleRow(Icons.Default.Storage, "Auto-save", AccentBlue, autoSaveOn) { autoSaveOn = it }
                    SettRow(Icons.Default.HighQuality, "Image Quality", AccentPurple, sub = "High", isLast = true) { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                }
            }

            GroupLabel("Storage")
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column {
                    ToggleRow(Icons.Default.Storage, "Cloud Backup", AccentTeal, cloudBackupOn, sub = "Pro feature") { Toast.makeText(context, "Upgrade to Pro", Toast.LENGTH_SHORT).show() }
                    SettRow(Icons.Default.DeleteForever, "Clear Cache", AccentRed, isLast = true) { Toast.makeText(context, "Cache cleared", Toast.LENGTH_SHORT).show() }
                }
            }

            GroupLabel("About")
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettRow(Icons.Default.Info, "Version", AccentBlue, sub = "1.0.0") {}
                    SettRow(Icons.Default.Star, "Rate App", AccentOrange) { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                    SettRow(Icons.Default.Description, "Privacy Policy", AccentGreen) { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                    SettRow(Icons.Default.Support, "Contact Support", AccentPurple, isLast = true) { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                }
            }

            GroupLabel("Danger Zone")
            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettRow(Icons.Default.DeleteForever, "Delete Account", AccentRed) { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                    SettRow(Icons.AutoMirrored.Filled.Logout, "Log Out", AccentRed, isLast = true) { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun GroupLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f), modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 4.dp))
}

@Composable
private fun SettRow(icon: ImageVector, title: String, color: Color, sub: String? = null, isLast: Boolean = false, onClick: () -> Unit = {}) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                if (sub != null) Text(sub, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            }
            Text("›", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        }
        if (!isLast) {
            Box(modifier = Modifier.fillMaxWidth().padding(start = 64.dp, end = 16.dp).height(0.5.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))
        }
    }
}

@Composable
private fun ToggleRow(icon: ImageVector, title: String, color: Color, checked: Boolean, sub: String? = null, isLast: Boolean = false, onToggle: (Boolean) -> Unit) {
    Column {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                if (sub != null) Text(sub, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            }
            Switch(checked = checked, onCheckedChange = onToggle)
        }
        if (!isLast) {
            Box(modifier = Modifier.fillMaxWidth().padding(start = 64.dp, end = 16.dp).height(0.5.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)))
        }
    }
}
