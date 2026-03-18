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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
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

// --- Palette ---
private val DeepNavy = Color(0xFF0A0E27)
private val MidBlue = Color(0xFF1B2559)
private val BrandBlue = Color(0xFF4D6EF5)
private val LightBlue = Color(0xFF8FA3F8)
private val AccentCyan = Color(0xFF00D4FF)
private val SubtleWhite = Color(0xB3FFFFFF) // 70 %
private val GhostWhite = Color(0x33FFFFFF) // 20 %

@Composable
fun SplashScreen(onGetStarted: () -> Unit) {

    // ---------- entrance animatables ----------
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(40f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val scanLineAlpha = remember { Animatable(0f) }
    val cornerAlpha = remember { Animatable(0f) }
    val particleAlpha = remember { Animatable(0f) }
    val buttonAlpha = remember { Animatable(0f) }
    val buttonOffsetY = remember { Animatable(60f) }

    LaunchedEffect(Unit) {
        // staggered entrance — feels crafted, not formulaic
        launch {
            logoScale.animateTo(1.1f, tween(600, easing = CubicBezierEasing(.2f, .8f, .2f, 1.2f)))
            logoScale.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        }
        launch { logoAlpha.animateTo(1f, tween(500)) }
        delay(300)
        launch { cornerAlpha.animateTo(1f, tween(500)) }
        delay(200)
        launch {
            titleAlpha.animateTo(1f, tween(600))
        }
        launch {
            titleOffsetY.animateTo(0f, tween(600, easing = FastOutSlowInEasing))
        }
        delay(200)
        launch { subtitleAlpha.animateTo(1f, tween(500)) }
        launch { scanLineAlpha.animateTo(1f, tween(400)) }
        delay(100)
        launch { particleAlpha.animateTo(1f, tween(600)) }
        delay(300)
        launch { buttonAlpha.animateTo(1f, tween(500)) }
        launch { buttonOffsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
    }

    // ---------- infinite loops ----------
    val inf = rememberInfiniteTransition(label = "splash")

    // scanning line sweeps up-down
    val scanY by inf.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Reverse),
        label = "scanY"
    )

    // gentle rotation for outer ring
    val ringRotation by inf.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = "ring"
    )

    // pulse for glow
    val glowPulse by inf.animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )

    // floating particles phase
    val particlePhase by inf.animateFloat(
        initialValue = 0f, targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "particles"
    )

    // ---------- UI ----------
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(DeepNavy, MidBlue, DeepNavy))
            )
    ) {
        // --- floating particles layer ---
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .alpha(particleAlpha.value)
        ) {
            drawParticles(particlePhase, size)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // --- logo assembly ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(180.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            ) {
                // outer rotating dashed ring
                Canvas(
                    modifier = Modifier
                        .size(180.dp)
                        .alpha(cornerAlpha.value)
                ) {
                    rotate(ringRotation) {
                        drawDashedCircle(center, size.minDimension / 2f - 4.dp.toPx())
                    }
                }

                // glow behind icon
                Canvas(modifier = Modifier.size(120.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(
                                BrandBlue.copy(alpha = 0.5f * glowPulse),
                                Color.Transparent
                            )
                        ),
                        radius = size.minDimension / 2f
                    )
                }

                // rounded-rect "scanner window" with corner brackets
                Canvas(
                    modifier = Modifier
                        .size(100.dp)
                        .alpha(cornerAlpha.value)
                ) {
                    drawScannerCorners(size, BrandBlue)
                }

                // scan line
                Canvas(
                    modifier = Modifier
                        .size(100.dp)
                        .alpha(scanLineAlpha.value * 0.9f)
                ) {
                    val lineY = size.height * 0.15f + size.height * 0.7f * scanY
                    drawLine(
                        brush = Brush.horizontalGradient(
                            listOf(Color.Transparent, AccentCyan, Color.Transparent)
                        ),
                        start = Offset(size.width * 0.1f, lineY),
                        end = Offset(size.width * 0.9f, lineY),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    // glow around line
                    drawLine(
                        brush = Brush.horizontalGradient(
                            listOf(Color.Transparent, AccentCyan.copy(alpha = 0.3f), Color.Transparent)
                        ),
                        start = Offset(size.width * 0.05f, lineY),
                        end = Offset(size.width * 0.95f, lineY),
                        strokeWidth = 8.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                // document icon drawn via Canvas (no material icon dependency)
                Canvas(modifier = Modifier.size(44.dp)) {
                    drawDocumentIcon(size, Color.White)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // --- title ---
            Text(
                text = "ProScan",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffsetY.value.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- subtitle ---
            Text(
                text = "Smart Document Scanner",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = SubtleWhite,
                letterSpacing = 3.sp,
                modifier = Modifier.alpha(subtitleAlpha.value)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- animated tagline bar ---
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(3.dp)
                    .alpha(subtitleAlpha.value)
                    .background(
                        Brush.horizontalGradient(listOf(Color.Transparent, AccentCyan, Color.Transparent)),
                        RoundedCornerShape(50)
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- feature pills ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(buttonAlpha.value)
                    .offset(y = (buttonOffsetY.value * 0.5f).dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeaturePill("Scan")
                FeaturePill("QR Code")
                FeaturePill("ID Card")
            }

            Spacer(modifier = Modifier.height(28.dp))

            // --- CTA button ---
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .alpha(buttonAlpha.value)
                    .offset(y = buttonOffsetY.value.dp)
                    .drawBehind {
                        // button glow
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                listOf(BrandBlue.copy(alpha = 0.4f * glowPulse), AccentCyan.copy(alpha = 0.3f * glowPulse))
                            ),
                            cornerRadius = CornerRadius(30.dp.toPx()),
                            size = Size(size.width + 8.dp.toPx(), size.height + 8.dp.toPx()),
                            topLeft = Offset(-4.dp.toPx(), -4.dp.toPx())
                        )
                    },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandBlue
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ====================================================================
// Helper composables & draw functions
// ====================================================================

@Composable
private fun FeaturePill(label: String) {
    Box(
        modifier = Modifier
            .background(GhostWhite, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

/** Draws 4 corner bracket shapes like a scanner viewfinder. */
private fun DrawScope.drawScannerCorners(canvasSize: Size, color: Color) {
    val stroke = 3.dp.toPx()
    val len = canvasSize.width * 0.28f
    val r = 10.dp.toPx()
    val pad = 2.dp.toPx()

    data class Corner(val x: Float, val y: Float, val dx: Int, val dy: Int)

    val corners = listOf(
        Corner(pad, pad, 1, 1),
        Corner(canvasSize.width - pad, pad, -1, 1),
        Corner(pad, canvasSize.height - pad, 1, -1),
        Corner(canvasSize.width - pad, canvasSize.height - pad, -1, -1)
    )

    corners.forEach { (cx, cy, dx, dy) ->
        val path = Path().apply {
            moveTo(cx + dx * len, cy)
            lineTo(cx + dx * r, cy)
            // small arc
            quadraticTo(cx, cy, cx, cy + dy * r)
            lineTo(cx, cy + dy * len)
        }
        drawPath(path, color, style = Stroke(stroke, cap = StrokeCap.Round))
    }
}

/** Dashed circle made of small arcs. */
private fun DrawScope.drawDashedCircle(c: Offset, radius: Float) {
    val dashCount = 36
    val gapDeg = 4f
    val dashDeg = (360f / dashCount) - gapDeg
    for (i in 0 until dashCount) {
        val startAngle = i * (dashDeg + gapDeg)
        drawArc(
            color = if (i % 3 == 0) AccentCyan.copy(alpha = 0.7f) else GhostWhite,
            startAngle = startAngle,
            sweepAngle = dashDeg,
            useCenter = false,
            topLeft = Offset(c.x - radius, c.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(1.5f.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

/** Simple document/page icon drawn procedurally. */
private fun DrawScope.drawDocumentIcon(s: Size, color: Color) {
    val w = s.width
    val h = s.height
    val fold = w * 0.3f
    val r = 3.dp.toPx()
    val stroke = 2.dp.toPx()

    // page outline with folded corner
    val page = Path().apply {
        moveTo(0f, r)
        quadraticTo(0f, 0f, r, 0f)
        lineTo(w - fold, 0f)
        lineTo(w, fold)
        lineTo(w, h - r)
        quadraticTo(w, h, w - r, h)
        lineTo(r, h)
        quadraticTo(0f, h, 0f, h - r)
        close()
    }
    drawPath(page, color, style = Stroke(stroke, cap = StrokeCap.Round))

    // fold triangle
    val foldPath = Path().apply {
        moveTo(w - fold, 0f)
        lineTo(w - fold, fold)
        lineTo(w, fold)
    }
    drawPath(foldPath, color.copy(alpha = 0.5f), style = Stroke(stroke * 0.8f, cap = StrokeCap.Round))

    // text lines
    val lineStartX = w * 0.2f
    val lineEndX = w * 0.7f
    for (i in 0..2) {
        val ly = h * 0.38f + i * h * 0.15f
        drawLine(
            color.copy(alpha = 0.6f),
            Offset(lineStartX, ly),
            Offset(if (i == 2) lineEndX * 0.7f else lineEndX, ly),
            strokeWidth = stroke * 0.7f,
            cap = StrokeCap.Round
        )
    }
}

/** Floating particle field. */
private fun DrawScope.drawParticles(phase: Float, canvasSize: Size) {
    data class Particle(val xRatio: Float, val yRatio: Float, val radius: Float, val speed: Float, val orbitRadius: Float)

    val particles = listOf(
        Particle(0.15f, 0.2f, 2f, 1.0f, 12f),
        Particle(0.85f, 0.15f, 1.5f, 1.3f, 8f),
        Particle(0.1f, 0.75f, 2.5f, 0.7f, 15f),
        Particle(0.9f, 0.8f, 1.8f, 0.9f, 10f),
        Particle(0.5f, 0.1f, 1.2f, 1.5f, 6f),
        Particle(0.3f, 0.88f, 2f, 1.1f, 9f),
        Particle(0.7f, 0.6f, 1.4f, 1.4f, 7f),
        Particle(0.2f, 0.5f, 1.8f, 0.8f, 11f),
        Particle(0.8f, 0.45f, 1.3f, 1.2f, 8f),
        Particle(0.45f, 0.92f, 2.2f, 0.6f, 14f),
        Particle(0.65f, 0.25f, 1.6f, 1.0f, 9f),
        Particle(0.35f, 0.35f, 1.1f, 1.6f, 5f),
    )

    particles.forEach { p ->
        val cx = canvasSize.width * p.xRatio + p.orbitRadius * cos(phase * p.speed).dp.toPx()
        val cy = canvasSize.height * p.yRatio + p.orbitRadius * sin(phase * p.speed * 0.7f).dp.toPx()
        // glow
        drawCircle(
            color = AccentCyan.copy(alpha = 0.15f),
            radius = p.radius.dp.toPx() * 3f,
            center = Offset(cx, cy)
        )
        // core
        drawCircle(
            color = Color.White.copy(alpha = 0.7f),
            radius = p.radius.dp.toPx(),
            center = Offset(cx, cy)
        )
    }
}
