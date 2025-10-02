package com.example.foodwaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodwaste.data.model.FoodItem
import com.example.foodwaste.ui.components.FoodItemCard
import com.example.foodwaste.viewmodel.MainViewModel
import java.time.LocalDate

@Composable
fun InventoryScreen(vm: MainViewModel) {
    val items by vm.allItems.collectAsState()
    val expiring by vm.expiringSoon.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { vm.addSample() }) { Text("Add sample") }
            // 占位：条码扫描入口（后续接 ML Kit）
            OutlinedButton(onClick = { /* TODO: openScanner() */ }) { Text("Scan barcode") }
        }
        if (expiring.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            AssistChip(onClick = { }, label = { Text("Expiring in 3 days: ${expiring.size}") })
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(items, key = { it.id }) { item ->
                FoodItemCard(item) { vm.delete(item) }
            }
        }
    }
}