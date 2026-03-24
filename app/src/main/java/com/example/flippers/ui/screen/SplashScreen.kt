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
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.blur
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

private val GradientTop = Color(0xFFC49AB5)
private val GradientMid = Color(0xFFE4AE9C)
private val GradientBottom = Color(0xFFF2D0B4)
private val DarkText = Color(0xFF190933)
private val BrandSalmon = Color(0xFFC75B4A)
private val SubtleDark = Color(0xCC190933)
private val GlassWhite = Color(0xAAFFFFFF)
private val GlassBorder = Color(0x66FFFFFF)
private val BlobPink = Color(0x40E8A0C0)
private val BlobPeach = Color(0x35F5C8A0)
private val BlobLilac = Color(0x30C0A0D8)

@Composable
fun SplashScreen(onNext: () -> Unit) {

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
        launch {
            logoScale.animateTo(1.1f, tween(600, easing = CubicBezierEasing(.2f, .8f, .2f, 1.2f)))
            logoScale.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        }
        launch { logoAlpha.animateTo(1f, tween(500)) }
        delay(300)
        launch { cornerAlpha.animateTo(1f, tween(500)) }
        delay(200)
        launch { titleAlpha.animateTo(1f, tween(600)) }
        launch { titleOffsetY.animateTo(0f, tween(600, easing = FastOutSlowInEasing)) }
        delay(200)
        launch { subtitleAlpha.animateTo(1f, tween(500)) }
        launch { scanLineAlpha.animateTo(1f, tween(400)) }
        delay(100)
        launch { particleAlpha.animateTo(1f, tween(600)) }
        delay(300)
        launch { buttonAlpha.animateTo(1f, tween(500)) }
        launch { buttonOffsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
    }

    val inf = rememberInfiniteTransition(label = "splash")
    val scanY by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Reverse), label = "scanY")
    val ringRotation by inf.animateFloat(0f, 360f, infiniteRepeatable(tween(12000, easing = LinearEasing)), label = "ring")
    val glowPulse by inf.animateFloat(0.4f, 1f, infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "glow")
    val particlePhase by inf.animateFloat(0f, (2 * PI).toFloat(), infiniteRepeatable(tween(6000, easing = LinearEasing)), label = "particles")
    val blobDrift by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "blob")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientTop, GradientMid, GradientBottom)))
    ) {
        // --- decorative blobs for depth ---
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-60).dp, y = (-40).dp)
                .offset(y = (blobDrift * 15).dp)
                .alpha(0.7f)
                .blur(80.dp)
                .background(BlobPink, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = 120.dp)
                .offset(y = (-blobDrift * 10).dp)
                .alpha(0.6f)
                .blur(70.dp)
                .background(BlobLilac, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 80.dp)
                .offset(x = (blobDrift * 12).dp)
                .alpha(0.5f)
                .blur(90.dp)
                .background(BlobPeach, CircleShape)
        )

        // radial light behind logo
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color.White.copy(alpha = 0.4f), Color.Transparent),
                    center = Offset(size.width / 2f, size.height * 0.34f),
                    radius = size.width * 0.65f
                ),
                radius = size.width * 0.65f,
                center = Offset(size.width / 2f, size.height * 0.34f)
            )
        }

        // floating particles
        Canvas(modifier = Modifier.fillMaxSize().alpha(particleAlpha.value)) {
            drawParticles(particlePhase, size)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // --- logo assembly ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(210.dp).scale(logoScale.value).alpha(logoAlpha.value)
            ) {
                // outer glass ring
                Box(
                    modifier = Modifier
                        .size(185.dp)
                        .background(Color.White.copy(alpha = 0.08f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                )
                // inner glass circle
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(GlassWhite.copy(alpha = 0.3f), CircleShape)
                        .border(1.5f.dp, GlassBorder, CircleShape)
                )

                // rotating dashed ring
                Canvas(modifier = Modifier.size(195.dp).alpha(cornerAlpha.value)) {
                    rotate(ringRotation) { drawDashedCircle(center, size.minDimension / 2f - 4.dp.toPx()) }
                }

                // glow behind icon
                Canvas(modifier = Modifier.size(130.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(listOf(BrandSalmon.copy(alpha = 0.3f * glowPulse), Color.Transparent)),
                        radius = size.minDimension / 2f
                    )
                }

                // scanner corners
                Canvas(modifier = Modifier.size(100.dp).alpha(cornerAlpha.value)) {
                    drawScannerCorners(size, DarkText)
                }

                // scan line
                Canvas(modifier = Modifier.size(100.dp).alpha(scanLineAlpha.value)) {
                    val lineY = size.height * 0.15f + size.height * 0.7f * scanY
                    drawLine(
                        brush = Brush.horizontalGradient(listOf(Color.Transparent, BrandSalmon, Color.Transparent)),
                        start = Offset(size.width * 0.08f, lineY), end = Offset(size.width * 0.92f, lineY),
                        strokeWidth = 2.5f.dp.toPx(), cap = StrokeCap.Round
                    )
                    drawLine(
                        brush = Brush.horizontalGradient(listOf(Color.Transparent, BrandSalmon.copy(alpha = 0.2f), Color.Transparent)),
                        start = Offset(0f, lineY), end = Offset(size.width, lineY),
                        strokeWidth = 16.dp.toPx(), cap = StrokeCap.Round
                    )
                }

                // document icon
                Canvas(modifier = Modifier.size(44.dp)) { drawDocumentIcon(size, DarkText) }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "ProScan",
                fontSize = 46.sp, fontWeight = FontWeight.ExtraBold, color = DarkText,
                letterSpacing = 3.sp,
                modifier = Modifier.alpha(titleAlpha.value).offset(y = titleOffsetY.value.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Smart Document Scanner",
                fontSize = 15.sp, fontWeight = FontWeight.Medium, color = SubtleDark,
                letterSpacing = 3.sp,
                modifier = Modifier.alpha(subtitleAlpha.value)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .width(56.dp).height(3.dp)
                    .alpha(subtitleAlpha.value)
                    .background(
                        Brush.horizontalGradient(listOf(BrandSalmon.copy(alpha = 0.2f), BrandSalmon, BrandSalmon.copy(alpha = 0.2f))),
                        RoundedCornerShape(50)
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            // feature pills
            Row(
                modifier = Modifier.fillMaxWidth().alpha(buttonAlpha.value).offset(y = (buttonOffsetY.value * 0.5f).dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeaturePill("Scan"); FeaturePill("QR Code"); FeaturePill("ID Card")
            }

            Spacer(modifier = Modifier.height(28.dp))

            // page indicator
            Row(
                modifier = Modifier.alpha(buttonAlpha.value).padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { i ->
                    Box(modifier = Modifier.padding(horizontal = 4.dp)
                        .size(if (i == 0) 24.dp else 8.dp, 8.dp)
                        .background(if (i == 0) DarkText else DarkText.copy(alpha = 0.15f), RoundedCornerShape(4.dp)))
                }
            }

            // CTA button
            Box(
                modifier = Modifier.fillMaxWidth().height(58.dp)
                    .alpha(buttonAlpha.value).offset(y = buttonOffsetY.value.dp)
                    .drawBehind {
                        drawRoundRect(DarkText.copy(alpha = 0.18f), cornerRadius = CornerRadius(32.dp.toPx()),
                            size = Size(size.width, size.height + 6.dp.toPx()), topLeft = Offset(0f, 5.dp.toPx()))
                    }
            ) {
                Button(
                    onClick = onNext, modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkText)
                ) {
                    Text("Next", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun FeaturePill(label: String) {
    Box(
        modifier = Modifier
            .background(GlassWhite, RoundedCornerShape(24.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
            .padding(horizontal = 18.dp, vertical = 9.dp)
    ) {
        Text(label, color = DarkText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    }
}

private fun DrawScope.drawScannerCorners(canvasSize: Size, color: Color) {
    val stroke = 3.5f.dp.toPx(); val len = canvasSize.width * 0.3f; val r = 12.dp.toPx(); val pad = 2.dp.toPx()
    data class C(val x: Float, val y: Float, val dx: Int, val dy: Int)
    listOf(C(pad, pad, 1, 1), C(canvasSize.width - pad, pad, -1, 1), C(pad, canvasSize.height - pad, 1, -1), C(canvasSize.width - pad, canvasSize.height - pad, -1, -1))
        .forEach { (cx, cy, dx, dy) ->
            val path = Path().apply { moveTo(cx + dx * len, cy); lineTo(cx + dx * r, cy); quadraticTo(cx, cy, cx, cy + dy * r); lineTo(cx, cy + dy * len) }
            drawPath(path, color, style = Stroke(stroke, cap = StrokeCap.Round))
        }
}

private fun DrawScope.drawDashedCircle(c: Offset, radius: Float) {
    val n = 40; val gap = 3.5f; val dash = (360f / n) - gap
    for (i in 0 until n) {
        val a = i % 5 == 0
        drawArc(if (a) DarkText.copy(alpha = 0.5f) else DarkText.copy(alpha = 0.1f), i * (dash + gap), dash, false,
            Offset(c.x - radius, c.y - radius), Size(radius * 2, radius * 2),
            style = Stroke(if (a) 2.5f.dp.toPx() else 1.5f.dp.toPx(), cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawDocumentIcon(s: Size, color: Color) {
    val w = s.width; val h = s.height; val fold = w * 0.3f; val r = 3.dp.toPx(); val st = 2.5f.dp.toPx()
    val page = Path().apply { moveTo(0f, r); quadraticTo(0f, 0f, r, 0f); lineTo(w - fold, 0f); lineTo(w, fold); lineTo(w, h - r); quadraticTo(w, h, w - r, h); lineTo(r, h); quadraticTo(0f, h, 0f, h - r); close() }
    drawPath(page, color, style = Stroke(st, cap = StrokeCap.Round))
    val fp = Path().apply { moveTo(w - fold, 0f); lineTo(w - fold, fold); lineTo(w, fold) }
    drawPath(fp, color.copy(alpha = 0.4f), style = Stroke(st * 0.8f, cap = StrokeCap.Round))
    for (i in 0..2) { val ly = h * 0.38f + i * h * 0.15f; drawLine(color.copy(alpha = 0.5f), Offset(w * 0.2f, ly), Offset(if (i == 2) w * 0.45f else w * 0.7f, ly), st * 0.6f, StrokeCap.Round) }
}

private fun DrawScope.drawParticles(phase: Float, cs: Size) {
    data class P(val x: Float, val y: Float, val r: Float, val s: Float, val o: Float)
    listOf(P(.15f,.2f,2.2f,1f,12f),P(.85f,.15f,1.6f,1.3f,8f),P(.1f,.75f,2.8f,.7f,15f),P(.9f,.8f,2f,.9f,10f),P(.5f,.08f,1.4f,1.5f,6f),P(.3f,.88f,2.2f,1.1f,9f),P(.7f,.6f,1.6f,1.4f,7f),P(.2f,.5f,2f,.8f,11f),P(.8f,.45f,1.5f,1.2f,8f),P(.45f,.92f,2.4f,.6f,14f),P(.65f,.25f,1.8f,1f,9f),P(.35f,.35f,1.2f,1.6f,5f))
        .forEach { p ->
            val cx = cs.width * p.x + p.o * cos(phase * p.s).dp.toPx()
            val cy = cs.height * p.y + p.o * sin(phase * p.s * .7f).dp.toPx()
            drawCircle(Color.White.copy(alpha = 0.4f), p.r.dp.toPx() * 3.5f, Offset(cx, cy))
            drawCircle(Color.White, p.r.dp.toPx(), Offset(cx, cy))
        }
}
