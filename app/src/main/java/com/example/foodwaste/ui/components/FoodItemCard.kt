package com.example.foodwaste.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodwaste.data.model.FoodItem
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun FoodItemCard(
    item: FoodItem,
    onDelete: (() -> Unit)? = null
) {
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().height(0.dp)) {}
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(item.name, style = MaterialTheme.typography.titleMedium)
            Text("Qty: ${item.quantity} • ${item.category}")

            val expiryText = item.expiryDate?.format(fmt) ?: "—"
            Text("Expiry: $expiryText")

            onDelete?.let {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = it) { Text("Delete") }
            }
        }
    }
}