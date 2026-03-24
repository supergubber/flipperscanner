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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
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
private val AccentTeal = Color(0xFF1A8F84)
private val AccentGold = Color(0xFFB8892E)
private val SubtleDark = Color(0xCC190933)
private val GlassWhite = Color(0xAAFFFFFF)
private val GlassBorder = Color(0x66FFFFFF)
private val BlobPink = Color(0x40E8A0C0)
private val BlobPeach = Color(0x35F5C8A0)
private val BlobLilac = Color(0x30C0A0D8)

@Composable
fun SplashFeaturesScreen(onNext: () -> Unit) {

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

    val inf = rememberInfiniteTransition(label = "feat")
    val glowPulse by inf.animateFloat(0.4f, 1f, infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "glow")
    val particlePhase by inf.animateFloat(0f, (2 * PI).toFloat(), infiniteRepeatable(tween(6000, easing = LinearEasing)), label = "particles")
    val iconFloat by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(3000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "float")
    val orbitAngle by inf.animateFloat(0f, 360f, infiniteRepeatable(tween(8000, easing = LinearEasing)), label = "orbit")
    val blobDrift by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(7000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "blob")

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientTop, GradientMid, GradientBottom)))
    ) {
        // decorative blobs
        Box(modifier = Modifier.size(260.dp).offset(x = (-70).dp, y = 60.dp).offset(y = (blobDrift * 12).dp).alpha(0.6f).blur(75.dp).background(BlobLilac, CircleShape))
        Box(modifier = Modifier.size(220.dp).align(Alignment.TopEnd).offset(x = 60.dp, y = (-30).dp).offset(y = (-blobDrift * 8).dp).alpha(0.5f).blur(65.dp).background(BlobPink, CircleShape))
        Box(modifier = Modifier.size(280.dp).align(Alignment.BottomStart).offset(x = (-40).dp, y = 60.dp).offset(x = (blobDrift * 10).dp).alpha(0.5f).blur(85.dp).background(BlobPeach, CircleShape))

        // radial light
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.3f), Color.Transparent), center = Offset(size.width / 2f, size.height * 0.12f), radius = size.width * 0.55f),
                radius = size.width * 0.55f, center = Offset(size.width / 2f, size.height * 0.12f)
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) { drawFeatureParticles(particlePhase, size) }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))

            Text("Powerful Features", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = DarkText, letterSpacing = 1.sp,
                modifier = Modifier.alpha(headerAlpha.value).offset(y = headerOffsetY.value.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text("Everything you need in one app", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = SubtleDark, letterSpacing = 2.sp,
                modifier = Modifier.alpha(headerAlpha.value).offset(y = headerOffsetY.value.dp))
            Box(modifier = Modifier.padding(top = 12.dp).width(48.dp).height(3.dp).alpha(headerAlpha.value)
                .background(Brush.horizontalGradient(listOf(BrandSalmon.copy(alpha = 0.2f), BrandSalmon, BrandSalmon.copy(alpha = 0.2f))), RoundedCornerShape(50)))

            Spacer(modifier = Modifier.height(36.dp))

            FeatureCard(card1Alpha.value, card1OffsetX.value, iconFloat, glowPulse, orbitAngle, BrandSalmon, "Document Scanner",
                "Capture crystal-clear scans of documents, receipts, whiteboards and books with auto-edge detection") { s, c -> drawDocScanIcon(s, c) }
            Spacer(modifier = Modifier.height(14.dp))
            FeatureCard(card2Alpha.value, card2OffsetX.value, iconFloat, glowPulse, -orbitAngle, AccentTeal, "QR & Barcode Reader",
                "Instantly decode any QR code or barcode — results appear in real time with one tap copy") { s, c -> drawQrIcon(s, c) }
            Spacer(modifier = Modifier.height(14.dp))
            FeatureCard(card3Alpha.value, card3OffsetX.value, iconFloat, glowPulse, orbitAngle * 0.7f, AccentGold, "ID Card Scanner",
                "Digitize ID cards, driver licenses and passports with smart card-edge framing") { s, c -> drawIdCardIcon(s, c) }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.alpha(bottomAlpha.value).padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center) {
                repeat(3) { i -> Box(modifier = Modifier.padding(horizontal = 4.dp).size(if (i == 1) 24.dp else 8.dp, 8.dp)
                    .background(if (i == 1) DarkText else DarkText.copy(alpha = 0.15f), RoundedCornerShape(4.dp))) }
            }

            Box(modifier = Modifier.fillMaxWidth().height(58.dp).alpha(bottomAlpha.value).offset(y = bottomOffsetY.value.dp)
                .drawBehind { drawRoundRect(DarkText.copy(alpha = 0.18f), cornerRadius = CornerRadius(32.dp.toPx()), size = Size(size.width, size.height + 6.dp.toPx()), topLeft = Offset(0f, 5.dp.toPx())) }
            ) {
                Button(onClick = onNext, modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkText)) {
                    Text("Next", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.5.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun FeatureCard(
    alpha: Float, offsetX: Float, iconFloat: Float, glowPulse: Float, orbitAngle: Float,
    accentColor: Color, title: String, description: String, drawIcon: DrawScope.(Size, Color) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().alpha(alpha).offset(x = offsetX.dp)
            .drawBehind {
                // card shadow
                drawRoundRect(Color.Black.copy(alpha = 0.06f), cornerRadius = CornerRadius(22.dp.toPx()),
                    size = Size(size.width, size.height + 4.dp.toPx()), topLeft = Offset(0f, 3.dp.toPx()))
            }
            .background(GlassWhite, RoundedCornerShape(20.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
            .drawBehind {
                drawRoundRect(accentColor, topLeft = Offset(0f, size.height * 0.15f),
                    size = Size(4.dp.toPx(), size.height * 0.7f), cornerRadius = CornerRadius(2.dp.toPx()))
            }
            .padding(start = 14.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(56.dp)) {
                val r = size.minDimension / 2f - 2.dp.toPx()
                val rad = Math.toRadians(orbitAngle.toDouble())
                drawCircle(accentColor, 3.5f.dp.toPx(), Offset(center.x + r * cos(rad).toFloat(), center.y + r * sin(rad).toFloat()))
                drawCircle(accentColor.copy(alpha = 0.15f), 9.dp.toPx(), Offset(center.x + r * cos(rad).toFloat(), center.y + r * sin(rad).toFloat()))
            }
            Canvas(modifier = Modifier.size(44.dp)) {
                drawCircle(brush = Brush.radialGradient(listOf(accentColor.copy(alpha = 0.3f * glowPulse), Color.Transparent)), radius = size.minDimension / 2f)
            }
            Canvas(modifier = Modifier.size(42.dp)) { drawHexBorder(size, accentColor.copy(alpha = 0.7f)) }
            Canvas(modifier = Modifier.size(22.dp).offset(y = (-2f + iconFloat * 4f).dp)) { drawIcon(size, accentColor) }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkText, letterSpacing = 0.3.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(description, fontSize = 12.sp, color = SubtleDark, lineHeight = 17.sp)
        }
    }
}

private fun DrawScope.drawHexBorder(s: Size, color: Color) {
    val cx = s.width / 2f; val cy = s.height / 2f; val r = s.minDimension / 2f - 1.dp.toPx()
    val path = Path(); for (i in 0..5) { val a = Math.toRadians((60.0 * i) - 30.0); val px = cx + r * cos(a).toFloat(); val py = cy + r * sin(a).toFloat(); if (i == 0) path.moveTo(px, py) else path.lineTo(px, py) }; path.close()
    drawPath(path, color, style = Stroke(2f.dp.toPx(), cap = StrokeCap.Round))
}

private fun DrawScope.drawDocScanIcon(s: Size, color: Color) {
    val w = s.width; val h = s.height; val st = 2f.dp.toPx()
    val page = Path().apply { val f = w * .25f; val r = 2.dp.toPx(); moveTo(0f, r); quadraticTo(0f, 0f, r, 0f); lineTo(w * .6f - f, 0f); lineTo(w * .6f, f); lineTo(w * .6f, h - r); quadraticTo(w * .6f, h, w * .6f - r, h); lineTo(r, h); quadraticTo(0f, h, 0f, h - r); close() }
    drawPath(page, color, style = Stroke(st, cap = StrokeCap.Round))
    for (i in 0..2) { val ly = h * .35f + i * h * .17f; drawLine(color.copy(.5f), Offset(w * .12f, ly), Offset(w * (if (i == 2) .35f else .48f), ly), st * .6f, StrokeCap.Round) }
    val mx = w * .72f; val my = h * .58f; val mr = w * .18f; drawCircle(color, mr, Offset(mx, my), style = Stroke(st))
    val ha = Math.toRadians(45.0); drawLine(color, Offset(mx + mr * cos(ha).toFloat(), my + mr * sin(ha).toFloat()), Offset(mx + (mr + w * .15f) * cos(ha).toFloat(), my + (mr + w * .15f) * sin(ha).toFloat()), st * 1.2f, StrokeCap.Round)
}

private fun DrawScope.drawQrIcon(s: Size, color: Color) {
    val w = s.width; val st = 1.8f.dp.toPx(); val u = w / 7f
    drawRect(color, Offset(0f, 0f), Size(3 * u, 3 * u), style = Stroke(st)); drawRect(color, Offset(u * .7f, u * .7f), Size(1.6f * u, 1.6f * u))
    drawRect(color, Offset(4 * u, 0f), Size(3 * u, 3 * u), style = Stroke(st)); drawRect(color, Offset(4.7f * u, .7f * u), Size(1.6f * u, 1.6f * u))
    drawRect(color, Offset(0f, 4 * u), Size(3 * u, 3 * u), style = Stroke(st)); drawRect(color, Offset(.7f * u, 4.7f * u), Size(1.6f * u, 1.6f * u))
    listOf(3.5f to 3.5f, 4.5f to 4.5f, 5.5f to 5.5f, 4f to 5.5f, 5.5f to 4f, 3.5f to 5f).forEach { (dx, dy) -> drawRect(color.copy(.7f), Offset(dx * u, dy * u), Size(u * .7f, u * .7f)) }
}

private fun DrawScope.drawIdCardIcon(s: Size, color: Color) {
    val w = s.width; val h = s.height; val st = 2f.dp.toPx(); val r = 3.dp.toPx()
    val card = Path().apply { moveTo(r, 0f); lineTo(w - r, 0f); quadraticTo(w, 0f, w, r); lineTo(w, h - r); quadraticTo(w, h, w - r, h); lineTo(r, h); quadraticTo(0f, h, 0f, h - r); lineTo(0f, r); quadraticTo(0f, 0f, r, 0f); close() }
    drawPath(card, color, style = Stroke(st, cap = StrokeCap.Round))
    drawCircle(color, w * .12f, Offset(w * .25f, h * .42f), style = Stroke(st * .8f))
    for (i in 0..2) { val ly = h * .28f + i * h * .2f; drawLine(color.copy(.6f), Offset(w * .48f, ly), Offset(w * (if (i == 2) .7f else .88f), ly), st * .7f, StrokeCap.Round) }
    drawRect(color.copy(.4f), Offset(w * .12f, h * .7f), Size(w * .2f, h * .18f), style = Stroke(st * .6f))
}

private fun DrawScope.drawFeatureParticles(phase: Float, cs: Size) {
    data class P(val x: Float, val y: Float, val r: Float, val s: Float, val o: Float)
    listOf(P(.08f,.15f,2f,1.2f,10f),P(.92f,.2f,1.5f,.9f,7f),P(.05f,.55f,2.2f,.6f,13f),P(.95f,.65f,1.7f,1.1f,9f),P(.5f,.05f,1.2f,1.5f,5f),P(.3f,.95f,2.4f,.5f,12f),P(.75f,.85f,1.6f,1.3f,6f),P(.18f,.78f,1.8f,.8f,11f))
        .forEach { p -> val cx = cs.width * p.x + p.o * cos(phase * p.s).dp.toPx(); val cy = cs.height * p.y + p.o * sin(phase * p.s * .7f).dp.toPx()
            drawCircle(Color.White.copy(alpha = .4f), p.r.dp.toPx() * 3.5f, Offset(cx, cy)); drawCircle(Color.White, p.r.dp.toPx(), Offset(cx, cy)) }
}
