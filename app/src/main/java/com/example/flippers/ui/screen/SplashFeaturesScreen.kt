package com.example.flippers.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// shared palette
private val DeepNavy = Color(0xFF0A0E27)
private val MidBlue = Color(0xFF1B2559)
private val BrandBlue = Color(0xFF4D6EF5)
private val AccentCyan = Color(0xFF00D4FF)
private val AccentGreen = Color(0xFF00E676)
private val AccentAmber = Color(0xFFFFAB40)
private val SubtleWhite = Color(0xB3FFFFFF)
private val GhostWhite = Color(0x33FFFFFF)

@Composable
fun SplashFeaturesScreen(onNext: () -> Unit) {

    // ---- entrance animations ----
    val headerAlpha = remember { Animatable(0f) }
    val headerOffsetY = remember { Animatable(30f) }
    val card1Alpha = remember { Animatable(0f) }
    val card1OffsetX = remember { Animatable(-80f) }
    val card2Alpha = remember { Animatable(0f) }
    val card2OffsetX = remember { Animatable(80f) }
    val card3Alpha = remember { Animatable(0f) }
    val card3OffsetX = remember { Animatable(-80f) }
    val bottomAlpha = remember { Animatable(0f) }
    val bottomOffsetY = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        launch { headerAlpha.animateTo(1f, tween(500)) }
        launch { headerOffsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
        delay(250)
        launch { card1Alpha.animateTo(1f, tween(500)) }
        launch { card1OffsetX.animateTo(0f, tween(600, easing = FastOutSlowInEasing)) }
        delay(200)
        launch { card2Alpha.animateTo(1f, tween(500)) }
        launch { card2OffsetX.animateTo(0f, tween(600, easing = FastOutSlowInEasing)) }
        delay(200)
        launch { card3Alpha.animateTo(1f, tween(500)) }
        launch { card3OffsetX.animateTo(0f, tween(600, easing = FastOutSlowInEasing)) }
        delay(300)
        launch { bottomAlpha.animateTo(1f, tween(500)) }
        launch { bottomOffsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
    }

    // ---- infinite animations ----
    val inf = rememberInfiniteTransition(label = "feat")
    val glowPulse by inf.animateFloat(
        0.4f, 1f,
        infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val particlePhase by inf.animateFloat(
        0f, (2 * PI).toFloat(),
        infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "particles"
    )
    val iconFloat by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(3000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "float"
    )
    val orbitAngle by inf.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "orbit"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DeepNavy, MidBlue, DeepNavy)))
    ) {
        // particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawFeatureParticles(particlePhase, size)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))

            // header
            Text(
                text = "Powerful Features",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .alpha(headerAlpha.value)
                    .offset(y = headerOffsetY.value.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Everything you need in one app",
                fontSize = 14.sp,
                color = SubtleWhite,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .alpha(headerAlpha.value)
                    .offset(y = headerOffsetY.value.dp)
            )
            // accent bar
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .width(40.dp)
                    .height(3.dp)
                    .alpha(headerAlpha.value)
                    .background(
                        Brush.horizontalGradient(listOf(Color.Transparent, AccentCyan, Color.Transparent)),
                        RoundedCornerShape(50)
                    )
            )

            Spacer(modifier = Modifier.height(44.dp))

            // ---- feature card 1 : document scanner ----
            FeatureCard(
                alpha = card1Alpha.value,
                offsetX = card1OffsetX.value,
                iconFloat = iconFloat,
                glowPulse = glowPulse,
                orbitAngle = orbitAngle,
                accentColor = BrandBlue,
                title = "Document Scanner",
                description = "Capture crystal-clear scans of documents, receipts, whiteboards and books with auto-edge detection",
                drawIcon = { s, color -> drawDocScanIcon(s, color) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ---- feature card 2 : QR & barcode ----
            FeatureCard(
                alpha = card2Alpha.value,
                offsetX = card2OffsetX.value,
                iconFloat = iconFloat,
                glowPulse = glowPulse,
                orbitAngle = -orbitAngle,
                accentColor = AccentGreen,
                title = "QR & Barcode Reader",
                description = "Instantly decode any QR code or barcode — results appear in real time with one tap copy",
                drawIcon = { s, color -> drawQrIcon(s, color) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ---- feature card 3 : ID card ----
            FeatureCard(
                alpha = card3Alpha.value,
                offsetX = card3OffsetX.value,
                iconFloat = iconFloat,
                glowPulse = glowPulse,
                orbitAngle = orbitAngle * 0.7f,
                accentColor = AccentAmber,
                title = "ID Card Scanner",
                description = "Digitize ID cards, driver licenses and passports with smart card-edge framing",
                drawIcon = { s, color -> drawIdCardIcon(s, color) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // ---- page indicator ----
            Row(
                modifier = Modifier
                    .alpha(bottomAlpha.value)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (i == 1) 24.dp else 8.dp, 8.dp)
                            .background(
                                if (i == 1) AccentCyan else GhostWhite,
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            // ---- Next button ----
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .alpha(bottomAlpha.value)
                    .offset(y = bottomOffsetY.value.dp)
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    BrandBlue.copy(alpha = 0.4f * glowPulse),
                                    AccentCyan.copy(alpha = 0.3f * glowPulse)
                                )
                            ),
                            cornerRadius = CornerRadius(30.dp.toPx()),
                            size = Size(size.width + 8.dp.toPx(), size.height + 8.dp.toPx()),
                            topLeft = Offset(-4.dp.toPx(), -4.dp.toPx())
                        )
                    },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Text("Next", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// =====================================================================
// Feature card composable
// =====================================================================

@Composable
private fun FeatureCard(
    alpha: Float,
    offsetX: Float,
    iconFloat: Float,
    glowPulse: Float,
    orbitAngle: Float,
    accentColor: Color,
    title: String,
    description: String,
    drawIcon: DrawScope.(Size, Color) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .offset(x = offsetX.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(accentColor.copy(alpha = 0.08f), Color.Transparent)
                ),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // animated icon hub
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            // orbiting dot
            Canvas(modifier = Modifier.size(64.dp)) {
                val r = size.minDimension / 2f - 2.dp.toPx()
                val rad = Math.toRadians(orbitAngle.toDouble())
                val dotX = center.x + r * cos(rad).toFloat()
                val dotY = center.y + r * sin(rad).toFloat()
                drawCircle(accentColor.copy(alpha = 0.6f), 3.dp.toPx(), Offset(dotX, dotY))
                drawCircle(accentColor.copy(alpha = 0.15f), 8.dp.toPx(), Offset(dotX, dotY))
            }
            // glow
            Canvas(modifier = Modifier.size(52.dp)) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(accentColor.copy(alpha = 0.35f * glowPulse), Color.Transparent)
                    ),
                    radius = size.minDimension / 2f
                )
            }
            // hexagonal border
            Canvas(modifier = Modifier.size(50.dp)) {
                drawHexBorder(size, accentColor.copy(alpha = 0.5f))
            }
            // icon
            Canvas(
                modifier = Modifier
                    .size(26.dp)
                    .offset(y = (-2f + iconFloat * 4f).dp)
            ) {
                drawIcon(size, Color.White)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = SubtleWhite,
                lineHeight = 17.sp
            )
        }
    }
}

