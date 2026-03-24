package com.example.flippers.ui.screen.subscription

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flippers.data.model.SubscriptionTier
import com.example.flippers.data.model.dummyPlanFeatures
import com.example.flippers.ui.theme.AccentGreen

private data class TierDisplay(val tier: SubscriptionTier, val isCurrent: Boolean, val highlight: Boolean)
private val tiers = listOf(
    TierDisplay(SubscriptionTier.Free, true, false),
    TierDisplay(SubscriptionTier.Pro, false, true),
    TierDisplay(SubscriptionTier.Enterprise, false, false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Your Plan", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Unlock the full power of ProScan", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

            tiers.forEach { display ->
                val borderMod = if (display.highlight) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp)) else Modifier
                Surface(modifier = Modifier.fillMaxWidth().then(borderMod), shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = if (display.highlight) 2.dp else 1.dp) {
                    Column {
                        if (display.highlight) {
                            Box(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).background(Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)))).padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Rocket, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("MOST POPULAR", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                        Column(Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(display.tier.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.weight(1f))
                                if (display.isCurrent) {
                                    Surface(shape = RoundedCornerShape(8.dp), color = AccentGreen.copy(alpha = 0.12f)) {
                                        Text("Current", modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = AccentGreen)
                                    }
                                }
                            }
                            Text(display.tier.price, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 6.dp))

                            dummyPlanFeatures.take(5).forEach { f ->
                                val v = when (display.tier) { SubscriptionTier.Free -> f.freeValue; SubscriptionTier.Pro -> f.proValue; SubscriptionTier.Enterprise -> f.enterpriseValue }
                                val ok = v != "—"
                                Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(20.dp).clip(RoundedCornerShape(6.dp)).background(if (ok) AccentGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)), contentAlignment = Alignment.Center) {
                                        Icon(if (ok) Icons.Default.Check else Icons.Default.Close, null, modifier = Modifier.size(12.dp), tint = if (ok) AccentGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f))
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Text(f.name, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                                    Text(v, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = if (ok) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                                }
                            }
                            Spacer(Modifier.height(14.dp))
                            if (!display.isCurrent) {
                                if (display.highlight) {
                                    Button(onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                                        Text("Upgrade to ${display.tier.name}", fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    OutlinedButton(onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(14.dp)) {
                                        Text("Upgrade to ${display.tier.name}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
