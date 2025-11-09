package com.example.foodwaste.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodwaste.data.FoodRepository
import com.example.foodwaste.data.local.FoodItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: FoodRepository) : ViewModel() {

    val foodList: StateFlow<List<FoodItem>> =
        repository.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addItem(item: FoodItem) {
        viewModelScope.launch { repository.upsert(item) }
    }

    fun deleteItem(item: FoodItem) {
        viewModelScope.launch { repository.delete(item) }
    }
}

