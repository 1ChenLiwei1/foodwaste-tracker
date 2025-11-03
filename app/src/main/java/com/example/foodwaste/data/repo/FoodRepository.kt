package com.example.foodwaste.data.repo

import com.example.foodwaste.data.local.FoodItemDao
import com.example.foodwaste.data.model.FoodItem
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

class FoodRepository(private val dao: FoodItemDao) {
    fun observeAll(): Flow<List<FoodItem>> = dao.observeAll()
    fun observeExpiring(daysAhead: Long): Flow<List<FoodItem>> =
        dao.observeExpiring(LocalDate.now().plusDays(daysAhead))
    suspend fun upsert(item: FoodItem) = dao.upsert(item)
    suspend fun delete(item: FoodItem) = dao.delete(item)
}