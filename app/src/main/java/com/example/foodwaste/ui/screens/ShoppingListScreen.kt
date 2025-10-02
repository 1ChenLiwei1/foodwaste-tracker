package com.example.foodwaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodwaste.viewmodel.MainViewModel
import org.threeten.bp.LocalDate

@Composable
fun ShoppingListScreen(vm: MainViewModel) {
    val items by vm.allItems.collectAsState()
    val list = items.filter { it.quantity <= 0 || (it.expiryDate != null && it.expiryDate!!.isBefore(LocalDate.now().plusDays(1))) }
        .map { it.name }.distinct()

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Shopping List (auto)", style = MaterialTheme.typography.titleLarge)
        if (list.isEmpty()) Text("All good! No urgent items.")
        list.forEach { name ->
            AssistChip(onClick = {}, label = { Text(name) })
        }
    }
}