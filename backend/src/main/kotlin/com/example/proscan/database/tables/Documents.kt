package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Documents : Table("documents") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id)
    val fileName = varchar("file_name", 255)
    val fileType = varchar("file_type", 50).default("image")
    val filePath = varchar("file_path", 512)
    val fileSizeBytes = long("file_size_bytes").default(0)
    val scanType = varchar("scan_type", 50) // Document, QR Code, Book, ID Card
    val mimeType = varchar("mime_type", 100).default("image/jpeg")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}
