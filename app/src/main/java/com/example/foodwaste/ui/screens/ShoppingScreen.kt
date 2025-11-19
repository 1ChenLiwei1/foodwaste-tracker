package com.example.foodwaste.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.foodwaste.ui.ShoppingViewModel
import com.example.foodwaste.data.local.ShoppingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(vm: ShoppingViewModel) {

    val items by vm.list.collectAsState()
    var newItem by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FE))   // 清爽商务蓝背景
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Shopping List",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        // ------------------ 输入栏 ------------------
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newItem,
                onValueChange = { newItem = it },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                label = { Text("Add item") }
            )

            FloatingActionButton(
                onClick = {
                    if (newItem.isNotBlank()) {
                        vm.add(newItem.trim())
                        newItem = ""
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(50.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }

        // ------------------ 列表 ------------------
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                ShoppingItemRow(item, vm)
            }
        }
    }
}

@Composable
fun ShoppingItemRow(item: ShoppingItem, vm: ShoppingViewModel) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.checked,
                    onCheckedChange = { vm.toggle(item) }
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (item.checked) Color.Gray else Color.Black
                )
            }

            IconButton(onClick = { vm.delete(item) }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFD9534F)
                )
            }
        }
    }
}