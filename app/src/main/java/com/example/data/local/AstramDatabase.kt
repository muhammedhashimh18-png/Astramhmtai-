package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AstramDao
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.QuizRecordEntity
import com.example.data.local.entities.SavedCreationEntity

@Database(
    entities = [
        ChatMessageEntity::class,
        SavedCreationEntity::class,
        QuizRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AstramDatabase : RoomDatabase() {
    abstract fun astramDao(): AstramDao

    companion object {
        @Volatile
        private var INSTANCE: AstramDatabase? = null

        fun getDatabase(context: Context): AstramDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AstramDatabase::class.java,
                    "astram_hmt_database"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
