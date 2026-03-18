package com.example.flippers.ui.screen.scanner

import android.graphics.Bitmap
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flippers.ui.camera.CameraPermissionHandler
import com.example.flippers.ui.camera.CameraPreviewView
import com.example.flippers.ui.components.ScannerBottomBar
import com.example.flippers.viewmodel.ScannerViewModel

@Composable
fun DocumentScannerScreen(
    onNavigateBack: () -> Unit,
    onImageCaptured: (String) -> Unit,
    viewModel: ScannerViewModel = viewModel()
) {
    val isFlashOn by viewModel.isFlashOn.collectAsState()
    val isGridOn by viewModel.isGridOn.collectAsState()
    val scanMode by viewModel.scanMode.collectAsState()
    val view = LocalView.current

    val modes = listOf("Document", "Book", "ID Card", "Business Card", "Whiteboard")

    LaunchedEffect(Unit) {
        viewModel.capturedImagePath.collect { path ->
            onImageCaptured(path)
        }
    }

    CameraPermissionHandler {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            CameraPreviewView(
                modifier = Modifier.fillMaxSize(),
                imageCapture = viewModel.imageCapture,
                enableTorch = isFlashOn
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Document Scanner",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    modes.forEach { mode ->
                        FilterChip(
                            selected = scanMode == mode,
                            onClick = { viewModel.setScanMode(mode) },
                            label = { Text(mode, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                ScannerBottomBar(
                    isFlashOn = isFlashOn,
                    isGridOn = isGridOn,
                    onGalleryClick = { },
                    onFlashClick = viewModel::toggleFlash,
                    onCaptureClick = {
                        captureScreen(view) { bitmap ->
                            viewModel.saveCapture(bitmap, scanMode.lowercase())
                        }
                    },
                    onGridClick = viewModel::toggleGrid,
                    onSettingsClick = { }
                )
            }
        }
    }
}

private fun captureScreen(view: View, onCapture: (Bitmap) -> Unit) {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    view.draw(canvas)
    onCapture(bitmap)
}
