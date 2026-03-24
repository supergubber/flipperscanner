package com.example.proscan.service

import com.example.proscan.database.tables.*
import com.example.proscan.models.responses.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object SubscriptionService {

    fun getPlans(): List<SubscriptionPlanResponse> {
        return transaction {
            val features = PlanFeatures.selectAll().orderBy(PlanFeatures.sortOrder).map { row ->
                Triple(row[PlanFeatures.featureName], row[PlanFeatures.freeValue],
                    row[PlanFeatures.proValue] to row[PlanFeatures.enterpriseValue])
            }

            SubscriptionPlans.selectAll().orderBy(SubscriptionPlans.sortOrder).map { row ->
                val planName = row[SubscriptionPlans.name]
                SubscriptionPlanResponse(
                    name = planName,
                    price = row[SubscriptionPlans.price],
                    highlight = row[SubscriptionPlans.highlight],
                    features = features.map { (name, freeVal, proEnterprise) ->
                        PlanFeatureResponse(
                            name = name,
                            value = when (planName) {
                                "Free" -> freeVal
                                "Pro" -> proEnterprise.first
                                "Enterprise" -> proEnterprise.second
                                else -> freeVal
                            }
                        )
                    }
                )
            }
        }
    }

    fun getCurrentSubscription(userId: Long): CurrentSubscriptionResponse {
        return transaction {
            val sub = UserSubscriptions.selectAll().where { UserSubscriptions.userId eq userId }
                .firstOrNull() ?: throw NoSuchElementException("No subscription found")

            CurrentSubscriptionResponse(
                plan = sub[UserSubscriptions.planName],
                usage = UsageStatsResponse(
                    scansUsed = sub[UserSubscriptions.scansUsed],
                    scansLimit = sub[UserSubscriptions.scansLimit],
                    qrUsed = sub[UserSubscriptions.qrUsed],
                    qrLimit = sub[UserSubscriptions.qrLimit],
                    storageUsedMb = sub[UserSubscriptions.storageUsedMb],
                    storageLimitMb = sub[UserSubscriptions.storageLimitMb]
                )
            )
        }
    }

    fun upgrade(userId: Long, plan: String): CurrentSubscriptionResponse {
        val limits = when (plan.lowercase()) {
            "pro" -> mapOf("scans" to -1, "qr" to -1, "storage" to 5120)
            "enterprise" -> mapOf("scans" to -1, "qr" to -1, "storage" to -1)
            else -> mapOf("scans" to 30, "qr" to 5, "storage" to 100)
        }

        transaction {
            UserSubscriptions.update({ UserSubscriptions.userId eq userId }) {
                it[planName] = plan.lowercase()
                it[scansLimit] = limits["scans"]!!
                it[qrLimit] = limits["qr"]!!
                it[storageLimitMb] = limits["storage"]!!
            }
            Users.update({ Users.id eq userId }) {
                it[Users.plan] = plan.lowercase()
            }
        }

        return getCurrentSubscription(userId)
    }

    fun getUsage(userId: Long): UsageStatsResponse {
        return transaction {
            val sub = UserSubscriptions.selectAll().where { UserSubscriptions.userId eq userId }
                .firstOrNull() ?: throw NoSuchElementException("No subscription found")

            UsageStatsResponse(
                scansUsed = sub[UserSubscriptions.scansUsed],
                scansLimit = sub[UserSubscriptions.scansLimit],
                qrUsed = sub[UserSubscriptions.qrUsed],
                qrLimit = sub[UserSubscriptions.qrLimit],
                storageUsedMb = sub[UserSubscriptions.storageUsedMb],
                storageLimitMb = sub[UserSubscriptions.storageLimitMb]
            )
        }
    }
}
