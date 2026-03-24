package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object QrCodes : Table("qr_codes") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id)
    val type = varchar("type", 50) // URL, Text, WiFi, Contact, Email, Phone, Payment
    val content = text("content")
    val label = varchar("label", 255)
    val imagePath = varchar("image_path", 512).nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}
