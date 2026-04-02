package com.example.flippers.ui.screen.qr

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.text.TextPaint
import androidx.compose.ui.graphics.toArgb

object EventQrRenderer {

    fun renderComposite(
        qrBitmap: Bitmap,
        frame: FrameStyle,
        emojis: List<String>,
        colorTheme: EventColorTheme,
        centerImage: Bitmap?,
        outputSize: Int = 640
    ): Bitmap {
        val output = Bitmap.createBitmap(outputSize, outputSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val size = outputSize.toFloat()

        // 1. Background
        canvas.drawColor(colorTheme.background.toArgb())

        // 2. Frame border
        drawFrame(canvas, frame, colorTheme, size)

        // 3. QR code centered
        val qrInset = 56f
        val qrSize = size - qrInset * 2
        val qrRect = RectF(qrInset, qrInset, qrInset + qrSize, qrInset + qrSize)
        canvas.drawBitmap(qrBitmap, null, qrRect, null)

        // 4. Center image overlay
        centerImage?.let { logo ->
            drawCenterLogo(canvas, logo, size, qrSize)
        }

        // 5. Corner emojis (user-selected)
        drawCornerEmojis(canvas, emojis, size)

        // 6. Frame decoration emojis (style-specific)
        drawFrameDecorations(canvas, frame, size)

        return output
    }

    private fun drawFrame(canvas: Canvas, frame: FrameStyle, theme: EventColorTheme, size: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = theme.primary.toArgb()
        }
        val inset = 16f

        when (frame) {
            FrameStyle.ELEGANT -> {
                paint.strokeWidth = 4f
                val outer = RectF(inset, inset, size - inset, size - inset)
                canvas.drawRoundRect(outer, 24f, 24f, paint)
                paint.strokeWidth = 1.5f
                val inner = RectF(inset + 8f, inset + 8f, size - inset - 8f, size - inset - 8f)
                canvas.drawRoundRect(inner, 20f, 20f, paint)
            }
            FrameStyle.MINIMAL -> {
                paint.strokeWidth = 2f
                canvas.drawRect(RectF(inset, inset, size - inset, size - inset), paint)
            }
            else -> {
                paint.strokeWidth = 5f
                val rect = RectF(inset, inset, size - inset, size - inset)
                canvas.drawRoundRect(rect, 16f, 16f, paint)
            }
        }
    }

    private fun drawCenterLogo(canvas: Canvas, logo: Bitmap, totalSize: Float, qrSize: Float) {
        val logoSize = qrSize * 0.22f
        val cx = totalSize / 2f
        val cy = totalSize / 2f

        // White circle background
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
        }
        canvas.drawCircle(cx, cy, logoSize / 2f + 6f, bgPaint)

        // Logo image
        val logoRect = RectF(
            cx - logoSize / 2f, cy - logoSize / 2f,
            cx + logoSize / 2f, cy + logoSize / 2f
        )
        canvas.drawBitmap(logo, null, logoRect, Paint(Paint.ANTI_ALIAS_FLAG))
    }

    private fun drawCornerEmojis(canvas: Canvas, emojis: List<String>, size: Float) {
        if (emojis.isEmpty()) return

        val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 36f
            textAlign = Paint.Align.CENTER
        }

        val positions = listOf(
            PointF(36f, 44f),
            PointF(size - 36f, 44f),
            PointF(36f, size - 16f),
            PointF(size - 36f, size - 16f)
        )

        emojis.take(4).forEachIndexed { i, emoji ->
            canvas.drawText(emoji, positions[i].x, positions[i].y, paint)
        }
    }

    private fun drawFrameDecorations(canvas: Canvas, frame: FrameStyle, size: Float) {
        if (frame.emoji.isEmpty()) return

        val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 20f
            textAlign = Paint.Align.CENTER
        }

        val spacing = size / 5f
        for (i in 1..4) {
            val x = spacing * i
            canvas.drawText(frame.emoji, x, 14f, paint)
            canvas.drawText(frame.emoji, x, size - 4f, paint)
            canvas.drawText(frame.emoji, 8f, spacing * i, paint)
            canvas.drawText(frame.emoji, size - 8f, spacing * i, paint)
        }
    }
}
