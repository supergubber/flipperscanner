package com.example.flippers.viewmodel

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flippers.data.local.GeneratedQrCode
import com.example.flippers.data.local.ProScanDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class QrGeneratorViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ProScanDatabase.getDatabase(application).generatedQrCodeDao()

    val qrHistory = dao.getAll()

    private val _selectedType = MutableStateFlow("")
    val selectedType: StateFlow<String> = _selectedType.asStateFlow()

    private val _contentFields = MutableStateFlow<Map<String, String>>(emptyMap())
    val contentFields: StateFlow<Map<String, String>> = _contentFields.asStateFlow()

    private val _generatedBitmap = MutableStateFlow<Bitmap?>(null)
    val generatedBitmap: StateFlow<Bitmap?> = _generatedBitmap.asStateFlow()

    private val _qrColorArgb = MutableStateFlow(0xFF000000.toInt())
    val qrColorArgb: StateFlow<Int> = _qrColorArgb.asStateFlow()

    private val _selectedQrCode = MutableStateFlow<GeneratedQrCode?>(null)
    val selectedQrCode: StateFlow<GeneratedQrCode?> = _selectedQrCode.asStateFlow()

    fun selectType(type: String) {
        if (_selectedType.value != type) {
            _selectedType.value = type
            _contentFields.value = emptyMap()
            _generatedBitmap.value = null
        }
    }

    fun updateField(key: String, value: String) {
        _contentFields.value = _contentFields.value.toMutableMap().apply { put(key, value) }
    }

    fun setQrColor(argb: Int) {
        _qrColorArgb.value = argb
    }

    fun generateQrCode(): Bitmap? {
        val content = buildQrContent() ?: return null
        val bitmap = createQrBitmap(content, 512, _qrColorArgb.value)
        _generatedBitmap.value = bitmap
        return bitmap
    }

    fun buildSmartLabel(): String {
        val fields = _contentFields.value
        return when (_selectedType.value) {
            "URL" -> fields["url"]?.take(40) ?: "URL"
            "Text" -> fields["text"]?.take(30) ?: "Text"
            "WiFi" -> "WiFi: ${fields["ssid"] ?: "Network"}"
            "Contact" -> "Contact: ${fields["name"] ?: "Unknown"}"
            "Email" -> fields["email"] ?: "Email"
            "Phone" -> fields["phone"] ?: "Phone"
            "Payment" -> "UPI: ${fields["upi"] ?: "Payment"}"
            else -> _selectedType.value
        }
    }

    fun saveQrCode(label: String) {
        val bitmap = _generatedBitmap.value ?: return
        val content = buildQrContent() ?: return
        viewModelScope.launch {
            val dir = File(getApplication<Application>().filesDir, "qr_codes")
            dir.mkdirs()
            val file = File(dir, "qr_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
            dao.insert(
                GeneratedQrCode(
                    type = _selectedType.value,
                    content = content,
                    label = label.ifBlank { _selectedType.value },
                    imagePath = file.absolutePath
                )
            )
        }
    }

    fun saveToGallery(bitmap: Bitmap): Boolean {
        return try {
            val context = getApplication<Application>()
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "QR_${System.currentTimeMillis()}.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Flippers")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }
            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return false

            context.contentResolver.openOutputStream(uri)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadQrCodeById(id: Long) {
        viewModelScope.launch {
            _selectedQrCode.value = dao.getById(id)
        }
    }

    fun deleteQrCode(qrCode: GeneratedQrCode) {
        viewModelScope.launch {
            if (qrCode.imagePath.isNotEmpty()) {
                File(qrCode.imagePath).delete()
            }
            dao.delete(qrCode)
        }
    }

    private fun buildQrContent(): String? {
        val fields = _contentFields.value
        return when (_selectedType.value) {
            "URL" -> fields["url"]?.takeIf { it.isNotBlank() }?.let { url ->
                if (url.startsWith("http://") || url.startsWith("https://")) url
                else "https://$url"
            }
            "Text" -> fields["text"]?.takeIf { it.isNotBlank() }
            "WiFi" -> {
                val ssid = fields["ssid"] ?: return null
                val password = fields["password"] ?: ""
                val security = fields["security"] ?: "WPA"
                "WIFI:T:$security;S:$ssid;P:$password;;"
            }
            "Contact" -> {
                val name = fields["name"] ?: return null
                val phone = fields["phone"] ?: ""
                val email = fields["email"] ?: ""
                "BEGIN:VCARD\nVERSION:3.0\nN:$name\nTEL:$phone\nEMAIL:$email\nEND:VCARD"
            }
            "Email" -> fields["email"]?.takeIf { it.isNotBlank() }?.let { "mailto:$it" }
            "Phone" -> fields["phone"]?.takeIf { it.isNotBlank() }?.let { "tel:$it" }
            "Payment" -> fields["upi"]?.takeIf { it.isNotBlank() }?.let { "upi://pay?pa=$it" }
            else -> null
        }
    }

    private fun createQrBitmap(content: String, size: Int, colorArgb: Int): Bitmap {
        val hints = mapOf(EncodeHintType.MARGIN to 1)
        val matrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (matrix[x, y]) colorArgb else 0xFFFFFFFF.toInt())
            }
        }
        return bitmap
    }
}
