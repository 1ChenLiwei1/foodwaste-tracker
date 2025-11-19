package com.example.foodwaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodwaste.data.model.Recipe
import com.example.foodwaste.ui.InventoryViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeName: String,
    vm: InventoryViewModel,
    onBack: () -> Unit
) {
    val allRecipes = vm.allRecipes
    val owned = vm.foodList.collectAsState().value.map { it.name.lowercase() }

    val recipe = allRecipes.firstOrNull { it.name == recipeName }

    if (recipe == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Recipe") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Recipe not found")
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe.name) },
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
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text("Ingredients", style = MaterialTheme.typography.titleMedium)
            }

            itemsIndexed(recipe.ingredients) { _, ing ->
                val has = ing.lowercase() in owned
                AssistChip(
                    onClick = {},
                    label = { Text(ing) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (has) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        labelColor = if (has) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
                Text("Steps", style = MaterialTheme.typography.titleMedium)
            }

            itemsIndexed(recipe.steps) { index, step ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text("${index + 1}. ", style = MaterialTheme.typography.titleMedium)
                        Text(step, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}



