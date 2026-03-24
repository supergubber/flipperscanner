package com.example.flippers.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp

@Composable
fun ScannerOverlay(
    modifier: Modifier = Modifier,
    cutoutRatio: Float = 0.7f,
    aspectRatio: Float = 1f,
    cornerColor: Color = Color(0xFFEE9E8E)
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val cutoutWidth = size.width * cutoutRatio
        val cutoutHeight = cutoutWidth * aspectRatio
        val left = (size.width - cutoutWidth) / 2
        val top = (size.height - cutoutHeight) / 2

        val cutoutPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(left, top, left + cutoutWidth, top + cutoutHeight),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            )
        }

        clipPath(cutoutPath, clipOp = ClipOp.Difference) {
            drawRect(Color.Black.copy(alpha = 0.6f))
        }

        val cornerLength = 40.dp.toPx()
        val strokeWidth = 4.dp.toPx()
        val cornerRadius = 16.dp.toPx()

        fun drawCorner(cx: Float, cy: Float, dx: Int, dy: Int) {
            drawLine(
                color = cornerColor,
                start = Offset(cx, cy + dy * cornerRadius),
                end = Offset(cx, cy + dy * cornerLength),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = cornerColor,
                start = Offset(cx + dx * cornerRadius, cy),
                end = Offset(cx + dx * cornerLength, cy),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawArc(
                color = cornerColor,
                startAngle = when {
                    dx > 0 && dy > 0 -> 180f
                    dx < 0 && dy > 0 -> 270f
                    dx > 0 && dy < 0 -> 90f
                    else -> 0f
                },
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(
                    cx + if (dx > 0) 0f else -2 * cornerRadius,
                    cy + if (dy > 0) 0f else -2 * cornerRadius
                ),
                size = Size(2 * cornerRadius, 2 * cornerRadius),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        drawCorner(left, top, 1, 1)
        drawCorner(left + cutoutWidth, top, -1, 1)
        drawCorner(left, top + cutoutHeight, 1, -1)
        drawCorner(left + cutoutWidth, top + cutoutHeight, -1, -1)
    }
}
