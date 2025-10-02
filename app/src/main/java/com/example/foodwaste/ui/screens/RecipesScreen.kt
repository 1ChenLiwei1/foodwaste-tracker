package com.example.foodwaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodwaste.data.model.Recipe
import com.example.foodwaste.viewmodel.MainViewModel

@Composable
fun RecipesScreen(vm: MainViewModel) {
    val items by vm.allItems.collectAsState()
    val pantry = items.map { it.name.lowercase() }.toSet()

    // Demo配方：后续可替换为AI/网络接口
    val recipes = listOf(
        Recipe("r1", "Leftover Fried Rice", listOf("rice","egg","carrot","onion"), listOf("heat pan","stir-fry")),
        Recipe("r2", "Milk Toast", listOf("milk","bread"), listOf("toast bread","pour milk"))
    )

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Suggested Recipes", style = MaterialTheme.typography.titleLarge)
        recipes.sortedByDescending { r -> r.ingredients.count { pantry.contains(it) } }
            .forEach { r ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(r.title, style = MaterialTheme.typography.titleMedium)
                        Text("Ingredients: " + r.ingredients.joinToString())
                        Text("Match: ${r.ingredients.count { pantry.contains(it) }}/${r.ingredients.size}")
                    }
                }
            }
    }
}