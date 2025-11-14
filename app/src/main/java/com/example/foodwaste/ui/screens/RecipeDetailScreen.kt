package com.example.foodwaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodwaste.data.RecipeProvider
import com.example.foodwaste.ui.InventoryViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeName: String,
    vm: InventoryViewModel,
    onBack: () -> Unit
) {
    // 取得对应食谱
    val recipe = RecipeProvider.recipes.find { it.name == recipeName }

    // 取得库存
    val foodList = vm.foodList.collectAsState(initial = emptyList()).value
    val ownedNames = foodList.map { it.name.lowercase() }

    if (recipe == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Recipe not found.")
        }
        return
    }

    val missing = recipe.ingredients.filter { it.lowercase() !in ownedNames }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipeName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text("Ingredients", style = MaterialTheme.typography.titleLarge)
            }

            itemsIndexed(recipe.ingredients) { _, ing ->
                Text("• $ing", style = MaterialTheme.typography.bodyLarge)
            }

            if (missing.isNotEmpty()) {
                item {
                    Text(
                        "Missing Ingredients",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                itemsIndexed(missing) { _, m ->
                    Text("✗ $m", color = MaterialTheme.colorScheme.error)
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                Text("Steps", style = MaterialTheme.typography.titleLarge)
            }

            // 步骤列表
            itemsIndexed(recipe.steps) { index, step ->
                Text("${index + 1}. $step", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}


