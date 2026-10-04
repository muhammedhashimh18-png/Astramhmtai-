package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.QuizRecordEntity
import com.example.data.local.entities.SavedCreationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AstramDao {
    // Chat messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()

    // Saved creations
    @Query("SELECT * FROM saved_creations ORDER BY timestamp DESC")
    fun getAllSavedCreations(): Flow<List<SavedCreationEntity>>

    @Query("SELECT * FROM saved_creations WHERE category = :category ORDER BY timestamp DESC")
    fun getCreationsByCategory(category: String): Flow<List<SavedCreationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreation(creation: SavedCreationEntity): Long

    @Query("DELETE FROM saved_creations WHERE id = :id")
    suspend fun deleteCreation(id: Long)

    @Query("UPDATE saved_creations SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long)

    // Quiz records
    @Query("SELECT * FROM quiz_records ORDER BY timestamp DESC")
    fun getAllQuizRecords(): Flow<List<QuizRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizRecord(record: QuizRecordEntity): Long
}
