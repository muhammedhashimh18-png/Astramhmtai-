package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: String = "default",
    val role: String, // "user" or "model"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelName: String = "gemini-3.5-flash"
)

@Entity(tableName = "saved_creations")
data class SavedCreationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // "IMAGE", "VIDEO", "ANIMATION", "STORY", "MOVIE", "MESSAGE", "TRANSLATION", "3D"
    val title: String,
    val content: String,
    val prompt: String,
    val style: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

@Entity(tableName = "quiz_records")
data class QuizRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topic: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Float,
    val timestamp: Long = System.currentTimeMillis()
)
