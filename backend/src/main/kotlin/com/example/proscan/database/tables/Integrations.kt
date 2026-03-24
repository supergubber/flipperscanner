package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table

object Integrations : Table("integrations") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id)
    val name = varchar("name", 100)
    val icon = varchar("icon", 50)
    val description = text("description")
    val isConnected = bool("is_connected").default(false)
    val category = varchar("category", 50)

    override val primaryKey = PrimaryKey(id)
}
