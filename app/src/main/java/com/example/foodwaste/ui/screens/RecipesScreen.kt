package com.example.foodwaste.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodwaste.ui.InventoryViewModel

@Composable
fun RecipesScreen(vm: InventoryViewModel, nav: NavController) {

    val canMake = vm.recipesUserCanMake()
    val missing = vm.recipesMissingIngredients()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item { Text("You can make:", style = MaterialTheme.typography.titleLarge) }

        items(canMake) { recipe ->
            RecipeCard(
                title = recipe.name,
                subtitle = "All ingredients available!",
                onClick = { nav.navigate("recipe_detail/${recipe.name}") }
            )
        }

        item {
            Spacer(Modifier.height(24.dp))
            Text("Missing ingredients:", style = MaterialTheme.typography.titleLarge)
        }

        items(missing) { (recipe, missingList) ->
            RecipeCard(
                title = recipe.name,
                subtitle = "Missing: ${missingList.joinToString()}",
                onClick = { nav.navigate("recipe_detail/${recipe.name}") }
            )
        }
    }
}

@Composable
fun RecipeCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

