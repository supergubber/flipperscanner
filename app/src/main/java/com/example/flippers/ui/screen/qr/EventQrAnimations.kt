package com.example.flippers.ui.screen.qr

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Reusable shimmer overlay — draws a diagonal light sweep across any composable.
 * Usage: ShimmerOverlay(Modifier.matchParentSize().clip(shape))
 */
@Composable
fun ShimmerOverlay(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = LinearEasing)
        ),
        label = "shimmer_x"
    )
    Box(
        modifier = modifier.background(
            Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.White.copy(alpha = 0.12f),
                    Color.Transparent
                ),
                start = Offset(shimmerX, 0f),
                end = Offset(shimmerX + 300f, 300f)
            )
        )
    )
}

/**
 * Reusable floating emoji overlay — emojis drift upward with fade, staggered.
 * Usage: FloatingEmojiOverlay(emojis = listOf("💍","💒"), Modifier.matchParentSize())
 */
@Composable
fun FloatingEmojiOverlay(
    emojis: List<String>,
    modifier: Modifier = Modifier
) {
    if (emojis.isEmpty()) return

    val transition = rememberInfiniteTransition(label = "float_emojis")

    Box(modifier = modifier) {
        emojis.take(4).forEachIndexed { index, emoji ->
            val delay = index * 500
            val yOffset by transition.animateFloat(
                initialValue = 0f,
                targetValue = -50f,
                animationSpec = infiniteRepeatable(
                    tween(2500, delayMillis = delay, easing = LinearEasing),
                    RepeatMode.Restart
                ),
                label = "y_$index"
            )
            val alpha by transition.animateFloat(
                initialValue = 0.9f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    tween(2500, delayMillis = delay, easing = LinearEasing),
                    RepeatMode.Restart
                ),
                label = "alpha_$index"
            )

            val xPosition = when (index) {
                0 -> 12.dp
                1 -> 240.dp
                2 -> 60.dp
                else -> 200.dp
            }
            val yPosition = when (index) {
                0, 1 -> 20.dp
                else -> 230.dp
            }

            Text(
                text = emoji,
                fontSize = 22.sp,
                modifier = Modifier
                    .offset(x = xPosition, y = yPosition)
                    .graphicsLayer {
                        translationY = yOffset
                        this.alpha = alpha
                    }
            )
        }
    }
}

/**
 * Reusable pulse scale modifier — gentle breathing scale animation.
 * Usage: Modifier.pulseScale()
 */
@Composable
fun Modifier.pulseScale(): Modifier {
    val transition = rememberInfiniteTransition(label = "pulse")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            tween(1200),
            RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}
