package com.example.flippers.ui.screen.scanner

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flippers.ui.camera.CameraPermissionHandler
import com.example.flippers.ui.camera.CameraPreviewView
import com.example.flippers.ui.camera.QrCodeAnalyzer
import com.example.flippers.ui.components.ScannerOverlay
import com.example.flippers.viewmodel.ScannerViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

private enum class ScanContentType(
    val label: String,
    val icon: ImageVector,
    val accentColor: Color,
    val actionLabel: String?
) {
    URL("URL", Icons.Default.Link, Color(0xFF2196F3), "Open Link"),
    UPI("Payment", Icons.Default.Payment, Color(0xFFE91E63), "Open Payment"),
    EMAIL("Email", Icons.Default.Email, Color(0xFF009688), "Send Email"),
    PHONE("Phone", Icons.Default.Phone, Color(0xFF5C6BC0), "Call"),
    CONTACT("Contact", Icons.Default.Person, Color(0xFFFF9800), null),
    WIFI("WiFi", Icons.Default.Wifi, Color(0xFF9C27B0), null),
    EVENT("Event", Icons.Default.Celebration, Color(0xFFD4AF37), null),
    TEXT("Text", Icons.Default.TextSnippet, Color(0xFF607D8B), null);

    companion object {
        fun detect(content: String): ScanContentType = when {
            content.startsWith("http://") || content.startsWith("https://") -> URL
            content.startsWith("upi://") -> UPI
            content.startsWith("mailto:") -> EMAIL
            content.startsWith("tel:") -> PHONE
            content.startsWith("BEGIN:VCARD") -> CONTACT
            content.startsWith("WIFI:") -> WIFI
            content.startsWith("---") && content.contains("Event:") -> EVENT
            else -> TEXT
        }
    }
}

