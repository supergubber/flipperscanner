package com.example.proscan.service

import com.example.proscan.models.responses.AnalyticsDashboardResponse
import com.example.proscan.models.responses.ScanTypeStatResponse

object AnalyticsService {

    fun getDashboard(userId: Long): AnalyticsDashboardResponse {
        // Use real data from DocumentService if available, fallback to seeded data
        val totalScans = DocumentService.countByUser(userId).toInt()
        val actualTotal = if (totalScans > 0) totalScans else 88 // fallback to dummy sum

        val scanTypeBreakdown = if (totalScans > 0) {
            listOf("Document", "QR Code", "Book", "ID Card").map { type ->
                ScanTypeStatResponse(type, DocumentService.countByUserAndType(userId, type).toInt())
            }.filter { it.count > 0 }
        } else {
            // Dummy data matching DummyData.kt
            listOf(
                ScanTypeStatResponse("Document", 45),
                ScanTypeStatResponse("QR Code", 23),
                ScanTypeStatResponse("Book", 12),
                ScanTypeStatResponse("ID Card", 8)
            )
        }

        val weeklyScans = listOf(5, 12, 8, 15, 10, 18, 7)
        val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        val mostUsed = scanTypeBreakdown.maxByOrNull { it.count }?.type ?: "Document"
        val dailyAvg = if (weeklyScans.isNotEmpty()) weeklyScans.sum() / weeklyScans.size else 0

        return AnalyticsDashboardResponse(
            totalScans = actualTotal,
            dailyAverage = dailyAvg,
            mostUsedType = mostUsed,
            todayScans = weeklyScans.lastOrNull() ?: 0,
            weeklyScans = weeklyScans,
            weekDays = weekDays,
            scanTypeBreakdown = scanTypeBreakdown
        )
    }

    fun getScanStats(userId: Long, period: String): AnalyticsDashboardResponse {
        // For now, return same dashboard data. Could be extended with period-based filtering.
        return getDashboard(userId)
    }
}
