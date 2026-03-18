package com.example.flippers.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flippers.data.local.ProScanDatabase
import com.example.flippers.data.repository.DocumentRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScannerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DocumentRepository

    val imageCapture = ImageCapture.Builder().build()

    private val _isFlashOn = MutableStateFlow(false)
    val isFlashOn: StateFlow<Boolean> = _isFlashOn.asStateFlow()

    private val _isGridOn = MutableStateFlow(false)
    val isGridOn: StateFlow<Boolean> = _isGridOn.asStateFlow()

    private val _scanMode = MutableStateFlow("Document")
    val scanMode: StateFlow<String> = _scanMode.asStateFlow()

    private val _barcodeResult = MutableStateFlow<String?>(null)
    val barcodeResult: StateFlow<String?> = _barcodeResult.asStateFlow()

    private val _capturedImagePath = MutableSharedFlow<String>()
    val capturedImagePath: SharedFlow<String> = _capturedImagePath.asSharedFlow()

    init {
        val dao = ProScanDatabase.getDatabase(application).scannedDocumentDao()
        repository = DocumentRepository(application, dao)
    }

    fun toggleFlash() { _isFlashOn.value = !_isFlashOn.value }
    fun toggleGrid() { _isGridOn.value = !_isGridOn.value }
    fun setScanMode(mode: String) { _scanMode.value = mode }
    fun onBarcodeDetected(value: String) { _barcodeResult.value = value }
    fun clearBarcodeResult() { _barcodeResult.value = null }

    fun saveCapture(bitmap: Bitmap, scanType: String = "document") {
        viewModelScope.launch {
            val doc = repository.saveDocument(bitmap, scanType)
            _capturedImagePath.emit(doc.filePath)
        }
    }
}
