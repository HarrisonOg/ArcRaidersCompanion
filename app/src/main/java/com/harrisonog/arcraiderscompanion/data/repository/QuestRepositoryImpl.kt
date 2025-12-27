package com.harrisonog.arcraiderscompanion.data.repository

import com.harrisonog.arcraiderscompanion.data.local.dao.QuestDao
import com.harrisonog.arcraiderscompanion.data.local.entity.toDomain
import com.harrisonog.arcraiderscompanion.data.local.entity.toEntity
import com.harrisonog.arcraiderscompanion.data.mapper.toDomain
import com.harrisonog.arcraiderscompanion.data.remote.MetaForgeApi
import com.harrisonog.arcraiderscompanion.domain.model.Quest
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus
import com.harrisonog.arcraiderscompanion.domain.repository.QuestRepository
import com.harrisonog.arcraiderscompanion.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestRepositoryImpl @Inject constructor(
    private val api: MetaForgeApi,
    private val questDao: QuestDao
) : QuestRepository {

    override fun getAllQuests(): Flow<List<Quest>> {
        return questDao.getAllQuests().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getQuestById(questId: String): Flow<Quest?> {
        return questDao.getQuestById(questId).map { it?.toDomain() }
    }

    override fun getQuestsByStatus(status: QuestStatus): Flow<List<Quest>> {
        return questDao.getQuestsByStatus(status).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getQuestsByChain(questChain: String): Flow<List<Quest>> {
        return questDao.getQuestsByChain(questChain).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshQuests(): Result<Unit> {
        return try {
            val questDtos = api.getQuests()
            val quests = questDtos.mapNotNull { it.toDomain() }

            quests.forEach { newQuest ->
                val existingQuest = questDao.getQuestByIdSync(newQuest.id)
                if (existingQuest != null) {
                    val updatedQuest = newQuest.copy(
                        status = existingQuest.status,
                        completedObjectives = existingQuest.completedObjectives
                    )
                    questDao.insertQuest(updatedQuest.toEntity())
                } else {
                    questDao.insertQuest(newQuest.toEntity())
                }
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateQuestStatus(questId: String, status: QuestStatus): Result<Unit> {
        return try {
            questDao.updateQuestStatus(questId, status)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateCompletedObjectives(
        questId: String,
        completedObjectives: Set<String>
    ): Result<Unit> {
        return try {
            questDao.updateCompletedObjectives(questId, completedObjectives)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun syncQuests(): Result<Unit> {
        val questCount = questDao.getQuestCount()
        return if (questCount == 0) {
            refreshQuests()
        } else {
            Result.Success(Unit)
        }
    }
}
