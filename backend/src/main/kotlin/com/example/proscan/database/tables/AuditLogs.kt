package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table

object AuditLogs : Table("audit_logs") {
    val id = long("id").autoIncrement()
    val orgId = long("org_id").default(1)
    val user = varchar("user_name", 255)
    val action = text("action")
    val timestamp = varchar("timestamp", 100)

    override val primaryKey = PrimaryKey(id)
}
