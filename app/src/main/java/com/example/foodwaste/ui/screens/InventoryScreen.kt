package com.example.foodwaste.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodwaste.data.local.FoodItem
import com.example.foodwaste.ui.InventoryViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

@Composable
fun InventoryScreen(vm: InventoryViewModel) {
    val list by vm.foodList.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Inventory",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            if (list.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No items yet. Tap + to add.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list) { item ->
                        InventoryItemCard(item = item, onDelete = {
                            vm.deleteItem(item)
                            scope.launch { snackbar.showSnackbar("Deleted ${item.name}") }
                        })
                    }
                }
            }
        }

        if (showDialog) {
            AddFoodDialog(
                onAdd = { name, date ->
                    vm.addItem(
                        FoodItem(
                            name = name,
                            expiryDate = date,
                            quantity = 1
                        )
                    )
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
private fun InventoryItemCard(
    item: FoodItem,
    onDelete: () -> Unit
) {
    val now = LocalDate.now()
    val days: Long? = item.expiryDate?.let { ChronoUnit.DAYS.between(now, it) }

    val isExpired = days != null && days <= 0
    val isSoon = days != null && days in 1..3

    val bgColor = when {
        isExpired -> Color(0xFFFFEBEE)
        isSoon -> Color(0xFFFFF3E0)
        else -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(bgColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isExpired || isSoon) Color(0xFFE53935)
                    else MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Expires: ${item.expiryDate ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isExpired) Color(0xFFE53935) else Color.Gray
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}

@Composable
fun AddFoodDialog(
    onAdd: (String, LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val parsed = try {
                    if (dateText.isBlank()) null else LocalDate.parse(dateText)
                } catch (_: Exception) {
                    null
                }
                if (name.isNotBlank()) {
                    onAdd(name, parsed)
                    onDismiss()
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add Food Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
