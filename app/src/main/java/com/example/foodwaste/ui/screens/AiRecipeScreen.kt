package com.example.foodwaste.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.foodwaste.ui.InventoryViewModel
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiRecipeScreen(
    vm: InventoryViewModel,
    onBack: () -> Unit
) {
    val inventory = vm.foodList.collectAsState().value

    var prompt by remember { mutableStateOf("") }

    val suggestion by remember(inventory, prompt) {
        mutableStateOf(
            buildString {
                appendLine("Based on your inventory, here are some ideas:")
                appendLine()
                val names = inventory.map { it.name }
                if (names.isEmpty()) {
                    appendLine("- Your inventory is empty. Consider buying some basic ingredients like eggs, rice, and vegetables.")
                } else {
                    appendLine("- Main available ingredients: ${names.joinToString()}")
                    appendLine("- Try combining 2–3 ingredients into a simple stir-fry or salad.")
                    appendLine("- You can also search online using these ingredients as keywords.")
                }
                if (prompt.isNotBlank()) {
                    appendLine()
                    appendLine("Extra preferences: $prompt")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Recipe Suggestions") },
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
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFBBDEFB), Color.White)
                    )
                )
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    label = { Text("Tell AI your mood / preference (e.g. quick dinner, high protein)") },
                    modifier = Modifier.fillMaxWidth()
                )

                ElevatedButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Generate (Mock)")
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            "AI Suggestions",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(suggestion, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}


