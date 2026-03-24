package com.example.proscan.service

import com.example.proscan.database.tables.QrCodes
import com.example.proscan.models.responses.QrCodeResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object QrService {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun create(userId: Long, type: String, content: String, label: String): QrCodeResponse {
        return transaction {
            val now = LocalDateTime.now()
            val id = QrCodes.insert {
                it[QrCodes.userId] = userId
                it[QrCodes.type] = type
                it[QrCodes.content] = content
                it[QrCodes.label] = label
                it[QrCodes.createdAt] = now
            }[QrCodes.id]

            QrCodeResponse(id, type, content, label, null, now.format(formatter))
        }
    }

    fun list(userId: Long): List<QrCodeResponse> {
        return transaction {
            QrCodes.selectAll().where { QrCodes.userId eq userId }
                .orderBy(QrCodes.createdAt, SortOrder.DESC)
                .map { rowToResponse(it) }
        }
    }

    fun getById(qrId: Long, userId: Long): QrCodeResponse {
        return transaction {
            QrCodes.selectAll().where { (QrCodes.id eq qrId) and (QrCodes.userId eq userId) }
                .firstOrNull()?.let { rowToResponse(it) }
                ?: throw NoSuchElementException("QR code not found")
        }
    }

    fun delete(qrId: Long, userId: Long) {
        transaction {
            val count = QrCodes.deleteWhere { (QrCodes.id eq qrId) and (QrCodes.userId eq userId) }
            if (count == 0) throw NoSuchElementException("QR code not found")
        }
    }

    private fun rowToResponse(row: ResultRow) = QrCodeResponse(
        id = row[QrCodes.id],
        type = row[QrCodes.type],
        content = row[QrCodes.content],
        label = row[QrCodes.label],
        imagePath = row[QrCodes.imagePath],
        createdAt = row[QrCodes.createdAt].format(formatter)
    )
}
