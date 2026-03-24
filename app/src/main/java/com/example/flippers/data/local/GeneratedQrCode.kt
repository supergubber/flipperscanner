package com.example.flippers.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generated_qr_codes")
data class GeneratedQrCode(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val content: String,
    val label: String,
    val imagePath: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
