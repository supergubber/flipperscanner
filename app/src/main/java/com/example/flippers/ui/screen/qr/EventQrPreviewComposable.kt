package com.example.flippers.ui.screen.qr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

/**
 * Reusable live preview composable — shows the decorated QR code with animations.
 * Can be used anywhere a preview of the event QR is needed.
 */
@Composable
fun EventQrLivePreview(
    state: EventQrUiState,
    modifier: Modifier = Modifier
) {
    val theme = state.activeColorTheme

    Box(
        modifier = modifier.size(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = theme.background),
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = theme.primary.copy(alpha = 0.1f),
                    spotColor = theme.primary.copy(alpha = 0.15f)
                )
                .pulseScale()
        ) {
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val bmp = state.compositeBitmap
                if (bmp != null) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Event QR Preview",
                        modifier = Modifier.size(268.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(268.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(theme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Enter details to preview",
                            style = MaterialTheme.typography.bodyMedium,
                            color = theme.dark.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }

        // Shimmer sweep
        ShimmerOverlay(
            Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp))
        )

        // Floating emojis
        if (state.selectedEmojis.isNotEmpty()) {
            FloatingEmojiOverlay(
                emojis = state.selectedEmojis,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
