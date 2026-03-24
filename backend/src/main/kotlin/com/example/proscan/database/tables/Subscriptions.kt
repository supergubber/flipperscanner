package com.example.proscan.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object SubscriptionPlans : Table("subscription_plans") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 50) // Free, Pro, Enterprise
    val price = varchar("price", 50)
    val highlight = bool("highlight").default(false)
    val sortOrder = integer("sort_order").default(0)

    override val primaryKey = PrimaryKey(id)
}

object PlanFeatures : Table("plan_features") {
    val id = long("id").autoIncrement()
    val featureName = varchar("feature_name", 100)
    val freeValue = varchar("free_value", 100)
    val proValue = varchar("pro_value", 100)
    val enterpriseValue = varchar("enterprise_value", 100)
    val sortOrder = integer("sort_order").default(0)

    override val primaryKey = PrimaryKey(id)
}

object UserSubscriptions : Table("user_subscriptions") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id).uniqueIndex()
    val planName = varchar("plan_name", 50).default("free")
    val scansUsed = integer("scans_used").default(0)
    val scansLimit = integer("scans_limit").default(30)
    val qrUsed = integer("qr_used").default(0)
    val qrLimit = integer("qr_limit").default(5)
    val storageUsedMb = integer("storage_used_mb").default(0)
    val storageLimitMb = integer("storage_limit_mb").default(100)
    val startDate = datetime("start_date").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}
