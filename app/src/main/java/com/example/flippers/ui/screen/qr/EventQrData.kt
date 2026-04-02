package com.example.flippers.ui.screen.qr

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// ── Color Theme ──

data class EventColorTheme(
    val primary: Color,
    val background: Color,
    val dark: Color
)

// ── Frame Styles ──

enum class FrameStyle(val displayName: String, val emoji: String) {
    ELEGANT("Elegant", ""),
    HEARTS("Hearts", "\u2764\uFE0F"),
    STARS("Stars", "\u2B50"),
    FLORAL("Floral", "\uD83C\uDF38"),
    MINIMAL("Minimal", ""),
    FESTIVE("Festive", "\uD83C\uDF89")
}

// ── Event Types ──

enum class EventType(
    val displayName: String,
    val icon: ImageVector,
    val themeColors: EventColorTheme,
    val defaultEmojis: List<String>,
    val allEmojis: List<String>
) {
    MARRIAGE(
        "Marriage", Icons.Default.Favorite,
        EventColorTheme(Color(0xFFD4AF37), Color(0xFFFFF8E7), Color(0xFF8B6914)),
        listOf("\uD83D\uDC8D", "\uD83D\uDC92", "\uD83E\uDD35", "\uD83D\uDC70"),
        listOf("\uD83D\uDC8D", "\uD83D\uDC92", "\uD83E\uDD35", "\uD83D\uDC70", "\uD83D\uDC95", "\uD83D\uDC90", "\uD83E\uDD42", "\u2728", "\uD83D\uDC9D", "\uD83C\uDF8A", "\uD83D\uDD4A\uFE0F", "\uD83D\uDC91")
    ),
    BIRTHDAY(
        "Birthday", Icons.Default.Cake,
        EventColorTheme(Color(0xFFFF6B6B), Color(0xFFFFF0F0), Color(0xFFCC3333)),
        listOf("\uD83C\uDF82", "\uD83C\uDF88", "\uD83C\uDF81", "\uD83C\uDF89"),
        listOf("\uD83C\uDF82", "\uD83C\uDF88", "\uD83C\uDF81", "\uD83C\uDF89", "\uD83E\uDD73", "\uD83C\uDF8A", "\uD83C\uDF70", "\uD83D\uDD6F\uFE0F", "\u2728", "\uD83C\uDF80", "\uD83C\uDFB6", "\uD83D\uDCAB")
    ),
    FESTIVAL(
        "Festival", Icons.Default.Celebration,
        EventColorTheme(Color(0xFFFF9800), Color(0xFFFFF3E0), Color(0xFFE65100)),
        listOf("\uD83C\uDF8A", "\u2728", "\uD83C\uDF86", "\uD83E\uDE94"),
        listOf("\uD83C\uDF8A", "\u2728", "\uD83C\uDF86", "\uD83E\uDE94", "\uD83D\uDD6F\uFE0F", "\uD83C\uDF89", "\uD83C\uDF1F", "\uD83C\uDFAD", "\uD83C\uDFAA", "\uD83C\uDFEE", "\uD83C\uDFB6", "\uD83C\uDF87")
    ),
    PARTY(
        "Party", Icons.Default.MusicNote,
        EventColorTheme(Color(0xFF9C27B0), Color(0xFFF3E5F5), Color(0xFF6A1B9A)),
        listOf("\uD83C\uDFB5", "\uD83E\uDD73", "\uD83C\uDF7E", "\uD83C\uDFB6"),
        listOf("\uD83C\uDFB5", "\uD83E\uDD73", "\uD83C\uDF7E", "\uD83C\uDFB6", "\uD83C\uDF89", "\uD83C\uDF88", "\uD83D\uDC83", "\uD83D\uDD7A", "\uD83C\uDFA7", "\uD83E\uDE69", "\uD83C\uDF79", "\u2728")
    ),
    BABY_SHOWER(
        "Baby Shower", Icons.Default.ChildCare,
        EventColorTheme(Color(0xFF64B5F6), Color(0xFFE3F2FD), Color(0xFF1976D2)),
        listOf("\uD83D\uDC76", "\uD83C\uDF7C", "\uD83E\uDDF8", "\uD83C\uDF80"),
        listOf("\uD83D\uDC76", "\uD83C\uDF7C", "\uD83E\uDDF8", "\uD83C\uDF80", "\uD83D\uDC95", "\uD83C\uDF38", "\u2B50", "\uD83C\uDF89", "\uD83E\uDDF7", "\uD83D\uDC9D", "\uD83C\uDF08", "\uD83C\uDF6D")
    ),
    GRADUATION(
        "Graduation", Icons.Default.School,
        EventColorTheme(Color(0xFF1B5E20), Color(0xFFE8F5E9), Color(0xFF2E7D32)),
        listOf("\uD83C\uDF93", "\uD83D\uDCDC", "\uD83C\uDFC6", "\u2B50"),
        listOf("\uD83C\uDF93", "\uD83D\uDCDC", "\uD83C\uDFC6", "\u2B50", "\uD83C\uDF8A", "\uD83C\uDF89", "\uD83D\uDCD6", "\uD83C\uDFAF", "\uD83D\uDCAA", "\u2728", "\uD83E\uDD47", "\uD83C\uDFB6")
    ),
    ANNIVERSARY(
        "Anniversary", Icons.Default.FavoriteBorder,
        EventColorTheme(Color(0xFFE91E63), Color(0xFFFCE4EC), Color(0xFFC2185B)),
        listOf("\u2764\uFE0F", "\uD83C\uDF39", "\uD83D\uDC90", "\uD83E\uDD42"),
        listOf("\u2764\uFE0F", "\uD83C\uDF39", "\uD83D\uDC90", "\uD83E\uDD42", "\uD83D\uDC9D", "\uD83D\uDC95", "\uD83D\uDC91", "\uD83D\uDD6F\uFE0F", "\u2728", "\uD83C\uDFB6", "\uD83D\uDC8D", "\uD83C\uDF8A")
    ),
    CORPORATE(
        "Corporate", Icons.Default.Business,
        EventColorTheme(Color(0xFF37474F), Color(0xFFECEFF1), Color(0xFF263238)),
        listOf("\uD83D\uDCCA", "\uD83C\uDFE2", "\uD83E\uDD1D", "\uD83D\uDCBC"),
        listOf("\uD83D\uDCCA", "\uD83C\uDFE2", "\uD83E\uDD1D", "\uD83D\uDCBC", "\uD83C\uDFAF", "\u2B50", "\uD83C\uDFC6", "\uD83D\uDCC8", "\uD83C\uDF8A", "\u2728", "\uD83C\uDF89", "\uD83D\uDC54")
    );
}

