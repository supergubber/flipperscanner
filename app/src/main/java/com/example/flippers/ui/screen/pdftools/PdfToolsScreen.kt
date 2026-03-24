package com.example.flippers.ui.screen.pdftools

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Splitscreen
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private data class PdfTool(val label: String, val icon: ImageVector, val route: String)

private val pdfTools = listOf(
    PdfTool("Watermark", Icons.Default.WaterDrop, "watermark"),
    PdfTool("eSign", Icons.Default.Draw, "coming_soon"),
    PdfTool("Split PDF", Icons.Default.Splitscreen, "coming_soon"),
    PdfTool("Merge PDF", Icons.Default.MergeType, "merge_pdf"),
    PdfTool("Protect PDF", Icons.Default.Lock, "coming_soon"),
    PdfTool("Compress", Icons.Default.Compress, "compress_pdf"),
    PdfTool("Convert to PDF", Icons.Default.PictureAsPdf, "coming_soon")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfToolsScreen(
    onNavigateBack: () -> Unit,
    onWatermarkClick: () -> Unit,
    onMergeClick: () -> Unit,
    onCompressClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PDF Tools") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pdfTools) { tool ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (tool.route) {
                                "watermark" -> onWatermarkClick()
                                "merge_pdf" -> onMergeClick()
                                "compress_pdf" -> onCompressClick()
                                else -> Toast.makeText(context, "Feature coming soon with Pro plan", Toast.LENGTH_SHORT).show()
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = tool.icon,
                            contentDescription = tool.label,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tool.label,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
