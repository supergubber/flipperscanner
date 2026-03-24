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
private val AccentGold = Color(0xFFB8892E)
private val AccentTeal = Color(0xFF1A8F84)
private val SubtleDark = Color(0xCC190933)
private val GlassWhite = Color(0xAAFFFFFF)
private val GlassBorder = Color(0x66FFFFFF)
private val BlobPink = Color(0x40E8A0C0)
private val BlobPeach = Color(0x35F5C8A0)
private val BlobLilac = Color(0x30C0A0D8)

@Composable
fun SplashReadyScreen(onGetStarted: () -> Unit) {

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
        launch { stat1Alpha.animateTo(1f, tween(400)) }; launch { stat1OffsetY.animateTo(0f, tween(400, easing = FastOutSlowInEasing)) }
        delay(150)
        launch { stat2Alpha.animateTo(1f, tween(400)) }; launch { stat2OffsetY.animateTo(0f, tween(400, easing = FastOutSlowInEasing)) }
        delay(150)
        launch { stat3Alpha.animateTo(1f, tween(400)) }; launch { stat3OffsetY.animateTo(0f, tween(400, easing = FastOutSlowInEasing)) }
        delay(200)
        launch { promiseAlpha.animateTo(1f, tween(500)) }
        delay(200)
        launch { bottomAlpha.animateTo(1f, tween(500)) }; launch { bottomOffsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
    }

    val inf = rememberInfiniteTransition(label = "ready")
    val glowPulse by inf.animateFloat(0.4f, 1f, infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "glow")
    val ringRotation by inf.animateFloat(0f, 360f, infiniteRepeatable(tween(10000, easing = LinearEasing)), label = "ring")
    val particlePhase by inf.animateFloat(0f, (2 * PI).toFloat(), infiniteRepeatable(tween(6000, easing = LinearEasing)), label = "particles")
    val checkGlow by inf.animateFloat(0.7f, 1f, infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "checkGlow")
    val radiatePulse by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(2500, easing = LinearEasing)), label = "radiate")
    val blobDrift by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(9000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "blob")

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientTop, GradientMid, GradientBottom)))
    ) {
        // decorative blobs
        Box(modifier = Modifier.size(300.dp).offset(x = (-80).dp, y = (-50).dp).offset(y = (blobDrift * 18).dp).alpha(0.6f).blur(85.dp).background(BlobPink, CircleShape))
        Box(modifier = Modifier.size(250.dp).align(Alignment.CenterEnd).offset(x = 100.dp, y = (-60).dp).offset(y = (-blobDrift * 12).dp).alpha(0.5f).blur(75.dp).background(BlobLilac, CircleShape))
        Box(modifier = Modifier.size(320.dp).align(Alignment.BottomCenter).offset(y = 100.dp).offset(x = (blobDrift * 15).dp).alpha(0.5f).blur(90.dp).background(BlobPeach, CircleShape))

        // radial light
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.4f), Color.Transparent), center = Offset(size.width / 2f, size.height * 0.26f), radius = size.width * 0.6f),
                radius = size.width * 0.6f, center = Offset(size.width / 2f, size.height * 0.26f)
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) { drawReadyParticles(particlePhase, size) }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            // --- shield assembly ---
            Box(
                modifier = Modifier.size(200.dp).scale(shieldScale.value).alpha(shieldAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                // outer glass ring
                Box(modifier = Modifier.size(180.dp).background(Color.White.copy(alpha = 0.08f), CircleShape).border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape))
                // inner glass circle
                Box(modifier = Modifier.size(150.dp).background(GlassWhite.copy(alpha = 0.25f), CircleShape).border(1.5f.dp, GlassBorder, CircleShape))

                // pulse rings
                Canvas(modifier = Modifier.size(195.dp)) {
                    val maxR = size.minDimension / 2f
                    for (i in 0..2) {
                        val p = (radiatePulse + i * 0.33f) % 1f
                        drawCircle(DarkText.copy(alpha = (1f - p) * 0.35f), maxR * 0.5f + maxR * 0.5f * p, center, style = Stroke(2f.dp.toPx()))
                    }
                }

                // segmented ring
                Canvas(modifier = Modifier.size(170.dp)) {
                    rotate(ringRotation) { drawSegmentedRing(center, size.minDimension / 2f - 4.dp.toPx()) }
                }

                // glow
                Canvas(modifier = Modifier.size(120.dp)) {
                    drawCircle(brush = Brush.radialGradient(listOf(AccentTeal.copy(alpha = 0.25f * glowPulse), Color.Transparent)), radius = size.minDimension / 2f)
                }

                // shield
                Canvas(modifier = Modifier.size(80.dp)) { drawShield(size, DarkText) }

                // checkmark
                Canvas(modifier = Modifier.size(34.dp).scale(checkScale.value)) {
                    drawCheckmark(size, AccentTeal.copy(alpha = checkGlow))
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text("You're All Set!", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = DarkText, letterSpacing = 1.sp,
                modifier = Modifier.alpha(titleAlpha.value).offset(y = titleOffsetY.value.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("ProScan is ready to transform your\nscanning experience", fontSize = 14.sp, fontWeight = FontWeight.Medium,
                color = SubtleDark, textAlign = TextAlign.Center, lineHeight = 20.sp, letterSpacing = 0.5.sp,
                modifier = Modifier.alpha(titleAlpha.value).offset(y = titleOffsetY.value.dp))

            Spacer(modifier = Modifier.height(36.dp))

            // stat counters
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatBadge("500+", "Scan Types", AccentGold, stat1Alpha.value, stat1OffsetY.value)
                StatBadge("HD", "Quality", AccentTeal, stat2Alpha.value, stat2OffsetY.value)
                StatBadge("0.3s", "Scan Speed", BrandSalmon, stat3Alpha.value, stat3OffsetY.value)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // promise line
            Row(
                modifier = Modifier.alpha(promiseAlpha.value)
                    .drawBehind { drawRoundRect(Color.Black.copy(alpha = 0.04f), cornerRadius = CornerRadius(18.dp.toPx()), size = Size(size.width, size.height + 3.dp.toPx()), topLeft = Offset(0f, 2.dp.toPx())) }
                    .background(GlassWhite, RoundedCornerShape(16.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Canvas(modifier = Modifier.size(16.dp)) { drawCheckmark(size, AccentTeal) }
                Spacer(modifier = Modifier.width(10.dp))
                Text("100% Free  •  No Ads  •  Offline Ready", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DarkText, letterSpacing = 0.5.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.alpha(bottomAlpha.value).padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center) {
                repeat(3) { i -> Box(modifier = Modifier.padding(horizontal = 4.dp).size(if (i == 2) 24.dp else 8.dp, 8.dp)
                    .background(if (i == 2) DarkText else DarkText.copy(alpha = 0.15f), RoundedCornerShape(4.dp))) }
            }

            Box(modifier = Modifier.fillMaxWidth().height(58.dp).alpha(bottomAlpha.value).offset(y = bottomOffsetY.value.dp)
                .drawBehind { drawRoundRect(DarkText.copy(alpha = 0.18f), cornerRadius = CornerRadius(32.dp.toPx()), size = Size(size.width, size.height + 6.dp.toPx()), topLeft = Offset(0f, 5.dp.toPx())) }
            ) {
                Button(onClick = onGetStarted, modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkText)) {
                    Text("Get Started", fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun StatBadge(value: String, label: String, accent: Color, alpha: Float, offsetY: Float) {
    Column(modifier = Modifier.alpha(alpha).offset(y = offsetY.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .drawBehind { drawRoundRect(Color.Black.copy(alpha = 0.04f), cornerRadius = CornerRadius(18.dp.toPx()), size = Size(size.width, size.height + 3.dp.toPx()), topLeft = Offset(0f, 2.dp.toPx())) }
                .background(GlassWhite, RoundedCornerShape(16.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) { Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = accent) }
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = SubtleDark, letterSpacing = 0.5.sp)
    }
}

private fun DrawScope.drawShield(s: Size, color: Color) {
    val w = s.width; val h = s.height; val st = 3f.dp.toPx()
    val path = Path().apply { moveTo(w * .5f, 0f); cubicTo(w * .75f, 0f, w, h * .05f, w, h * .2f); lineTo(w, h * .55f); cubicTo(w, h * .7f, w * .7f, h * .85f, w * .5f, h); cubicTo(w * .3f, h * .85f, 0f, h * .7f, 0f, h * .55f); lineTo(0f, h * .2f); cubicTo(0f, h * .05f, w * .25f, 0f, w * .5f, 0f); close() }
    drawPath(path, color.copy(alpha = 0.1f)); drawPath(path, color, style = Stroke(st, cap = StrokeCap.Round))
}

private fun DrawScope.drawCheckmark(s: Size, color: Color) {
    val path = Path().apply { moveTo(s.width * .18f, s.height * .52f); lineTo(s.width * .42f, s.height * .76f); lineTo(s.width * .82f, s.height * .26f) }
    drawPath(path, color, style = Stroke(3.5f.dp.toPx(), cap = StrokeCap.Round))
}

private fun DrawScope.drawSegmentedRing(c: Offset, radius: Float) {
    val n = 28; val gap = 4f; val arc = (360f / n) - gap
    for (i in 0 until n) {
        val p = i % 4 == 0; val s = i % 3 == 0; val a = if (p) 0.5f else if (s) 0.25f else 0.08f
        val col = if (p) DarkText else if (s) AccentTeal else DarkText
        drawArc(col.copy(alpha = a), i * (arc + gap), arc, false, Offset(c.x - radius, c.y - radius), Size(radius * 2, radius * 2),
            style = Stroke(if (p) 2.5f.dp.toPx() else 1.5f.dp.toPx(), cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawReadyParticles(phase: Float, cs: Size) {
    data class P(val x: Float, val y: Float, val r: Float, val s: Float, val o: Float)
    listOf(P(.12f,.12f,1.8f,1f,9f),P(.88f,.18f,1.4f,1.4f,6f),P(.06f,.6f,2.2f,.7f,12f),P(.94f,.7f,1.9f,.9f,10f),P(.45f,.04f,1.3f,1.6f,5f),P(.55f,.96f,2.2f,.5f,13f),P(.25f,.85f,1.7f,1.1f,8f),P(.78f,.5f,1.5f,1.3f,7f),P(.35f,.42f,1.2f,1.5f,4f),P(.68f,.88f,2f,.8f,11f))
        .forEach { p -> val cx = cs.width * p.x + p.o * cos(phase * p.s).dp.toPx(); val cy = cs.height * p.y + p.o * sin(phase * p.s * .7f).dp.toPx()
            drawCircle(Color.White.copy(alpha = .4f), p.r.dp.toPx() * 3.5f, Offset(cx, cy)); drawCircle(Color.White, p.r.dp.toPx(), Offset(cx, cy)) }
}
