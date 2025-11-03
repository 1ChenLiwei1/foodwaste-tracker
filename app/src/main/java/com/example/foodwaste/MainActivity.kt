package com.example.foodwaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodwaste.ui.theme.MyApplicationTheme

// 三个导航目的地
sealed class Dest(val route: String, val label: String) {
    data object Inventory : Dest("inventory", "Inventory")
    data object Recipes : Dest("recipes", "Recipes")
    data object Shopping : Dest("shopping", "Shopping")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FoodWasteAppUI() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodWasteAppUI() {
    MyApplicationTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStack?.destination?.route

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Food Waste Reduction Tracker") })
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left button - Stock
                            IconButton(onClick = { navController.navigate(Dest.Inventory.route) }) {
                                Icon(
                                    Icons.Default.List,
                                    contentDescription = "Inventory",
                                    tint = if (currentRoute == Dest.Inventory.route)
                                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Scan button in the middle
                            FloatingActionButton(
                                onClick = { /* TODO: Open the QR code scanning interface */ },
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Scan")
                            }

                            // Right-hand button - Recipes
                            IconButton(onClick = { navController.navigate(Dest.Recipes.route) }) {
                                Icon(
                                    Icons.Default.Restaurant,
                                    contentDescription = "Recipes",
                                    tint = if (currentRoute == Dest.Recipes.route)
                                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Dest.Inventory.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Dest.Inventory.route) { InventoryScreen() }
                composable(Dest.Recipes.route) { RecipesScreen() }
                composable(Dest.Shopping.route) { ShoppingScreen() }
            }
        }
    }
}

/* ------------------ 页面内容 ------------------ */

@Composable
fun InventoryScreen() {
    InventoryCRUDScreen()  // ✅ 库存增删改查界面
}

@Composable
fun RecipesScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Recipe Recommendations (placeholder)")
    }
}

@Composable
fun ShoppingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Shopping List (placeholder)")
    }
}
