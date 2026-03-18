package com.example.flippers.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
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
import androidx.compose.ui.draw.scale
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

private val DeepNavy = Color(0xFF0A0E27)
private val MidBlue = Color(0xFF1B2559)
private val BrandBlue = Color(0xFF4D6EF5)
private val AccentCyan = Color(0xFF00D4FF)
private val AccentGreen = Color(0xFF00E676)
private val SubtleWhite = Color(0xB3FFFFFF)
private val GhostWhite = Color(0x33FFFFFF)

@Composable
fun SplashReadyScreen(onGetStarted: () -> Unit) {

    // ---- entrance ----
    val shieldScale = remember { Animatable(0f) }
    val shieldAlpha = remember { Animatable(0f) }
    val checkScale = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(30f) }
    val stat1Alpha = remember { Animatable(0f) }
    val stat1OffsetY = remember { Animatable(40f) }
    val stat2Alpha = remember { Animatable(0f) }
    val stat2OffsetY = remember { Animatable(40f) }
    val stat3Alpha = remember { Animatable(0f) }
    val stat3OffsetY = remember { Animatable(40f) }
    val promiseAlpha = remember { Animatable(0f) }
    val bottomAlpha = remember { Animatable(0f) }
    val bottomOffsetY = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        launch {
            shieldScale.animateTo(1.15f, tween(500, easing = CubicBezierEasing(.2f, .8f, .2f, 1.2f)))
            shieldScale.animateTo(1f, tween(250, easing = FastOutSlowInEasing))
        }
        launch { shieldAlpha.animateTo(1f, tween(400)) }
        delay(400)
        launch {
            checkScale.animateTo(1.3f, tween(300, easing = CubicBezierEasing(.2f, .8f, .2f, 1.4f)))
            checkScale.animateTo(1f, tween(200, easing = FastOutSlowInEasing))
        }
        delay(200)
        launch { titleAlpha.animateTo(1f, tween(500)) }
        launch { titleOffsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
        delay(250)
        launch { stat1Alpha.animateTo(1f, tween(400)) }
        launch { stat1OffsetY.animateTo(0f, tween(400, easing = FastOutSlowInEasing)) }
        delay(150)
        launch { stat2Alpha.animateTo(1f, tween(400)) }
        launch { stat2OffsetY.animateTo(0f, tween(400, easing = FastOutSlowInEasing)) }
        delay(150)
        launch { stat3Alpha.animateTo(1f, tween(400)) }
        launch { stat3OffsetY.animateTo(0f, tween(400, easing = FastOutSlowInEasing)) }
        delay(200)
        launch { promiseAlpha.animateTo(1f, tween(500)) }
        delay(200)
        launch { bottomAlpha.animateTo(1f, tween(500)) }
        launch { bottomOffsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
    }

    // ---- infinite ----
    val inf = rememberInfiniteTransition(label = "ready")
    val glowPulse by inf.animateFloat(
        0.4f, 1f,
        infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val ringRotation by inf.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(10000, easing = LinearEasing)),
        label = "ring"
    )
    val particlePhase by inf.animateFloat(
        0f, (2 * PI).toFloat(),
        infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "particles"
    )
    val checkGlow by inf.animateFloat(
        0.6f, 1f,
        infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "checkGlow"
    )
    val radiatePulse by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2500, easing = LinearEasing)),
        label = "radiate"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DeepNavy, MidBlue, DeepNavy)))
    ) {
        // particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawReadyParticles(particlePhase, size)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            // ---- shield assembly ----
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(shieldScale.value)
                    .alpha(shieldAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                // radiating pulse rings
                Canvas(modifier = Modifier.size(180.dp)) {
                    val maxR = size.minDimension / 2f
                    for (i in 0..2) {
                        val phase = (radiatePulse + i * 0.33f) % 1f
                        val r = maxR * 0.5f + maxR * 0.5f * phase
                        val a = (1f - phase) * 0.3f
                        drawCircle(AccentGreen.copy(alpha = a), r, center, style = Stroke(1.5f.dp.toPx()))
                    }
                }

                // rotating segmented ring
                Canvas(modifier = Modifier.size(160.dp)) {
                    rotate(ringRotation) {
                        drawSegmentedRing(center, size.minDimension / 2f - 4.dp.toPx())
                    }
                }

                // glow
                Canvas(modifier = Modifier.size(110.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(AccentGreen.copy(alpha = 0.3f * glowPulse), Color.Transparent)
                        ),
                        radius = size.minDimension / 2f
                    )
                }

                // shield shape
                Canvas(modifier = Modifier.size(80.dp)) {
                    drawShield(size, BrandBlue)
                }

                // checkmark
                Canvas(
                    modifier = Modifier
                        .size(34.dp)
                        .scale(checkScale.value)
                ) {
                    drawCheckmark(size, AccentGreen.copy(alpha = checkGlow))
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ---- title ----
            Text(
                text = "You're All Set!",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffsetY.value.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ProScan is ready to transform your\nscanning experience",
                fontSize = 14.sp,
                color = SubtleWhite,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                letterSpacing = 0.5.sp,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffsetY.value.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ---- stat counters ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBadge("500+", "Scan Types", AccentCyan, stat1Alpha.value, stat1OffsetY.value)
                StatBadge("HD", "Quality", AccentGreen, stat2Alpha.value, stat2OffsetY.value)
                StatBadge("0.3s", "Scan Speed", BrandBlue, stat3Alpha.value, stat3OffsetY.value)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ---- promise line ----
            Row(
                modifier = Modifier
                    .alpha(promiseAlpha.value)
                    .background(GhostWhite, RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Canvas(modifier = Modifier.size(16.dp)) {
                    drawCheckmark(size, AccentGreen)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "100% Free  •  No Ads  •  Offline Ready",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    letterSpacing = 0.5.sp
                )
            }

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
                            .size(if (i == 2) 24.dp else 8.dp, 8.dp)
                            .background(
                                if (i == 2) AccentCyan else GhostWhite,
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            // ---- Get Started ----
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .alpha(bottomAlpha.value)
                    .offset(y = bottomOffsetY.value.dp)
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    AccentGreen.copy(alpha = 0.35f * glowPulse),
                                    AccentCyan.copy(alpha = 0.25f * glowPulse)
                                )
                            ),
                            cornerRadius = CornerRadius(30.dp.toPx()),
                            size = Size(size.width + 10.dp.toPx(), size.height + 10.dp.toPx()),
                            topLeft = Offset(-5.dp.toPx(), -5.dp.toPx())
                        )
                    },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Brush.horizontalGradient(
                        listOf(BrandBlue, AccentCyan)
                    ).let { BrandBlue } // solid fallback, glow does gradient
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// =====================================================================
// Composables
// =====================================================================

@Composable
private fun StatBadge(
    value: String,
    label: String,
    accent: Color,
    alpha: Float,
    offsetY: Float
) {
    Column(
        modifier = Modifier
            .alpha(alpha)
            .offset(y = offsetY.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(accent.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                .padding(horizontal = 18.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accent
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = SubtleWhite,
            letterSpacing = 0.5.sp
        )
    }
}

// =====================================================================
// Draw helpers
// =====================================================================

private fun DrawScope.drawShield(s: Size, color: Color) {
    val w = s.width; val h = s.height
    val st = 2.5f.dp.toPx()
    val path = Path().apply {
        moveTo(w * 0.5f, 0f)
        // top-right
        cubicTo(w * 0.75f, 0f, w, h * 0.05f, w, h * 0.2f)
        // right side
        lineTo(w, h * 0.55f)
        // bottom-right curve to point
        cubicTo(w, h * 0.7f, w * 0.7f, h * 0.85f, w * 0.5f, h)
        // bottom-left curve from point
        cubicTo(w * 0.3f, h * 0.85f, 0f, h * 0.7f, 0f, h * 0.55f)
        // left side
        lineTo(0f, h * 0.2f)
        // top-left
        cubicTo(0f, h * 0.05f, w * 0.25f, 0f, w * 0.5f, 0f)
        close()
    }
    drawPath(path, color.copy(alpha = 0.15f))
    drawPath(path, color, style = Stroke(st, cap = StrokeCap.Round))
}

private fun DrawScope.drawCheckmark(s: Size, color: Color) {
    val st = 3.dp.toPx()
    val path = Path().apply {
        moveTo(s.width * 0.18f, s.height * 0.52f)
        lineTo(s.width * 0.42f, s.height * 0.76f)
        lineTo(s.width * 0.82f, s.height * 0.26f)
    }
    drawPath(path, color, style = Stroke(st, cap = StrokeCap.Round))
}

/** Segmented ring with varying opacity */
private fun DrawScope.drawSegmentedRing(c: Offset, radius: Float) {
    val segments = 24
    val gap = 5f
    val arc = (360f / segments) - gap
    for (i in 0 until segments) {
        val startAngle = i * (arc + gap)
        val alpha = if (i % 4 == 0) 0.6f else if (i % 2 == 0) 0.3f else 0.15f
        val col = if (i % 4 == 0) AccentCyan else if (i % 3 == 0) AccentGreen else GhostWhite
        drawArc(
            col.copy(alpha = alpha),
            startAngle, arc, false,
            Offset(c.x - radius, c.y - radius),
            Size(radius * 2, radius * 2),
            style = Stroke(1.5f.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

private fun DrawScope.drawReadyParticles(phase: Float, canvasSize: Size) {
    data class P(val xR: Float, val yR: Float, val rad: Float, val sp: Float, val orb: Float)
    val ps = listOf(
        P(0.12f, 0.12f, 1.6f, 1.0f, 9f), P(0.88f, 0.18f, 1.2f, 1.4f, 6f),
        P(0.06f, 0.6f, 2.0f, 0.7f, 12f), P(0.94f, 0.7f, 1.7f, 0.9f, 10f),
        P(0.45f, 0.04f, 1.1f, 1.6f, 5f), P(0.55f, 0.96f, 2.0f, 0.5f, 13f),
        P(0.25f, 0.85f, 1.5f, 1.1f, 8f), P(0.78f, 0.5f, 1.3f, 1.3f, 7f),
        P(0.35f, 0.42f, 1.0f, 1.5f, 4f), P(0.68f, 0.88f, 1.8f, 0.8f, 11f),
    )
    ps.forEach { p ->
        val cx = canvasSize.width * p.xR + p.orb * cos(phase * p.sp).dp.toPx()
        val cy = canvasSize.height * p.yR + p.orb * sin(phase * p.sp * 0.7f).dp.toPx()
        drawCircle(AccentGreen.copy(alpha = 0.1f), p.rad.dp.toPx() * 3f, Offset(cx, cy))
        drawCircle(Color.White.copy(alpha = 0.6f), p.rad.dp.toPx(), Offset(cx, cy))
    }
}
