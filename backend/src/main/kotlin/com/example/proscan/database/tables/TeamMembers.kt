package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table

object TeamMembers : Table("team_members") {
    val id = long("id").autoIncrement()
    val orgId = long("org_id").default(1)
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val role = varchar("role", 50) // Admin, Manager, Member
    val avatarInitial = varchar("avatar_initial", 10)
    val joinedDate = varchar("joined_date", 50)

    override val primaryKey = PrimaryKey(id)
}
