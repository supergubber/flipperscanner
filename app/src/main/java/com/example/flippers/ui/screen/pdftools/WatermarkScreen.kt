package com.example.flippers.ui.screen.pdftools

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flippers.ui.components.ProScanButton
import com.example.flippers.ui.components.ProScanTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatermarkScreen(onNavigateBack: () -> Unit) {
    var watermarkText by remember { mutableStateOf("CONFIDENTIAL") }
    var selectedPosition by remember { mutableStateOf("Center") }
    val positions = listOf("Top", "Center", "Bottom", "Diagonal")
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Watermark") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Preview area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = when (selectedPosition) {
                    "Top" -> Alignment.TopCenter
                    "Bottom" -> Alignment.BottomCenter
                    else -> Alignment.Center
                }
            ) {
                // Simulated document
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(8) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (it == 7) 0.6f else 1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
                        )
                    }
                }

                // Watermark overlay
                Text(
                    text = watermarkText,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    textAlign = TextAlign.Center,
                    modifier = if (selectedPosition == "Diagonal") Modifier
                        .rotate(-30f)
                        .padding(16.dp) else Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProScanTextField(
                value = watermarkText,
                onValueChange = { watermarkText = it },
                label = "Watermark Text",
                leadingIcon = Icons.Default.TextFields
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Position", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                positions.forEach { position ->
                    FilterChip(
                        selected = selectedPosition == position,
                        onClick = { selectedPosition = position },
                        label = { Text(position) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            ProScanButton(
                text = "Apply Watermark",
                onClick = {
                    Toast.makeText(context, "Feature coming soon with Pro plan", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
