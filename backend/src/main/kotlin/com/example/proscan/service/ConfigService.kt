package com.example.proscan.service

import com.example.proscan.database.tables.PlanFeatures
import com.example.proscan.database.tables.SubscriptionPlans
import com.example.proscan.models.responses.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object ConfigService {

    fun getAppConfig(): AppConfigResponse {
        val plans = transaction {
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
                    features = features.map { (featureName, freeVal, proEnterprise) ->
                        PlanFeatureResponse(
                            name = featureName,
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

        return AppConfigResponse(
            theme = ThemeConfig(
                primaryColor = "#EE9E8E",
                primaryDark = "#D4856F",
                primaryLight = "#F5C4B8",
                accentBlue = "#4A90D9",
                accentGreen = "#34A853",
                accentOrange = "#E8853D",
                accentPurple = "#9B6DD7",
                accentRed = "#E05555",
                accentTeal = "#2ABFBF"
            ),
            bottomNavTabs = listOf(
                NavTabConfig("Home", "home", "Home"),
                NavTabConfig("QR", "qr_generator", "QrCode2"),
                NavTabConfig("Analytics", "analytics_dashboard", "Analytics"),
                NavTabConfig("Settings", "settings", "Settings")
            ),
            centerButton = NavTabConfig("Scan", "document_scanner", "DocumentScanner"),
            bottomNavVisibleOn = listOf("home", "qr_generator", "analytics_dashboard", "settings", "profile"),
            homeQuickActions = listOf(
                QuickActionConfig("QrCodeScanner", "Scan Code", "Read QR & barcodes", "#4A90D9", "qr_code_scanner"),
                QuickActionConfig("QrCode2", "Generate QR", "Create any QR code", "#9B6DD7", "qr_generator"),
                QuickActionConfig("PictureAsPdf", "PDF Tools", "Edit & merge PDFs", "#E8853D", "pdf_tools"),
                QuickActionConfig("BarChart", "Analytics", "View scan stats", "#34A853", "analytics_dashboard")
            ),
            scannerTypes = listOf(
                ToolItemConfig("document", "Document", "DocumentScanner"),
                ToolItemConfig("qr_code", "QR Code", "QrCode2"),
                ToolItemConfig("book", "Book", "Book"),
                ToolItemConfig("id_card", "ID Card", "Badge"),
                ToolItemConfig("business_card", "Business Card", "ContactPage"),
                ToolItemConfig("whiteboard", "Whiteboard", "Dashboard")
            ),
            qrTypes = listOf(
                ToolItemConfig("URL", "URL", "Language", "#4A90D9"),
                ToolItemConfig("Text", "Text", "TextFields", "#34A853"),
                ToolItemConfig("WiFi", "WiFi", "Wifi", "#9B6DD7"),
                ToolItemConfig("Contact", "Contact", "ContactPhone", "#E8853D"),
                ToolItemConfig("Payment", "Payment", "Payment", "#E05555"),
                ToolItemConfig("Email", "Email", "Email", "#2ABFBF"),
                ToolItemConfig("Phone", "Phone", "Phone", "#5C6BC0")
            ),
            pdfTools = listOf(
                ToolItemConfig("watermark", "Watermark", "BrandingWatermark", route = "watermark"),
                ToolItemConfig("esign", "eSign", "Draw", route = "esign_pdf"),
                ToolItemConfig("split", "Split", "CallSplit", route = "split_pdf"),
                ToolItemConfig("merge", "Merge PDFs", "MergeType", route = "merge_pdf"),
                ToolItemConfig("protect", "Protect", "Lock", route = "protect_pdf"),
                ToolItemConfig("compress", "Compress", "Compress", route = "compress_pdf"),
                ToolItemConfig("convert", "Convert", "SwapHoriz", route = "convert_pdf")
            ),
            subscriptionPlans = plans,
            featureFlags = FeatureFlagsConfig(
                showUpgradeBanner = true,
                enablePdfTools = true,
                enableEnterprise = true,
                enableAnalytics = true,
                enableIntegrations = true,
                enableCloudBackup = false
            ),
            settingsSections = listOf(
                SettingsSectionConfig("Account", listOf(
                    SettingsItemConfig("Person", "Profile", "#4A90D9", "profile"),
                    SettingsItemConfig("Star", "Subscription", "#E8853D", "subscription"),
                    SettingsItemConfig("CreditCard", "Payment Methods", "#34A853", "payment_methods"),
                    SettingsItemConfig("BarChart", "Usage Dashboard", "#9B6DD7", "usage_dashboard")
                )),
                SettingsSectionConfig("Enterprise", listOf(
                    SettingsItemConfig("AdminPanelSettings", "Admin Dashboard", "#E05555", "admin_dashboard"),
                    SettingsItemConfig("Extension", "Integrations", "#2ABFBF", "integrations")
                )),
                SettingsSectionConfig("App", listOf(
                    SettingsItemConfig("Language", "Language", "#4A90D9", "language"),
                    SettingsItemConfig("DarkMode", "Theme", "#9B6DD7", "theme"),
                    SettingsItemConfig("Notifications", "Notifications", "#E8853D", "notifications")
                )),
                SettingsSectionConfig("About", listOf(
                    SettingsItemConfig("Info", "Version", "#34A853", "version"),
                    SettingsItemConfig("Star", "Rate App", "#E8853D", "rate"),
                    SettingsItemConfig("Security", "Privacy Policy", "#4A90D9", "privacy"),
                    SettingsItemConfig("Help", "Support", "#9B6DD7", "support")
                ))
            ),
            integrations = listOf(
                IntegrationCatalogItem("Slack", "Chat", "Send scan notifications to channels", "Productivity"),
                IntegrationCatalogItem("Google Drive", "CloudUpload", "Auto-backup scans to Drive", "Storage"),
                IntegrationCatalogItem("Dropbox", "Cloud", "Sync scans to Dropbox", "Storage"),
                IntegrationCatalogItem("Salesforce", "Business", "Attach scans to CRM records", "CRM"),
                IntegrationCatalogItem("HubSpot", "Hub", "Link documents to contacts", "CRM"),
                IntegrationCatalogItem("Stripe", "Payment", "Scan receipts for payment records", "Payment"),
                IntegrationCatalogItem("Shopify", "ShoppingCart", "Scan product barcodes", "Payment")
            )
        )
    }
}
