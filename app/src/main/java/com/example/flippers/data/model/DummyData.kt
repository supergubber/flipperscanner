package com.example.flippers.data.model

// --- Subscription ---

sealed class SubscriptionTier(val name: String, val price: String) {
    data object Free : SubscriptionTier("Free", "$0")
    data object Pro : SubscriptionTier("Pro", "$4.99/mo")
    data object Enterprise : SubscriptionTier("Enterprise", "$19.99/mo")
}

data class PlanFeature(
    val name: String,
    val freeValue: String,
    val proValue: String,
    val enterpriseValue: String
)

val dummyPlanFeatures = listOf(
    PlanFeature("Scans per month", "30", "Unlimited", "Unlimited"),
    PlanFeature("QR Code generation", "5/month", "Unlimited", "Unlimited"),
    PlanFeature("PDF Tools", "Basic", "All tools", "All tools"),
    PlanFeature("Cloud backup", "—", "5 GB", "Unlimited"),
    PlanFeature("Team management", "—", "—", "Up to 50 members"),
    PlanFeature("Analytics dashboard", "—", "Basic", "Advanced"),
    PlanFeature("Integrations", "—", "3 apps", "Unlimited"),
    PlanFeature("Priority support", "—", "Email", "24/7 Phone & Email"),
    PlanFeature("Audit log", "—", "—", "Full history"),
    PlanFeature("Custom branding", "—", "—", "Yes")
)

data class UsageStats(
    val scansUsed: Int = 12,
    val scansLimit: Int = 30,
    val qrUsed: Int = 3,
    val qrLimit: Int = 5,
    val storageUsedMb: Int = 45,
    val storageLimitMb: Int = 100
)

// --- Analytics ---

val dummyWeeklyScans = listOf(5, 12, 8, 15, 10, 18, 7)
val dummyWeekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

data class ScanTypeStat(val type: String, val count: Int)

val dummyScanTypeBreakdown = listOf(
    ScanTypeStat("Document", 45),
    ScanTypeStat("QR Code", 23),
    ScanTypeStat("Book", 12),
    ScanTypeStat("ID Card", 8)
)

// --- Enterprise / Team ---

data class TeamMember(
    val name: String,
    val email: String,
    val role: String,
    val avatarInitial: String,
    val joinedDate: String
)

val dummyTeamMembers = listOf(
    TeamMember("John Smith", "john@acmecorp.com", "Admin", "JS", "Jan 15, 2025"),
    TeamMember("Sarah Johnson", "sarah@acmecorp.com", "Manager", "SJ", "Feb 3, 2025"),
    TeamMember("Mike Chen", "mike@acmecorp.com", "Member", "MC", "Mar 10, 2025"),
    TeamMember("Emily Davis", "emily@acmecorp.com", "Member", "ED", "Apr 1, 2025"),
    TeamMember("Alex Kumar", "alex@acmecorp.com", "Manager", "AK", "Apr 20, 2025"),
    TeamMember("Lisa Wang", "lisa@acmecorp.com", "Member", "LW", "May 5, 2025")
)

data class AuditEntry(
    val user: String,
    val action: String,
    val timestamp: String
)

val dummyAuditLog = listOf(
    AuditEntry("John Smith", "Scanned document: Invoice_2025.pdf", "Mar 20, 2026 — 2:30 PM"),
    AuditEntry("Admin", "Changed security settings", "Mar 20, 2026 — 1:15 PM"),
    AuditEntry("Sarah Johnson", "Generated QR code", "Mar 19, 2026 — 4:45 PM"),
    AuditEntry("Mike Chen", "Exported scan to PDF", "Mar 19, 2026 — 3:20 PM"),
    AuditEntry("Admin", "Added new team member: Lisa Wang", "Mar 18, 2026 — 10:00 AM"),
    AuditEntry("Emily Davis", "Scanned ID card", "Mar 18, 2026 — 9:30 AM"),
    AuditEntry("Alex Kumar", "Merged 3 PDF files", "Mar 17, 2026 — 5:00 PM"),
    AuditEntry("John Smith", "Updated profile settings", "Mar 17, 2026 — 2:00 PM"),
    AuditEntry("Admin", "Enabled 2FA requirement", "Mar 16, 2026 — 11:00 AM"),
    AuditEntry("Sarah Johnson", "Scanned book: Chapter 5", "Mar 16, 2026 — 9:15 AM")
)

data class OrgStats(
    val name: String = "Acme Corp",
    val members: Int = 12,
    val totalScans: Int = 1547,
    val plan: String = "Enterprise"
)

// --- Integrations ---

data class Integration(
    val name: String,
    val icon: String, // Material icon name hint
    val description: String,
    val isConnected: Boolean,
    val category: String
)

val dummyIntegrations = listOf(
    Integration("Slack", "Chat", "Send scan notifications to channels", false, "Productivity"),
    Integration("Google Drive", "CloudUpload", "Auto-backup scans to Drive", true, "Storage"),
    Integration("Dropbox", "Cloud", "Sync scans to Dropbox", false, "Storage"),
    Integration("Salesforce", "Business", "Attach scans to CRM records", false, "CRM"),
    Integration("HubSpot", "Hub", "Link documents to contacts", false, "CRM"),
    Integration("Stripe", "Payment", "Scan receipts for payment records", false, "Payment"),
    Integration("Shopify", "ShoppingCart", "Scan product barcodes", false, "Payment")
)

// --- Payment ---

data class PaymentMethod(
    val name: String,
    val icon: String,
    val lastFour: String = "",
    val isDefault: Boolean = false
)

val dummyPaymentMethods = listOf(
    PaymentMethod("Credit Card", "CreditCard", "4242", true),
    PaymentMethod("PayPal", "AccountBalance", ""),
    PaymentMethod("UPI", "PhoneAndroid", ""),
    PaymentMethod("Google Pay", "Wallet", "")
)

data class Transaction(
    val description: String,
    val amount: String,
    val date: String,
    val status: String // "Paid", "Pending", "Failed"
)

val dummyTransactions = listOf(
    Transaction("Pro Plan — Monthly", "$4.99", "Mar 15, 2026", "Paid"),
    Transaction("Pro Plan — Monthly", "$4.99", "Feb 15, 2026", "Paid"),
    Transaction("Pro Plan — Monthly", "$4.99", "Jan 15, 2026", "Paid"),
    Transaction("Enterprise Upgrade", "$19.99", "Dec 20, 2025", "Failed"),
    Transaction("Pro Plan — Monthly", "$4.99", "Dec 15, 2025", "Paid"),
    Transaction("Pro Plan — Annual", "$49.99", "Nov 1, 2025", "Pending")
)

// --- QR History Dummy ---

data class QrHistoryItem(
    val type: String,
    val content: String,
    val label: String,
    val createdAt: String
)

val dummyQrHistory = listOf(
    QrHistoryItem("URL", "https://example.com", "My Website", "Mar 20, 2026"),
    QrHistoryItem("WiFi", "SSID: HomeNetwork", "Home WiFi", "Mar 19, 2026"),
    QrHistoryItem("Text", "Hello World!", "Greeting", "Mar 18, 2026"),
    QrHistoryItem("Contact", "John Doe — john@example.com", "John's Contact", "Mar 17, 2026"),
    QrHistoryItem("Email", "support@proscan.com", "Support Email", "Mar 16, 2026"),
    QrHistoryItem("Phone", "+1 555-0123", "Office Phone", "Mar 15, 2026")
)
