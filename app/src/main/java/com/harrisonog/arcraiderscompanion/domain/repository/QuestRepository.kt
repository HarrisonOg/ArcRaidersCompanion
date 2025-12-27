package com.harrisonog.arcraiderscompanion.domain.repository

import com.harrisonog.arcraiderscompanion.domain.model.Quest
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface QuestRepository {
    fun getAllQuests(): Flow<List<Quest>>

    fun getQuestById(questId: String): Flow<Quest?>

    fun getQuestsByStatus(status: QuestStatus): Flow<List<Quest>>

    fun getQuestsByChain(questChain: String): Flow<List<Quest>>

    suspend fun refreshQuests(): Result<Unit>

    suspend fun updateQuestStatus(questId: String, status: QuestStatus): Result<Unit>

    suspend fun updateCompletedObjectives(questId: String, completedObjectives: Set<String>): Result<Unit>

    suspend fun syncQuests(): Result<Unit>
}
