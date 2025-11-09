package com.example.foodwaste.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.foodwaste.FoodWasteApp
import com.example.foodwaste.data.local.FoodItem
import com.example.foodwaste.ui.InventoryViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

@Composable
fun InventoryScreen(vm: InventoryViewModel) {
    // 获取 ViewModel + Repository
    val context = LocalContext.current.applicationContext as FoodWasteApp
    val viewModel: InventoryViewModel = viewModel(
        factory = viewModelFactory {
            initializer { InventoryViewModel(context.repository) }
        }
    )
    val foodList by viewModel.foodList.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Inventory Management",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            if (foodList.isEmpty()) {
                Text("No food items yet. Add one!", color = Color.Gray)
            } else {
                LazyColumn {
                    items(foodList) { item ->
                        val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), item.expiryDate)
                        val isExpiringSoon = daysLeft in 1..3
                        val isExpired = daysLeft <= 0

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isExpired -> Color(0xFFFFE0E0)
                                    isExpiringSoon -> Color(0xFFFFF3E0)
                                    else -> MaterialTheme.colorScheme.surface
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isExpired || isExpiringSoon) Color.Red else Color.Unspecified
                                    )
                                    Text(
                                        text = "Expires: ${item.expiryDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isExpired) Color.Red else Color.Gray
                                    )
                                }
                                TextButton(onClick = { viewModel.deleteItem(item) }) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AddFoodDialog(
                onAdd = { name, date ->
                    val newItem = FoodItem(name = name, expiryDate = date)
                    vm.addItem(newItem)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun AddFoodDialog(onAdd: (String, LocalDate) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val parsedDate = try {
                    LocalDate.parse(dateText)
                } catch (e: Exception) {
                    LocalDate.now().plusDays(7)
                }
                if (name.isNotBlank()) onAdd(name, parsedDate)
            }) {
                Text("Add")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add Food Item") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Expiry Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
