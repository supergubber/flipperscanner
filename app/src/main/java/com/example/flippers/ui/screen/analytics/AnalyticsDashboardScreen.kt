package com.example.flippers.ui.screen.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flippers.data.model.dummyScanTypeBreakdown
import com.example.flippers.data.model.dummyWeekDays
import com.example.flippers.data.model.dummyWeeklyScans
import com.example.flippers.ui.theme.AccentBlue
import com.example.flippers.ui.theme.AccentGreen
import com.example.flippers.ui.theme.AccentOrange
import com.example.flippers.ui.theme.AccentPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsDashboardScreen(
    onNavigateBack: () -> Unit,
    onViewScanStats: () -> Unit
) {
    val totalScans = dummyWeeklyScans.sum()
    val dailyAverage = totalScans / dummyWeeklyScans.size
    val mostUsedType = dummyScanTypeBreakdown.maxByOrNull { it.count }?.type ?: "Document"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Stats 2x2
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MiniStat(Icons.Default.DocumentScanner, "$totalScans", "Total Scans", AccentBlue, Modifier.weight(1f))
                MiniStat(Icons.Default.CalendarToday, "$dailyAverage", "Daily Avg", AccentGreen, Modifier.weight(1f))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MiniStat(Icons.Default.Star, mostUsedType, "Most Used", AccentOrange, Modifier.weight(1f))
                MiniStat(Icons.AutoMirrored.Filled.TrendingUp, "${dummyWeeklyScans.last()}", "Today", AccentPurple, Modifier.weight(1f))
            }

            Spacer(Modifier.height(4.dp))
            Text("Weekly Activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            // Bar chart
            val primary = MaterialTheme.colorScheme.primary
            val track = MaterialTheme.colorScheme.surfaceVariant
            val maxVal = dummyWeeklyScans.max().toFloat()

            Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Canvas(Modifier.fillMaxWidth().height(170.dp)) {
                        val n = dummyWeeklyScans.size
                        val gap = 10.dp.toPx()
                        val bw = (size.width - gap * (n - 1)) / n
                        dummyWeeklyScans.forEachIndexed { i, v ->
                            val h = (v / maxVal) * size.height * 0.85f
                            val x = i * (bw + gap)
                            drawRoundRect(track, Offset(x, 0f), Size(bw, size.height), CornerRadius(8f))
                            drawRoundRect(
                                brush = Brush.verticalGradient(listOf(primary, primary.copy(alpha = 0.5f)), startY = size.height - h, endY = size.height),
                                topLeft = Offset(x, size.height - h), size = Size(bw, h), cornerRadius = CornerRadius(8f)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        dummyWeekDays.forEach { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) }
                    }
                }
            }

            // Detail link
            Surface(onClick = onViewScanStats, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(AccentPurple.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = AccentPurple, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("View Detailed Stats", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Text("›", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f))
                }
            }
        }
    }
}

@Composable
private fun MiniStat(icon: ImageVector, value: String, label: String, color: Color, modifier: Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
            }
        }
    }
}
