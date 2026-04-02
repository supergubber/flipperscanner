package com.example.flippers.ui.screen.qr

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flippers.ui.components.ProScanTextField
import com.example.flippers.viewmodel.EventQrViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventQrEditorScreen(
    viewModel: EventQrViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val theme = state.activeColorTheme

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.setCenterImage(it) }
    }

    // Auto-regenerate when QR exists and title changes
    LaunchedEffect(state.eventTitle, state.eventDate, state.eventVenue, state.eventMessage) {
        if (state.qrBitmap != null && state.eventTitle.isNotBlank()) {
            viewModel.generateQrCode()
        }
    }

    // Save success feedback
    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            Toast.makeText(context, "Event QR saved!", Toast.LENGTH_SHORT).show()
            kotlinx.coroutines.delay(1500)
            viewModel.resetSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "${state.eventType.displayName} QR",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            // ── [A] Live Preview ──
            EventQrLivePreview(state = state)

            Spacer(Modifier.height(24.dp))

            // ── [B] Event Details ──
            SectionHeader("Event Details")
            Spacer(Modifier.height(8.dp))

            ProScanTextField(
                value = state.eventTitle,
                onValueChange = { viewModel.updateTitle(it) },
                label = "Event Title *",
                leadingIcon = Icons.Default.Title
            )
            Spacer(Modifier.height(10.dp))

            // Date & Time pickers
            val calendar = remember { Calendar.getInstance() }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Date field - opens DatePicker on click
                OutlinedTextField(
                    value = state.eventDate,
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    val formatted = String.format("%02d/%02d/%04d", day, month + 1, year)
                                    viewModel.updateDate(formatted)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                    leadingIcon = {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    enabled = false
                )

                // Time field - opens TimePicker on click
                OutlinedTextField(
                    value = state.eventTime,
                    onValueChange = {},
                    label = { Text("Time") },
                    readOnly = true,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    val amPm = if (hour < 12) "AM" else "PM"
                                    val h = if (hour % 12 == 0) 12 else hour % 12
                                    val formatted = String.format("%d:%02d %s", h, minute, amPm)
                                    viewModel.updateTime(formatted)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                false
                            ).show()
                        },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    enabled = false
                )
            }
            Spacer(Modifier.height(10.dp))

            // Venue / Location field with map button
            ProScanTextField(
                value = state.eventVenue,
                onValueChange = { viewModel.updateVenue(it) },
                label = "Venue / Location",
                leadingIcon = Icons.Default.LocationOn,
                trailingIcon = Icons.Default.Map,
                onTrailingIconClick = {
                    val query = if (state.eventVenue.isNotBlank()) {
                        Uri.encode(state.eventVenue)
                    } else ""
                    val mapIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=$query")
                    )
                    context.startActivity(mapIntent)
                }
            )
            Spacer(Modifier.height(10.dp))

            ProScanTextField(
                value = state.hostName,
                onValueChange = { viewModel.updateHostName(it) },
                label = "Host Name",
                leadingIcon = Icons.Default.Person
            )
            Spacer(Modifier.height(10.dp))

            ProScanTextField(
                value = state.eventMessage,
                onValueChange = { viewModel.updateMessage(it) },
                label = "Message / Invitation Text",
                leadingIcon = Icons.AutoMirrored.Filled.Message,
                singleLine = false
            )

            Spacer(Modifier.height(24.dp))

            // ── [C] Frame Style ──
            SectionHeader("Frame Style")
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FrameStyle.entries.forEach { frame ->
                    val isSelected = state.selectedFrame == frame
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.selectFrame(frame) }
                            .then(
                                if (isSelected) Modifier.border(
                                    2.dp, theme.primary, RoundedCornerShape(12.dp)
                                ) else Modifier
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) theme.primary.copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = if (isSelected) 2.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (frame.emoji.isNotEmpty()) {
                                Text(frame.emoji, fontSize = 16.sp)
                                Spacer(Modifier.width(6.dp))
                            }
                            Text(
                                frame.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) theme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── [D] Color Theme ──
            SectionHeader("Color Theme")
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                eventColorPresets.forEach { preset ->
                    val isSelected = state.activeColorTheme == preset
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .then(
                                if (isSelected) Modifier.border(3.dp, preset.dark, CircleShape)
                                else Modifier
                            )
                            .padding(if (isSelected) 4.dp else 0.dp)
                            .clip(CircleShape)
                            .background(preset.primary)
                            .clickable { viewModel.setCustomColor(preset) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── [E] Emoji Grid ──
            SectionHeader("Corner Emojis", subtitle = "Select up to 4")
            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(state.eventType.allEmojis) { emoji ->
                    val isSelected = state.selectedEmojis.contains(emoji)
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.toggleEmoji(emoji) }
                            .then(
                                if (isSelected) Modifier.border(
                                    2.dp, theme.primary, RoundedCornerShape(12.dp)
                                ) else Modifier
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) theme.primary.copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(emoji, fontSize = 22.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── [F] Center Image ──
            SectionHeader("Center Logo")
            Spacer(Modifier.height(8.dp))

            if (state.centerImageBitmap != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Image(
                            bitmap = state.centerImageBitmap!!.asImageBitmap(),
                            contentDescription = "Center logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Logo uploaded",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.removeCenterImage() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            } else {
                OutlinedButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Upload Logo Image")
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── [G] Action Buttons ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Generate & Save (primary)
                Button(
                    onClick = {
                        if (state.eventTitle.isBlank()) {
                            Toast.makeText(context, "Enter event title", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.generateQrCode()
                        viewModel.saveEventQr()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = theme.primary
                    ),
                    enabled = !state.isSaving
                ) {
                    AnimatedContent(
                        targetState = state.saveSuccess,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "save_anim"
                    ) { saved ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (saved) Icons.Default.Check else Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                if (saved) "Saved!" else "Generate & Save",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                // Share (secondary)
                OutlinedButton(
                    onClick = {
                        if (state.compositeBitmap == null) {
                            if (state.eventTitle.isBlank()) {
                                Toast.makeText(context, "Enter event title first", Toast.LENGTH_SHORT).show()
                                return@OutlinedButton
                            }
                            viewModel.generateQrCode()
                        }
                        viewModel.getShareIntent()?.let { intent ->
                            context.startActivity(Intent.createChooser(intent, "Share Event QR"))
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Share", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Reusable section header ──

@Composable
private fun SectionHeader(title: String, subtitle: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        if (subtitle != null) {
            Spacer(Modifier.width(8.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
        }
    }
}
