package com.harrisonog.arcraiderscompanion.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harrisonog.arcraiderscompanion.domain.model.*

class Converters {
    private val gson = Gson()

    // Quest-related converters
    @TypeConverter
    fun fromQuestObjectiveList(value: List<QuestObjective>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toQuestObjectiveList(value: String): List<QuestObjective> {
        val type = object : TypeToken<List<QuestObjective>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromRequiredItemList(value: List<RequiredItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toRequiredItemList(value: String): List<RequiredItem> {
        val type = object : TypeToken<List<RequiredItem>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromRewardList(value: List<Reward>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toRewardList(value: String): List<Reward> {
        val type = object : TypeToken<List<Reward>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringSet(value: Set<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringSet(value: String): Set<String> {
        val type = object : TypeToken<Set<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    // Item-related converters
    @TypeConverter
    fun fromRecyclingMaterialList(value: List<RecyclingMaterial>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toRecyclingMaterialList(value: String): List<RecyclingMaterial> {
        val type = object : TypeToken<List<RecyclingMaterial>>() {}.type
        return gson.fromJson(value, type)
    }

    // Enum converters
    @TypeConverter
    fun fromQuestStatus(value: QuestStatus): String {
        return value.name
    }

    @TypeConverter
    fun toQuestStatus(value: String): QuestStatus {
        return QuestStatus.valueOf(value)
    }

    @TypeConverter
    fun fromItemCategory(value: ItemCategory): String {
        return value.name
    }

    @TypeConverter
    fun toItemCategory(value: String): ItemCategory {
        return ItemCategory.valueOf(value)
    }

    @TypeConverter
    fun fromItemRarity(value: ItemRarity): String {
        return value.name
    }

    @TypeConverter
    fun toItemRarity(value: String): ItemRarity {
        return ItemRarity.valueOf(value)
    }
}
