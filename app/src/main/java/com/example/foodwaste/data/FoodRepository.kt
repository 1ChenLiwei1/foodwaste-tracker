package com.example.foodwaste.data

import com.example.foodwaste.data.local.FoodItem
import com.example.foodwaste.data.local.FoodItemDao
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

class FoodRepository(private val dao: FoodItemDao) {

    fun observeAll(): Flow<List<FoodItem>> = dao.observeAll()

    fun observeExpiring(until: LocalDate): Flow<List<FoodItem>> = dao.observeExpiring(until)

    suspend fun upsert(item: FoodItem) = dao.upsert(item)

    suspend fun delete(item: FoodItem) = dao.delete(item)

    suspend fun clearAll() = dao.clearAll()
}