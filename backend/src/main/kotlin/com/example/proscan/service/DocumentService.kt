package com.example.proscan.service

import com.example.proscan.database.tables.Documents
import com.example.proscan.models.responses.DocumentResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DocumentService {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun upload(userId: Long, fileName: String, scanType: String, fileBytes: ByteArray, uploadDir: String): DocumentResponse {
        val timestamp = System.currentTimeMillis()
        val safeName = fileName.replace(Regex("[^a-zA-Z0-9._-]"), "_")
        val filePath = "$uploadDir/documents/${userId}/${timestamp}_$safeName"
        val file = File(filePath)
        file.parentFile.mkdirs()
        file.writeBytes(fileBytes)

        return transaction {
            val now = LocalDateTime.now()
            val id = Documents.insert {
                it[Documents.userId] = userId
                it[Documents.fileName] = fileName
                it[Documents.scanType] = scanType
                it[Documents.filePath] = filePath
                it[Documents.fileSizeBytes] = fileBytes.size.toLong()
                it[Documents.mimeType] = guessMimeType(fileName)
                it[Documents.createdAt] = now
                it[Documents.updatedAt] = now
            }[Documents.id]

            DocumentResponse(id, fileName, "image", fileBytes.size.toLong(), scanType,
                guessMimeType(fileName), now.format(formatter), now.format(formatter))
        }
    }

    fun list(userId: Long, page: Int, size: Int, search: String?): Pair<List<DocumentResponse>, Long> {
        return transaction {
            var query = Documents.selectAll().where { Documents.userId eq userId }
            if (!search.isNullOrBlank()) {
                query = Documents.selectAll().where {
                    (Documents.userId eq userId) and (Documents.fileName like "%$search%")
                }
            }
            val total = query.count()
            val docs = query.orderBy(Documents.createdAt, SortOrder.DESC)
                .limit(size).offset(((page - 1) * size).toLong())
                .map { rowToResponse(it) }
            Pair(docs, total)
        }
    }

    fun getById(documentId: Long, userId: Long): DocumentResponse {
        return transaction {
            Documents.selectAll().where { (Documents.id eq documentId) and (Documents.userId eq userId) }
                .firstOrNull()?.let { rowToResponse(it) }
                ?: throw NoSuchElementException("Document not found")
        }
    }

    fun getFilePath(documentId: Long, userId: Long): String {
        return transaction {
            Documents.selectAll().where { (Documents.id eq documentId) and (Documents.userId eq userId) }
                .firstOrNull()?.get(Documents.filePath)
                ?: throw NoSuchElementException("Document not found")
        }
    }

    fun delete(documentId: Long, userId: Long) {
        transaction {
            val row = Documents.selectAll()
                .where { (Documents.id eq documentId) and (Documents.userId eq userId) }
                .firstOrNull() ?: throw NoSuchElementException("Document not found")
            val filePath = row[Documents.filePath]
            File(filePath).delete()
            Documents.deleteWhere { (Documents.id eq documentId) and (Documents.userId eq userId) }
        }
    }

    fun countByUser(userId: Long): Long = transaction {
        Documents.selectAll().where { Documents.userId eq userId }.count()
    }

    fun countByUserAndType(userId: Long, scanType: String): Long = transaction {
        Documents.selectAll().where { (Documents.userId eq userId) and (Documents.scanType eq scanType) }.count()
    }

    private fun rowToResponse(row: ResultRow) = DocumentResponse(
        id = row[Documents.id],
        fileName = row[Documents.fileName],
        fileType = row[Documents.fileType],
        fileSizeBytes = row[Documents.fileSizeBytes],
        scanType = row[Documents.scanType],
        mimeType = row[Documents.mimeType],
        createdAt = row[Documents.createdAt].format(formatter),
        updatedAt = row[Documents.updatedAt].format(formatter)
    )

    private fun guessMimeType(fileName: String): String = when {
        fileName.endsWith(".pdf", true) -> "application/pdf"
        fileName.endsWith(".png", true) -> "image/png"
        fileName.endsWith(".jpg", true) || fileName.endsWith(".jpeg", true) -> "image/jpeg"
        else -> "application/octet-stream"
    }
}
