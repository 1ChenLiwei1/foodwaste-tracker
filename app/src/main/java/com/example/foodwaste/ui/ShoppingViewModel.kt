package com.example.foodwaste.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodwaste.data.local.ShoppingItem
import com.example.foodwaste.data.repo.ShoppingRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repo: ShoppingRepository
) : ViewModel() {

    val list = repo.observeAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun add(name: String) = viewModelScope.launch {
        repo.add(name)
    }

    fun toggle(item: ShoppingItem) = viewModelScope.launch {
        repo.toggle(item)
    }

    fun delete(item: ShoppingItem) = viewModelScope.launch {
        repo.delete(item)
    }

    fun clearAll() = viewModelScope.launch {
        repo.clearAll()
    }
}

