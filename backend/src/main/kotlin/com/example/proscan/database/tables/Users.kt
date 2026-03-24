package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Users : Table("users") {
    val id = long("id").autoIncrement()
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val name = varchar("name", 255)
    val avatarUrl = varchar("avatar_url", 512).nullable()
    val phone = varchar("phone", 50).nullable()
    val gender = varchar("gender", 20).nullable()
    val dateOfBirth = varchar("date_of_birth", 50).nullable()
    val plan = varchar("plan", 50).default("free")
    val profileCompleted = bool("profile_completed").default(false)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}
