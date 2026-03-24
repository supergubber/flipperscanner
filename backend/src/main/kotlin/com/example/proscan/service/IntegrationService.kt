package com.example.proscan.service

import com.example.proscan.database.tables.Integrations
import com.example.proscan.models.responses.IntegrationConfigResponse
import com.example.proscan.models.responses.IntegrationResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object IntegrationService {

    // In-memory config store (keyed by "userId:integrationId")
    private val configStore = mutableMapOf<String, IntegrationConfigResponse>()

    fun getAll(userId: Long): List<IntegrationResponse> {
        return transaction {
            Integrations.selectAll().where { Integrations.userId eq userId }
                .orderBy(Integrations.id)
                .map { row ->
                    IntegrationResponse(
                        id = row[Integrations.id],
                        name = row[Integrations.name],
                        icon = row[Integrations.icon],
                        description = row[Integrations.description],
                        isConnected = row[Integrations.isConnected],
                        category = row[Integrations.category]
                    )
                }
        }
    }

    fun connect(userId: Long, integrationId: Long): IntegrationResponse {
        return transaction {
            Integrations.update({
                (Integrations.id eq integrationId) and (Integrations.userId eq userId)
            }) {
                it[isConnected] = true
            }
            getIntegration(integrationId, userId)
        }
    }

    fun disconnect(userId: Long, integrationId: Long): IntegrationResponse {
        return transaction {
            Integrations.update({
                (Integrations.id eq integrationId) and (Integrations.userId eq userId)
            }) {
                it[isConnected] = false
            }
            getIntegration(integrationId, userId)
        }
    }

    fun getConfig(userId: Long, integrationId: Long): IntegrationConfigResponse {
        val integration = transaction {
            Integrations.selectAll().where {
                (Integrations.id eq integrationId) and (Integrations.userId eq userId)
            }.firstOrNull() ?: throw NoSuchElementException("Integration not found")
        }
        val key = "$userId:$integrationId"
        return configStore.getOrDefault(key, IntegrationConfigResponse(
            integrationId = integrationId,
            integrationName = integration[Integrations.name],
            apiKey = "",
            webhookUrl = ""
        ))
    }

    fun saveConfig(userId: Long, integrationId: Long, apiKey: String, webhookUrl: String): IntegrationConfigResponse {
        val integration = transaction {
            Integrations.selectAll().where {
                (Integrations.id eq integrationId) and (Integrations.userId eq userId)
            }.firstOrNull() ?: throw NoSuchElementException("Integration not found")
        }
        val key = "$userId:$integrationId"
        val config = IntegrationConfigResponse(
            integrationId = integrationId,
            integrationName = integration[Integrations.name],
            apiKey = apiKey,
            webhookUrl = webhookUrl
        )
        configStore[key] = config
        return config
    }

    private fun getIntegration(integrationId: Long, userId: Long): IntegrationResponse {
        return Integrations.selectAll().where {
            (Integrations.id eq integrationId) and (Integrations.userId eq userId)
        }.firstOrNull()?.let { row ->
            IntegrationResponse(
                row[Integrations.id], row[Integrations.name], row[Integrations.icon],
                row[Integrations.description], row[Integrations.isConnected], row[Integrations.category]
            )
        } ?: throw NoSuchElementException("Integration not found")
    }
}
