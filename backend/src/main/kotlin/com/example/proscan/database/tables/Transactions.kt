package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table

object Transactions : Table("transactions") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id)
    val description = varchar("description", 255)
    val amount = varchar("amount", 20)
    val date = varchar("date", 50)
    val status = varchar("status", 20) // Paid, Pending, Failed

    override val primaryKey = PrimaryKey(id)
}
