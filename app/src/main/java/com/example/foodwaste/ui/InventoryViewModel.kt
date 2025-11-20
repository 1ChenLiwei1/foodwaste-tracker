package com.example.foodwaste.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodwaste.data.FoodRepository
import com.example.foodwaste.data.model.Recipe
import com.example.foodwaste.data.RecipeProvider
import com.example.foodwaste.data.local.FoodItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class InventoryViewModel(
    private val repository: FoodRepository
) : ViewModel() {

    // Inventory List (Room Real-time Flow)
    val foodList: StateFlow<List<FoodItem>> =
        repository.observeAll()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Add or update
    fun addItem(item: FoodItem) = viewModelScope.launch {
        repository.upsert(item)
    }

    // Delete
    fun deleteItem(item: FoodItem) = viewModelScope.launch {
        repository.delete(item)
    }

    val allRecipes = RecipeProvider.recipes

    fun recipesUserCanMake(): List<Recipe> {
        val owned = foodList.value.map { it.name.lowercase() }
        return allRecipes.filter { recipe ->
            recipe.ingredients.all { it.lowercase() in owned }
        }
    }

    fun recipesMissingIngredients(): List<Pair<Recipe, List<String>>> {
        val owned = foodList.value.map { it.name.lowercase() }
        return allRecipes.map { recipe ->
            val missing = recipe.ingredients.filter { it.lowercase() !in owned }
            recipe to missing
        }
    }

    fun clearAll() = viewModelScope.launch {
        repository.clearAll()
    }
}




