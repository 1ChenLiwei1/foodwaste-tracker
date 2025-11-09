package com.example.foodwaste

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodwaste.data.local.FoodItem
import org.threeten.bp.LocalDate

@Composable
fun InventoryCRUDScreen() {
    var foodList by remember { mutableStateOf(listOf<FoodItem>()) }
    var newName by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text(
            text = "Inventory Management",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = newName,
                onValueChange = { newName = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Enter item name") }
            )
            Button(
                onClick = {
                    if (newName.isNotBlank()) {
                        val newItem = FoodItem(
                            name = newName,
                            expiryDate = LocalDate.now().plusDays(7)
                        )
                        foodList = foodList + newItem
                        newName = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(foodList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.name)
                        TextButton(onClick = {
                            foodList = foodList - item
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
