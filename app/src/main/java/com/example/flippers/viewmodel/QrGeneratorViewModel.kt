package com.example.flippers.viewmodel

import android.app.Application
import android.graphics.Bitmap
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
            "URL" -> fields["url"]?.takeIf { it.isNotBlank() }
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
