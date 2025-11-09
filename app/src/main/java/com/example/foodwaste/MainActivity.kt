package com.example.foodwaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodwaste.ui.InventoryViewModel
import com.example.foodwaste.ui.screens.InventoryScreen
import com.example.foodwaste.ui.theme.MyApplicationTheme

// 三个导航目的地
sealed class Dest(val route: String, val label: String) {
    data object Inventory : Dest("inventory", "Inventory")
    data object Recipes : Dest("recipes", "Recipes")
    data object Shopping : Dest("shopping", "Shopping")
}

class MainActivity : ComponentActivity() {

    // ✅ 提升 ViewModel 到 Activity 层
    private val inventoryVM: InventoryViewModel by viewModels {
        val app = application as FoodWasteApp
        viewModelFactory {
            initializer { InventoryViewModel(app.repository) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodWasteAppUI(inventoryVM)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodWasteAppUI(vm: InventoryViewModel) {
    MyApplicationTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStack?.destination?.route

        Scaffold(
            topBar = { TopAppBar(title = { Text("Food Waste Reduction Tracker") }) },
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
                            // 左侧 - 库存
                            IconButton(onClick = { navController.navigate(Dest.Inventory.route) }) {
                                Icon(
                                    Icons.Default.List,
                                    contentDescription = "Inventory",
                                    tint = if (currentRoute == Dest.Inventory.route)
                                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // 中间扫码按钮
                            FloatingActionButton(
                                onClick = { /* TODO: 打开扫码界面 */ },
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Scan")
                            }

                            // 右侧 - 食谱
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
                // ✅ 将 VM 传给库存页
                composable(Dest.Inventory.route) { InventoryScreen(vm = vm) }
                composable(Dest.Recipes.route) { RecipesScreen() }
                composable(Dest.Shopping.route) { ShoppingScreen() }
            }
        }
    }
}

/* ------------------ 页面内容 ------------------ */

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
