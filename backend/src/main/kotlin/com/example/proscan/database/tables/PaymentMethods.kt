package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table

object PaymentMethods : Table("payment_methods") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id)
    val name = varchar("name", 100)
    val icon = varchar("icon", 50)
    val lastFour = varchar("last_four", 4).default("")
    val isDefault = bool("is_default").default(false)

    override val primaryKey = PrimaryKey(id)
}
