package com.example.foodwaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodwaste.ui.InventoryViewModel
import com.example.foodwaste.ui.ShoppingViewModel

@Composable
fun ProfileScreen(
    inventoryVM: InventoryViewModel,
    shoppingVM: ShoppingViewModel
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            Icons.Default.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(100.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text("Profile", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(24.dp))

        ProfileActionCard(
            text = "Clear Inventory",
            onClick = { inventoryVM.clearAll() }
        )

        ProfileActionCard(
            text = "Clear Shopping List",
            onClick = { shoppingVM.clearAll() }
        )

        ProfileActionCard(
            text = "About App",
            onClick = { /* TODO: 介绍信息 */ }
        )
    }
}

@Composable
private fun ProfileActionCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.large,
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, style = MaterialTheme.typography.titleMedium)
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

