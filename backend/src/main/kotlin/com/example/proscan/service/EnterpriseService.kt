package com.example.proscan.service

import com.example.proscan.database.tables.AuditLogs
import com.example.proscan.database.tables.Documents
import com.example.proscan.database.tables.TeamMembers
import com.example.proscan.models.responses.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object EnterpriseService {

    fun getDashboard(): OrgStatsResponse {
        return transaction {
            val memberCount = TeamMembers.selectAll().count().toInt()
            val totalScans = Documents.selectAll().count().toInt().let { if (it == 0) 1547 else it }
            OrgStatsResponse(
                name = "Acme Corp",
                members = memberCount,
                totalScans = totalScans,
                plan = "Enterprise"
            )
        }
    }

    fun getTeamMembers(): List<TeamMemberResponse> {
        return transaction {
            TeamMembers.selectAll().orderBy(TeamMembers.id).map { row ->
                TeamMemberResponse(
                    id = row[TeamMembers.id],
                    name = row[TeamMembers.name],
                    email = row[TeamMembers.email],
                    role = row[TeamMembers.role],
                    avatarInitial = row[TeamMembers.avatarInitial],
                    joinedDate = row[TeamMembers.joinedDate]
                )
            }
        }
    }

    fun addTeamMember(name: String, email: String, role: String): TeamMemberResponse {
        return transaction {
            val initials = name.split(" ").take(2).map { it.first().uppercaseChar() }.joinToString("")
            val id = TeamMembers.insert {
                it[TeamMembers.name] = name
                it[TeamMembers.email] = email
                it[TeamMembers.role] = role
                it[avatarInitial] = initials
                it[joinedDate] = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy"))
            }[TeamMembers.id]

            AuditLogs.insert {
                it[user] = "Admin"
                it[action] = "Added new team member: $name"
                it[timestamp] = java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy — h:mm a")
                )
            }

            TeamMemberResponse(id, name, email, role, initials,
                java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")))
        }
    }

    fun updateTeamMember(memberId: Long, role: String): TeamMemberResponse {
        return transaction {
            TeamMembers.update({ TeamMembers.id eq memberId }) {
                it[TeamMembers.role] = role
            }
            TeamMembers.selectAll().where { TeamMembers.id eq memberId }.firstOrNull()?.let { row ->
                TeamMemberResponse(
                    row[TeamMembers.id], row[TeamMembers.name], row[TeamMembers.email],
                    row[TeamMembers.role], row[TeamMembers.avatarInitial], row[TeamMembers.joinedDate]
                )
            } ?: throw NoSuchElementException("Team member not found")
        }
    }

    fun removeTeamMember(memberId: Long) {
        transaction {
            val row = TeamMembers.selectAll().where { TeamMembers.id eq memberId }.firstOrNull()
                ?: throw NoSuchElementException("Team member not found")
            val memberName = row[TeamMembers.name]
            TeamMembers.deleteWhere { TeamMembers.id eq memberId }
            AuditLogs.insert {
                it[user] = "Admin"
                it[action] = "Removed team member: $memberName"
                it[timestamp] = java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy — h:mm a")
                )
            }
        }
    }

    fun getAuditLog(page: Int, size: Int): Pair<List<AuditEntryResponse>, Long> {
        return transaction {
            val total = AuditLogs.selectAll().count()
            val entries = AuditLogs.selectAll()
                .orderBy(AuditLogs.id, SortOrder.DESC)
                .limit(size).offset(((page - 1) * size).toLong())
                .map { row ->
                    AuditEntryResponse(
                        id = row[AuditLogs.id],
                        user = row[AuditLogs.user],
                        action = row[AuditLogs.action],
                        timestamp = row[AuditLogs.timestamp]
                    )
                }
            Pair(entries, total)
        }
    }

    // Simple in-memory security settings (could be persisted in a table)
    private var securitySettings = SecuritySettingsResponse(
        require2fa = false,
        passwordMinLength = 8,
        sessionTimeoutMinutes = 30,
        dataRetention = "90 days",
        autoLockTimeout = "5 minutes"
    )

    fun getSecuritySettings(): SecuritySettingsResponse = securitySettings

    fun updateSecuritySettings(
        require2fa: Boolean, passwordMinLength: Int, sessionTimeoutMinutes: Int,
        dataRetention: String, autoLockTimeout: String
    ): SecuritySettingsResponse {
        securitySettings = SecuritySettingsResponse(
            require2fa, passwordMinLength, sessionTimeoutMinutes, dataRetention, autoLockTimeout
        )

        transaction {
            AuditLogs.insert {
                it[user] = "Admin"
                it[action] = "Updated security settings (2FA: $require2fa, min password: $passwordMinLength)"
                it[timestamp] = java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy — h:mm a")
                )
            }
        }

        return securitySettings
    }
}
