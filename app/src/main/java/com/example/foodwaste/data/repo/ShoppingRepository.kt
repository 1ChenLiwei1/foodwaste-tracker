package com.example.foodwaste.data.repo

import com.example.foodwaste.data.local.ShoppingDao
import com.example.foodwaste.data.local.ShoppingItem
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val dao: ShoppingDao) {

    fun observeAll(): Flow<List<ShoppingItem>> = dao.observeAll()

    suspend fun add(name: String) =
        dao.upsert(ShoppingItem(name = name))

    suspend fun toggle(item: ShoppingItem) =
        dao.upsert(item.copy(checked = !item.checked))

    suspend fun delete(item: ShoppingItem) =
        dao.delete(item)

    suspend fun clearAll() = dao.clearAll()
}