// =====================================================================
// Draw helpers
// =====================================================================

private fun DrawScope.drawHexBorder(s: Size, color: Color) {
    val cx = s.width / 2f
    val cy = s.height / 2f
    val r = s.minDimension / 2f - 1.dp.toPx()
    val path = Path()
    for (i in 0..5) {
        val angle = Math.toRadians((60.0 * i) - 30.0)
        val px = cx + r * cos(angle).toFloat()
        val py = cy + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
    }
    path.close()
    drawPath(path, color, style = Stroke(1.5f.dp.toPx(), cap = StrokeCap.Round))
}

/** Document scanner icon — magnifier over page */
private fun DrawScope.drawDocScanIcon(s: Size, color: Color) {
    val w = s.width; val h = s.height
    val st = 1.8f.dp.toPx()
    // page
    val page = Path().apply {
        val fold = w * 0.25f; val rad = 2.dp.toPx()
        moveTo(0f, rad); quadraticTo(0f, 0f, rad, 0f)
        lineTo(w * 0.6f - fold, 0f); lineTo(w * 0.6f, fold)
        lineTo(w * 0.6f, h - rad); quadraticTo(w * 0.6f, h, w * 0.6f - rad, h)
        lineTo(rad, h); quadraticTo(0f, h, 0f, h - rad); close()
    }
    drawPath(page, color, style = Stroke(st, cap = StrokeCap.Round))
    // lines
    for (i in 0..2) {
        val ly = h * 0.35f + i * h * 0.17f
        drawLine(color.copy(0.5f), Offset(w * 0.12f, ly), Offset(w * (if (i == 2) 0.35f else 0.48f), ly), st * 0.6f, StrokeCap.Round)
    }
    // magnifier
    val mx = w * 0.72f; val my = h * 0.58f; val mr = w * 0.18f
    drawCircle(color, mr, Offset(mx, my), style = Stroke(st))
    val handleAngle = Math.toRadians(45.0)
    drawLine(
        color, Offset(mx + mr * cos(handleAngle).toFloat(), my + mr * sin(handleAngle).toFloat()),
        Offset(mx + (mr + w * 0.15f) * cos(handleAngle).toFloat(), my + (mr + w * 0.15f) * sin(handleAngle).toFloat()),
        st * 1.2f, StrokeCap.Round
    )
}

