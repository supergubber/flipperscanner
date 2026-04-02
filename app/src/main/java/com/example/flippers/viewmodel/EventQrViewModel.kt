package com.example.flippers.viewmodel

import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flippers.data.local.GeneratedQrCode
import com.example.flippers.data.local.ProScanDatabase
import com.example.flippers.ui.screen.qr.EventColorTheme
import com.example.flippers.ui.screen.qr.EventQrRenderer
import com.example.flippers.ui.screen.qr.EventQrUiState
import com.example.flippers.ui.screen.qr.EventType
import com.example.flippers.ui.screen.qr.FrameStyle
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class EventQrViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ProScanDatabase.getDatabase(application).generatedQrCodeDao()

    private val _uiState = MutableStateFlow(EventQrUiState())
    val uiState: StateFlow<EventQrUiState> = _uiState.asStateFlow()

    // ── Event type ──

    fun selectEventType(type: EventType) {
        _uiState.update {
            EventQrUiState(
                eventType = type,
                selectedEmojis = type.defaultEmojis,
                selectedFrame = FrameStyle.ELEGANT
            )
        }
    }

    // ── Field updates ──

    fun updateTitle(value: String) = _uiState.update { it.copy(eventTitle = value) }
    fun updateDate(value: String) = _uiState.update { it.copy(eventDate = value) }
    fun updateTime(value: String) = _uiState.update { it.copy(eventTime = value) }
    fun updateVenue(value: String) = _uiState.update { it.copy(eventVenue = value) }
    fun updateMessage(value: String) = _uiState.update { it.copy(eventMessage = value) }
    fun updateHostName(value: String) = _uiState.update { it.copy(hostName = value) }

    // ── Decoration ──

    fun selectFrame(frame: FrameStyle) {
        _uiState.update { it.copy(selectedFrame = frame) }
        regenerateComposite()
    }

    fun toggleEmoji(emoji: String) {
        _uiState.update { state ->
            val current = state.selectedEmojis.toMutableList()
            if (current.contains(emoji)) {
                current.remove(emoji)
            } else if (current.size < 4) {
                current.add(emoji)
            }
            state.copy(selectedEmojis = current)
        }
        regenerateComposite()
    }

    fun setCustomColor(theme: EventColorTheme) {
        _uiState.update { it.copy(customColorTheme = theme) }
        regenerateComposite()
    }

    fun setCenterImage(uri: Uri) {
        viewModelScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                try {
                    val context = getApplication<Application>()
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
                } catch (e: Exception) {
                    null
                }
            }
            _uiState.update { it.copy(centerImageUri = uri, centerImageBitmap = bitmap) }
            regenerateComposite()
        }
    }

    fun removeCenterImage() {
        _uiState.update { it.copy(centerImageUri = null, centerImageBitmap = null) }
        regenerateComposite()
    }

    // ── QR Generation ──

    fun generateQrCode() {
        val content = buildEventContent() ?: return
        viewModelScope.launch {
            val qrBitmap = withContext(Dispatchers.Default) {
                createQrBitmap(content, 512, 0xFF000000.toInt())
            }
            _uiState.update { it.copy(qrBitmap = qrBitmap) }
            regenerateComposite()
        }
    }

    fun regenerateComposite() {
        val state = _uiState.value
        val qr = state.qrBitmap ?: return
        viewModelScope.launch {
            val composite = withContext(Dispatchers.Default) {
                EventQrRenderer.renderComposite(
                    qrBitmap = qr,
                    frame = state.selectedFrame,
                    emojis = state.selectedEmojis,
                    colorTheme = state.activeColorTheme,
                    centerImage = state.centerImageBitmap
                )
            }
            _uiState.update { it.copy(compositeBitmap = composite) }
        }
    }

    // ── Save ──

    fun saveEventQr() {
        val state = _uiState.value
        val bitmap = state.compositeBitmap ?: return
        val content = buildEventContent() ?: return

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Save to internal storage
                val dir = File(getApplication<Application>().filesDir, "qr_codes")
                dir.mkdirs()
                val file = File(dir, "event_qr_${System.currentTimeMillis()}.png")
                FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

                // Insert into Room DB
                dao.insert(
                    GeneratedQrCode(
                        type = "Event:${state.eventType.name}",
                        content = content,
                        label = "${state.eventType.displayName}: ${state.eventTitle.take(30)}",
                        imagePath = file.absolutePath
                    )
                )

                // Save to gallery
                saveToGallery(bitmap)
            }
            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }

    fun resetSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

    private fun saveToGallery(bitmap: Bitmap) {
        try {
            val context = getApplication<Application>()
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "EventQR_${System.currentTimeMillis()}.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Flippers")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }
            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
            ) ?: return
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)
            }
        } catch (_: Exception) { }
    }

    // ── Share ──

    fun getShareIntent(): Intent? {
        val bitmap = _uiState.value.compositeBitmap ?: return null
        val context = getApplication<Application>()
        val dir = File(context.cacheDir, "shared_qr")
        dir.mkdirs()
        val file = File(dir, "event_qr_share.png")
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    // ── Content builder ──

    private fun buildEventContent(): String? {
        val s = _uiState.value
        if (s.eventTitle.isBlank()) return null
        return buildString {
            appendLine("--- ${s.eventType.displayName} ---")
            appendLine("Event: ${s.eventTitle}")
            if (s.eventDate.isNotBlank()) appendLine("Date: ${s.eventDate}")
            if (s.eventTime.isNotBlank()) appendLine("Time: ${s.eventTime}")
            if (s.eventVenue.isNotBlank()) appendLine("Venue: ${s.eventVenue}")
            if (s.hostName.isNotBlank()) appendLine("Host: ${s.hostName}")
            if (s.eventMessage.isNotBlank()) appendLine("Message: ${s.eventMessage}")
        }
    }

    // ── QR bitmap ──

    private fun createQrBitmap(content: String, size: Int, colorArgb: Int): Bitmap {
        val hints = mapOf<EncodeHintType, Any>(
            EncodeHintType.MARGIN to 2,
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H
        )
        val matrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val fgColor = colorArgb or (0xFF shl 24) // ensure full alpha
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (matrix[x, y]) fgColor else 0xFFFFFFFF.toInt())
            }
        }
        return bitmap
    }
}
