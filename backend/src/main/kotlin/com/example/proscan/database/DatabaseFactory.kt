package com.example.proscan.database

import com.example.proscan.database.tables.*
import com.example.proscan.util.PasswordHash
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.time.LocalDateTime

object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val url = config.property("database.url").getString()
        val driver = config.property("database.driver").getString()

        // Ensure data directory exists
        File("./data").mkdirs()

        val uploadDir = config.property("storage.uploadDir").getString()
        File(uploadDir).mkdirs()
        File("$uploadDir/documents").mkdirs()
        File("$uploadDir/qr_codes").mkdirs()
        File("$uploadDir/pdf").mkdirs()

        Database.connect(url, driver)

        transaction {
            SchemaUtils.create(
                Users,
                Documents,
                QrCodes,
                SubscriptionPlans,
                PlanFeatures,
                UserSubscriptions,
                TeamMembers,
                AuditLogs,
                Integrations,
                PaymentMethods,
                Transactions
            )

            // Seed data if tables are empty
            if (Users.selectAll().empty()) {
                seedData()
            }
        }
    }

    private fun seedData() {
        // Demo user
        val demoUserId = Users.insert {
            it[email] = "demo@proscan.com"
            it[passwordHash] = PasswordHash.hash("demo123")
            it[name] = "ProScan User"
            it[plan] = "free"
            it[profileCompleted] = true
            it[createdAt] = LocalDateTime.now()
            it[updatedAt] = LocalDateTime.now()
        }[Users.id]

        // Subscription plans
        SubscriptionPlans.insert { it[name] = "Free"; it[price] = "\$0"; it[highlight] = false; it[sortOrder] = 0 }
        SubscriptionPlans.insert { it[name] = "Pro"; it[price] = "\$4.99/mo"; it[highlight] = true; it[sortOrder] = 1 }
        SubscriptionPlans.insert { it[name] = "Enterprise"; it[price] = "\$19.99/mo"; it[highlight] = false; it[sortOrder] = 2 }

        // Plan features
        val features = listOf(
            listOf("Scans per month", "30", "Unlimited", "Unlimited"),
            listOf("QR Code generation", "5/month", "Unlimited", "Unlimited"),
            listOf("PDF Tools", "Basic", "All tools", "All tools"),
            listOf("Cloud backup", "—", "5 GB", "Unlimited"),
            listOf("Team management", "—", "—", "Up to 50 members"),
            listOf("Analytics dashboard", "—", "Basic", "Advanced"),
            listOf("Integrations", "—", "3 apps", "Unlimited"),
            listOf("Priority support", "—", "Email", "24/7 Phone & Email"),
            listOf("Audit log", "—", "—", "Full history"),
            listOf("Custom branding", "—", "—", "Yes")
        )
        features.forEachIndexed { index, f ->
            PlanFeatures.insert {
                it[featureName] = f[0]; it[freeValue] = f[1]; it[proValue] = f[2]; it[enterpriseValue] = f[3]
                it[sortOrder] = index
            }
        }

        // User subscription (usage stats)
        UserSubscriptions.insert {
            it[userId] = demoUserId
            it[planName] = "free"
            it[scansUsed] = 12; it[scansLimit] = 30
            it[qrUsed] = 3; it[qrLimit] = 5
            it[storageUsedMb] = 45; it[storageLimitMb] = 100
        }

        // Team members
        val teamMembers = listOf(
            listOf("John Smith", "john@acmecorp.com", "Admin", "JS", "Jan 15, 2025"),
            listOf("Sarah Johnson", "sarah@acmecorp.com", "Manager", "SJ", "Feb 3, 2025"),
            listOf("Mike Chen", "mike@acmecorp.com", "Member", "MC", "Mar 10, 2025"),
            listOf("Emily Davis", "emily@acmecorp.com", "Member", "ED", "Apr 1, 2025"),
            listOf("Alex Kumar", "alex@acmecorp.com", "Manager", "AK", "Apr 20, 2025"),
            listOf("Lisa Wang", "lisa@acmecorp.com", "Member", "LW", "May 5, 2025")
        )
        teamMembers.forEach { m ->
            TeamMembers.insert {
                it[name] = m[0]; it[email] = m[1]; it[role] = m[2]; it[avatarInitial] = m[3]; it[joinedDate] = m[4]
            }
        }

        // Audit log
        val auditEntries = listOf(
            Triple("John Smith", "Scanned document: Invoice_2025.pdf", "Mar 20, 2026 — 2:30 PM"),
            Triple("Admin", "Changed security settings", "Mar 20, 2026 — 1:15 PM"),
            Triple("Sarah Johnson", "Generated QR code", "Mar 19, 2026 — 4:45 PM"),
            Triple("Mike Chen", "Exported scan to PDF", "Mar 19, 2026 — 3:20 PM"),
            Triple("Admin", "Added new team member: Lisa Wang", "Mar 18, 2026 — 10:00 AM"),
            Triple("Emily Davis", "Scanned ID card", "Mar 18, 2026 — 9:30 AM"),
            Triple("Alex Kumar", "Merged 3 PDF files", "Mar 17, 2026 — 5:00 PM"),
            Triple("John Smith", "Updated profile settings", "Mar 17, 2026 — 2:00 PM"),
            Triple("Admin", "Enabled 2FA requirement", "Mar 16, 2026 — 11:00 AM"),
            Triple("Sarah Johnson", "Scanned book: Chapter 5", "Mar 16, 2026 — 9:15 AM")
        )
        auditEntries.forEach { (u, a, t) ->
            AuditLogs.insert { it[user] = u; it[action] = a; it[timestamp] = t }
        }

        // Integrations (for demo user)
        data class IntData(val n: String, val i: String, val d: String, val c: Boolean, val cat: String)
        val integrations = listOf(
            IntData("Slack", "Chat", "Send scan notifications to channels", false, "Productivity"),
            IntData("Google Drive", "CloudUpload", "Auto-backup scans to Drive", true, "Storage"),
            IntData("Dropbox", "Cloud", "Sync scans to Dropbox", false, "Storage"),
            IntData("Salesforce", "Business", "Attach scans to CRM records", false, "CRM"),
            IntData("HubSpot", "Hub", "Link documents to contacts", false, "CRM"),
            IntData("Stripe", "Payment", "Scan receipts for payment records", false, "Payment"),
            IntData("Shopify", "ShoppingCart", "Scan product barcodes", false, "Payment")
        )
        integrations.forEach { intg ->
            Integrations.insert {
                it[userId] = demoUserId; it[name] = intg.n; it[icon] = intg.i
                it[description] = intg.d; it[isConnected] = intg.c; it[category] = intg.cat
            }
        }

        // Payment methods
        data class PMData(val n: String, val i: String, val l: String, val d: Boolean)
        val paymentMethods = listOf(
            PMData("Credit Card", "CreditCard", "4242", true),
            PMData("PayPal", "AccountBalance", "", false),
            PMData("UPI", "PhoneAndroid", "", false),
            PMData("Google Pay", "Wallet", "", false)
        )
        paymentMethods.forEach { pm ->
            PaymentMethods.insert {
                it[userId] = demoUserId; it[name] = pm.n; it[icon] = pm.i
                it[lastFour] = pm.l; it[isDefault] = pm.d
            }
        }

        // Transactions
        data class TxData(val desc: String, val amt: String, val dt: String, val st: String)
        val transactions = listOf(
            TxData("Pro Plan — Monthly", "\$4.99", "Mar 15, 2026", "Paid"),
            TxData("Pro Plan — Monthly", "\$4.99", "Feb 15, 2026", "Paid"),
            TxData("Pro Plan — Monthly", "\$4.99", "Jan 15, 2026", "Paid"),
            TxData("Enterprise Upgrade", "\$19.99", "Dec 20, 2025", "Failed"),
            TxData("Pro Plan — Monthly", "\$4.99", "Dec 15, 2025", "Paid"),
            TxData("Pro Plan — Annual", "\$49.99", "Nov 1, 2025", "Pending")
        )
        transactions.forEach { tx ->
            Transactions.insert {
                it[userId] = demoUserId; it[description] = tx.desc
                it[amount] = tx.amt; it[date] = tx.dt; it[status] = tx.st
            }
        }

        // QR history
        data class QrData(val t: String, val c: String, val l: String, val d: String)
        val qrHistory = listOf(
            QrData("URL", "https://example.com", "My Website", "Mar 20, 2026"),
            QrData("WiFi", "SSID: HomeNetwork", "Home WiFi", "Mar 19, 2026"),
            QrData("Text", "Hello World!", "Greeting", "Mar 18, 2026"),
            QrData("Contact", "John Doe — john@example.com", "John's Contact", "Mar 17, 2026"),
            QrData("Email", "support@proscan.com", "Support Email", "Mar 16, 2026"),
            QrData("Phone", "+1 555-0123", "Office Phone", "Mar 15, 2026")
        )
        qrHistory.forEach { qr ->
            QrCodes.insert {
                it[userId] = demoUserId; it[type] = qr.t; it[content] = qr.c; it[label] = qr.l
            }
        }
    }
}