/** QR code icon */
private fun DrawScope.drawQrIcon(s: Size, color: Color) {
    val w = s.width; val h = s.height
    val st = 1.5f.dp.toPx()
    val unit = w / 7f
    // top-left finder
    drawRect(color, Offset(0f, 0f), Size(3 * unit, 3 * unit), style = Stroke(st))
    drawRect(color, Offset(unit * 0.7f, unit * 0.7f), Size(1.6f * unit, 1.6f * unit))
    // top-right finder
    drawRect(color, Offset(4 * unit, 0f), Size(3 * unit, 3 * unit), style = Stroke(st))
    drawRect(color, Offset(4.7f * unit, 0.7f * unit), Size(1.6f * unit, 1.6f * unit))
    // bottom-left finder
    drawRect(color, Offset(0f, 4 * unit), Size(3 * unit, 3 * unit), style = Stroke(st))
    drawRect(color, Offset(0.7f * unit, 4.7f * unit), Size(1.6f * unit, 1.6f * unit))
    // data dots
    val dots = listOf(3.5f to 3.5f, 4.5f to 4.5f, 5.5f to 5.5f, 4f to 5.5f, 5.5f to 4f, 3.5f to 5f)
    dots.forEach { (dx, dy) ->
        drawRect(color.copy(0.7f), Offset(dx * unit, dy * unit), Size(unit * 0.7f, unit * 0.7f))
    }
}

/** ID card icon */
private fun DrawScope.drawIdCardIcon(s: Size, color: Color) {
    val w = s.width; val h = s.height
    val st = 1.8f.dp.toPx()
    val r = 3.dp.toPx()
    // card outline
    val card = Path().apply {
        moveTo(r, 0f); lineTo(w - r, 0f); quadraticTo(w, 0f, w, r)
        lineTo(w, h - r); quadraticTo(w, h, w - r, h)
        lineTo(r, h); quadraticTo(0f, h, 0f, h - r)
        lineTo(0f, r); quadraticTo(0f, 0f, r, 0f); close()
    }
    drawPath(card, color, style = Stroke(st, cap = StrokeCap.Round))
    // avatar circle
    drawCircle(color, w * 0.12f, Offset(w * 0.25f, h * 0.42f), style = Stroke(st * 0.8f))
    // text lines
    for (i in 0..2) {
        val ly = h * 0.28f + i * h * 0.2f
        drawLine(color.copy(0.6f), Offset(w * 0.48f, ly), Offset(w * (if (i == 2) 0.7f else 0.88f), ly), st * 0.7f, StrokeCap.Round)
    }
    // chip rectangle
    drawRect(color.copy(0.4f), Offset(w * 0.12f, h * 0.7f), Size(w * 0.2f, h * 0.18f), style = Stroke(st * 0.6f))
}

/** Particle field — slightly different distribution from splash 1 */
private fun DrawScope.drawFeatureParticles(phase: Float, canvasSize: Size) {
    data class P(val xR: Float, val yR: Float, val rad: Float, val sp: Float, val orb: Float)
    val ps = listOf(
        P(0.08f, 0.15f, 1.8f, 1.2f, 10f), P(0.92f, 0.2f, 1.3f, 0.9f, 7f),
        P(0.05f, 0.55f, 2.0f, 0.6f, 13f), P(0.95f, 0.65f, 1.5f, 1.1f, 9f),
        P(0.5f, 0.05f, 1.0f, 1.5f, 5f),  P(0.3f, 0.95f, 2.2f, 0.5f, 12f),
        P(0.75f, 0.85f, 1.4f, 1.3f, 6f), P(0.18f, 0.78f, 1.6f, 0.8f, 11f),
    )
    ps.forEach { p ->
        val cx = canvasSize.width * p.xR + p.orb * cos(phase * p.sp).dp.toPx()
        val cy = canvasSize.height * p.yR + p.orb * sin(phase * p.sp * 0.7f).dp.toPx()
        drawCircle(AccentCyan.copy(alpha = 0.12f), p.rad.dp.toPx() * 3f, Offset(cx, cy))
        drawCircle(Color.White.copy(alpha = 0.6f), p.rad.dp.toPx(), Offset(cx, cy))
    }
}
