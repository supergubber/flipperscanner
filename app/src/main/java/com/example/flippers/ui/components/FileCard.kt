package com.example.flippers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.flippers.data.local.ScannedDocument
import com.example.flippers.ui.theme.AccentBlue
import com.example.flippers.ui.theme.AccentGreen
import com.example.flippers.ui.theme.AccentOrange
import com.example.flippers.ui.theme.AccentPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FileCard(
    document: ScannedDocument,
    onShareClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icon, color) = when (document.scanType.lowercase()) {
        "qr", "qr code" -> Icons.Default.QrCode2 to AccentPurple
        "book" -> Icons.AutoMirrored.Filled.MenuBook to AccentGreen
        "id", "id card" -> Icons.Default.CreditCard to AccentOrange
        else -> Icons.Default.Description to AccentBlue
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, Modifier.size(22.dp), tint = color)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(document.fileName, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${formatDate(document.createdAt)} · ${formatFileSize(document.fileSize)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            }
            IconButton(onClick = onShareClick) { Icon(Icons.Default.Share, "Share", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)) }
            IconButton(onClick = onMoreClick) { Icon(Icons.Default.MoreVert, "More", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)) }
        }
    }
}

private fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(millis))
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "${"%.1f".format(bytes / (1024.0 * 1024.0))} MB"
    }
}
