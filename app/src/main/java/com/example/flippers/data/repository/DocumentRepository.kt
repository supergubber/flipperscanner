package com.example.flippers.data.repository

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import com.example.flippers.data.local.ScannedDocument
import com.example.flippers.data.local.ScannedDocumentDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DocumentRepository(
    private val context: Context,
    private val documentDao: ScannedDocumentDao
) {

    private val scansDir: File
        get() {
            val dir = File(context.filesDir, "scans")
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

    fun getAllDocuments(): Flow<List<ScannedDocument>> = documentDao.getAllDocuments()

    fun getRecentDocuments(limit: Int = 10): Flow<List<ScannedDocument>> =
        documentDao.getRecentDocuments(limit)

    fun searchDocuments(query: String): Flow<List<ScannedDocument>> =
        documentDao.searchDocuments(query)

    suspend fun saveDocument(
        bitmap: Bitmap,
        scanType: String = "document"
    ): ScannedDocument = withContext(Dispatchers.IO) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "SCAN_${scanType}_$timestamp.jpg"
        val file = File(scansDir, fileName)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        val document = ScannedDocument(
            fileName = fileName,
            filePath = file.absolutePath,
            fileSize = file.length(),
            scanType = scanType,
            mimeType = "image/jpeg",
            thumbnailPath = file.absolutePath
        )
        val id = documentDao.insertDocument(document)
        document.copy(id = id)
    }

    suspend fun deleteDocument(document: ScannedDocument) = withContext(Dispatchers.IO) {
        val file = File(document.filePath)
        if (file.exists()) file.delete()
        documentDao.deleteDocument(document)
    }

    fun getShareIntent(document: ScannedDocument): Intent {
        val file = File(document.filePath)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        return Intent(Intent.ACTION_SEND).apply {
            type = document.mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