private data class ParsedEventData(
    val type: String = "",
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val venue: String = "",
    val host: String = "",
    val message: String = ""
) {
    companion object {
        fun parse(content: String): ParsedEventData {
            val lines = content.lines()
            var type = ""
            var title = ""
            var date = ""
            var time = ""
            var venue = ""
            var host = ""
            var message = ""

            for (line in lines) {
                val trimmed = line.trim()
                when {
                    trimmed.startsWith("---") && trimmed.endsWith("---") ->
                        type = trimmed.removePrefix("---").removeSuffix("---").trim()
                    trimmed.startsWith("Event:") -> title = trimmed.removePrefix("Event:").trim()
                    trimmed.startsWith("Date:") -> date = trimmed.removePrefix("Date:").trim()
                    trimmed.startsWith("Time:") -> time = trimmed.removePrefix("Time:").trim()
                    trimmed.startsWith("Venue:") -> venue = trimmed.removePrefix("Venue:").trim()
                    trimmed.startsWith("Host:") -> host = trimmed.removePrefix("Host:").trim()
                    trimmed.startsWith("Message:") -> message = trimmed.removePrefix("Message:").trim()
                }
            }
            return ParsedEventData(type, title, date, time, venue, host, message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScannerScreen(
    onNavigateBack: () -> Unit,
    viewModel: ScannerViewModel = viewModel()
) {
    val barcodeResult by viewModel.barcodeResult.collectAsState()
    val isFlashOn by viewModel.isFlashOn.collectAsState()
    var selectedTab by remember { mutableStateOf("QR Code") }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val analyzer = remember {
        QrCodeAnalyzer { barcodes ->
            barcodes.firstOrNull()?.rawValue?.let { viewModel.onBarcodeDetected(it) }
        }
    }

    // Image picker for uploading QR code from gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val inputImage = InputImage.fromFilePath(context, it)
                val scanner = BarcodeScanning.getClient()
                scanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        barcodes.firstOrNull()?.rawValue?.let { value ->
                            viewModel.onBarcodeDetected(value)
                        }
                    }
                    .addOnCompleteListener { scanner.close() }
            } catch (_: Exception) { }
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = if (barcodeResult != null) SheetValue.Expanded else SheetValue.PartiallyExpanded
        )
    )

    CameraPermissionHandler {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = if (barcodeResult != null) 320.dp else 0.dp,
            sheetContent = {
                if (barcodeResult != null) {
                    val contentType = remember(barcodeResult) {
                        ScanContentType.detect(barcodeResult ?: "")
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp)
                    ) {
                        // Drag handle indicator
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Icon + Type badge + Title row
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Type icon circle
                            Surface(
                                shape = CircleShape,
                                color = contentType.accentColor.copy(alpha = 0.12f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Icon(
                                        contentType.icon,
                                        contentDescription = null,
                                        tint = contentType.accentColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "Scan Result",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                // Type badge
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = contentType.accentColor.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = contentType.label,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = contentType.accentColor
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Content card — event or generic
                        if (contentType == ScanContentType.EVENT) {
                            val eventData = remember(barcodeResult) {
                                ParsedEventData.parse(barcodeResult ?: "")
                            }
                            EventScanResultCard(eventData = eventData, context = context)
                        } else {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = barcodeResult ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 6,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Action buttons
                        if (contentType.actionLabel != null) {
                            Button(
                                onClick = {
                                    barcodeResult?.let { value ->
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                                        context.startActivity(intent)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = contentType.accentColor
                                )
                            ) {
                                Icon(
                                    Icons.Default.OpenInNew,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    contentType.actionLabel,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Copy & Share row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FilledTonalButton(
                                onClick = {
                                    barcodeResult?.let {
                                        clipboardManager.setText(AnnotatedString(it))
                                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Copy")
                            }

                            OutlinedButton(
                                onClick = {
                                    barcodeResult?.let { value ->
                                        val intent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, value)
                                        }
                                        context.startActivity(Intent.createChooser(intent, "Share via"))
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Share")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            },
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                CameraPreviewView(
                    modifier = Modifier.fillMaxSize(),
                    imageAnalyzer = analyzer,
                    enableTorch = isFlashOn
                )

                ScannerOverlay(cutoutRatio = 0.7f, aspectRatio = 1f)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Top bar: Back + Title + Flash + Gallery
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Text(
                            text = "QR Scanner",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        // Flash toggle
                        IconButton(onClick = { viewModel.toggleFlash() }) {
                            Icon(
                                if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Flash",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Bottom controls: Gallery button + Tab chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Upload from gallery button
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            IconButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                                Icon(
                                    Icons.Default.PhotoLibrary,
                                    contentDescription = "Upload QR",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Tab chips
                        listOf("QR Code", "Barcode").forEach { tab ->
                            FilterChip(
                                selected = selectedTab == tab,
                                onClick = { selectedTab = tab },
                                label = { Text(tab) },
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        // Balance spacer for symmetry
                        Spacer(modifier = Modifier.size(44.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EventScanResultCard(
    eventData: ParsedEventData,
    context: android.content.Context
) {
    val accentColor = Color(0xFFD4AF37)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFFF8E7),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Event type header
            if (eventData.type.isNotBlank()) {
                Text(
                    text = eventData.type,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Event title
            if (eventData.title.isNotBlank()) {
                Text(
                    text = eventData.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E2723)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Date row — clickable to add to calendar
            if (eventData.date.isNotBlank()) {
                HorizontalDivider(color = accentColor.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.7f))
                        .clickable {
                            val intent = Intent(Intent.ACTION_INSERT).apply {
                                data = android.provider.CalendarContract.Events.CONTENT_URI
                                putExtra(
                                    android.provider.CalendarContract.Events.TITLE,
                                    eventData.title
                                )
                                if (eventData.venue.isNotBlank()) {
                                    putExtra(
                                        android.provider.CalendarContract.Events.EVENT_LOCATION,
                                        eventData.venue
                                    )
                                }
                                if (eventData.message.isNotBlank()) {
                                    putExtra(
                                        android.provider.CalendarContract.Events.DESCRIPTION,
                                        eventData.message
                                    )
                                }
                            }
                            context.startActivity(intent)
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.12f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = eventData.date,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF3E2723)
                        )
                        if (eventData.time.isNotBlank()) {
                            Text(
                                text = eventData.time,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF5D4037)
                            )
                        }
                    }
                    Text(
                        text = "Add to Calendar",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Venue row — clickable to open in Maps
            if (eventData.venue.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.7f))
                        .clickable {
                            val query = Uri.encode(eventData.venue)
                            val mapIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("geo:0,0?q=$query")
                            )
                            context.startActivity(mapIntent)
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF4CAF50).copy(alpha = 0.12f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = eventData.venue,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3E2723),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Open Map",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Host
            if (eventData.host.isNotBlank()) {
                HorizontalDivider(color = accentColor.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF5D4037),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Hosted by ${eventData.host}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5D4037)
                    )
                }
            }

            // Message
            if (eventData.message.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = eventData.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5D4037).copy(alpha = 0.8f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