// ── Extra color themes for picker ──

val eventColorPresets = listOf(
    EventColorTheme(Color(0xFFD4AF37), Color(0xFFFFF8E7), Color(0xFF8B6914)),  // Gold
    EventColorTheme(Color(0xFFE91E63), Color(0xFFFCE4EC), Color(0xFFC2185B)),  // Rose
    EventColorTheme(Color(0xFF1565C0), Color(0xFFE3F2FD), Color(0xFF0D47A1)),  // Royal Blue
    EventColorTheme(Color(0xFF2E7D32), Color(0xFFE8F5E9), Color(0xFF1B5E20)),  // Emerald
    EventColorTheme(Color(0xFF6A1B9A), Color(0xFFF3E5F5), Color(0xFF4A148C)),  // Purple
    EventColorTheme(Color(0xFFE65100), Color(0xFFFFF3E0), Color(0xFFBF360C))   // Saffron
)

// ── UI State ──

data class EventQrUiState(
    val eventType: EventType = EventType.BIRTHDAY,
    val eventTitle: String = "",
    val eventDate: String = "",
    val eventTime: String = "",
    val eventVenue: String = "",
    val eventMessage: String = "",
    val hostName: String = "",
    val selectedFrame: FrameStyle = FrameStyle.ELEGANT,
    val selectedEmojis: List<String> = emptyList(),
    val customColorTheme: EventColorTheme? = null,
    val centerImageUri: Uri? = null,
    val centerImageBitmap: Bitmap? = null,
    val qrBitmap: Bitmap? = null,
    val compositeBitmap: Bitmap? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
) {
    val activeColorTheme: EventColorTheme
        get() = customColorTheme ?: eventType.themeColors
}
