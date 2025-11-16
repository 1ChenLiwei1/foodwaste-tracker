package com.example.foodwaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecipeFilterRow(
    selected: String,
    onSelected: (String) -> Unit
) {
    val categories = listOf("All", "Breakfast", "Lunch", "Dinner", "Snack", "Drink")

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { cat ->
            FilterChip(
                selected = (selected == cat),
                onClick = { onSelected(cat) },
                label = { Text(cat) }
            )
        }
    }
}


