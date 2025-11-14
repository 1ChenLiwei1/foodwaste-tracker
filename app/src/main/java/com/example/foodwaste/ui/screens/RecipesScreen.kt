package com.example.foodwaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodwaste.ui.InventoryViewModel

@Composable
fun RecipesScreen(vm: InventoryViewModel) {

    val canMake = vm.recipesUserCanMake()
    val missing = vm.recipesMissingIngredients()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text("You can make:", style = MaterialTheme.typography.titleLarge)
        }

        items(canMake) { recipe ->
            RecipeCard(recipe.name, "All ingredients available!")
        }

        item {
            Spacer(Modifier.height(24.dp))
            Text("Missing ingredients:", style = MaterialTheme.typography.titleLarge)
        }

        items(missing) { (recipe, missingList) ->
            RecipeCard(recipe.name, "Missing: ${missingList.joinToString()}")
        }
    }
}

@Composable
fun RecipeCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
