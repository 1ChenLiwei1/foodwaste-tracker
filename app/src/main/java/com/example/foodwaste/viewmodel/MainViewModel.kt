package com.example.foodwaste.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodwaste.data.local.AppDatabase
import com.example.foodwaste.data.model.FoodItem
import com.example.foodwaste.data.repo.FoodRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = FoodRepository(AppDatabase.get(app).foodDao())

    // 全部物品
    val allItems = repo.observeAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // 3 天内过期的物品
    val expiringSoon = repo.observeExpiring(daysAhead = 3)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // 添加示例数据
    fun addSample() = viewModelScope.launch {
        repo.upsert(
            FoodItem(
                name = "Milk",
                quantity = 1,
                category = "Dairy",
                expiryDate = LocalDate.now().plusDays(2)
            )
        )
    }

    fun upsert(item: FoodItem) = viewModelScope.launch { repo.upsert(item) }
    fun delete(item: FoodItem) = viewModelScope.launch { repo.delete(item) }
}