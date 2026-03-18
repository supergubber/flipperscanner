package com.example.flippers.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, ScannedDocument::class],
    version = 2,
    exportSchema = true
)
abstract class ProScanDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun scannedDocumentDao(): ScannedDocumentDao

    companion object {
        @Volatile
        private var INSTANCE: ProScanDatabase? = null

        fun getDatabase(context: Context): ProScanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProScanDatabase::class.java,
                    "proscan_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
