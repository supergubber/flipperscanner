package com.example.flippers.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GeneratedQrCodeDao {
    @Query("SELECT * FROM generated_qr_codes ORDER BY createdAt DESC")
    fun getAll(): Flow<List<GeneratedQrCode>>

    @Insert
    suspend fun insert(qrCode: GeneratedQrCode): Long

    @Delete
    suspend fun delete(qrCode: GeneratedQrCode)
}
