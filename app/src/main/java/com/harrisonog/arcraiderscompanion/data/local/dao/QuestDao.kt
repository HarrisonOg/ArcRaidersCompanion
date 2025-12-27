package com.harrisonog.arcraiderscompanion.data.local.dao

import androidx.room.*
import com.harrisonog.arcraiderscompanion.data.local.entity.QuestEntity
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    @Query("SELECT * FROM quests ORDER BY name ASC")
    fun getAllQuests(): Flow<List<QuestEntity>>

    @Query("SELECT * FROM quests WHERE id = :questId")
    fun getQuestById(questId: String): Flow<QuestEntity?>

    @Query("SELECT * FROM quests WHERE status = :status ORDER BY name ASC")
    fun getQuestsByStatus(status: QuestStatus): Flow<List<QuestEntity>>

    @Query("SELECT * FROM quests WHERE questChain = :questChain ORDER BY name ASC")
    fun getQuestsByChain(questChain: String): Flow<List<QuestEntity>>

    @Query("SELECT * FROM quests WHERE id = :questId")
    suspend fun getQuestByIdSync(questId: String): QuestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: QuestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(quests: List<QuestEntity>)

    @Update
    suspend fun updateQuest(quest: QuestEntity)

    @Delete
    suspend fun deleteQuest(quest: QuestEntity)

    @Query("DELETE FROM quests")
    suspend fun deleteAllQuests()

    @Query("UPDATE quests SET status = :status WHERE id = :questId")
    suspend fun updateQuestStatus(questId: String, status: QuestStatus)

    @Query("UPDATE quests SET completedObjectives = :completedObjectives WHERE id = :questId")
    suspend fun updateCompletedObjectives(questId: String, completedObjectives: Set<String>)

    @Query("SELECT COUNT(*) FROM quests")
    suspend fun getQuestCount(): Int

    @Query("SELECT COUNT(*) FROM quests WHERE status = :status")
    suspend fun getQuestCountByStatus(status: QuestStatus): Int
}
