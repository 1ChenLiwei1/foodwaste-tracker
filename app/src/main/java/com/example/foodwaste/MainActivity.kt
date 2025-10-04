package com.example.foodwaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodwaste.ui.theme.MyApplicationTheme

sealed class Dest(val route: String, val label: String) {
    data object Inventory : Dest("inventory", "Inventory")
    data object Recipes   : Dest("recipes",   "Recipes")
    data object Shopping  : Dest("shopping",  "Shopping")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FoodWasteApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodWasteApp() {
    MyApplicationTheme {
        val navController = rememberNavController()
        val items = listOf(Dest.Inventory, Dest.Recipes, Dest.Shopping)

        Scaffold(
            topBar = { TopAppBar(title = { Text("Food Waste Reduction Tracker") }) },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { dest ->
                        NavigationBarItem(
                            selected = currentDestination.isTopLevel(dest),
                            onClick = {
                                navController.navigate(dest.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                }
                            },
                            icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                            label = { Text(dest.label) }
                        )
                    }
                }
            }
        ) { inner ->
            NavHost(
                navController = navController,
                startDestination = Dest.Inventory.route,
                modifier = Modifier.padding(inner)
            ) {
                composable(Dest.Inventory.route) { InventoryScreen() }
                composable(Dest.Recipes.route)   { RecipesScreen() }
                composable(Dest.Shopping.route)  { ShoppingScreen() }
            }
        }
    }
}

private fun NavDestination?.isTopLevel(dest: Dest): Boolean =
    this?.hierarchy?.any { it.route == dest.route } == true

/* ---------- 占位页面：先跑通 ---------- */

@Composable
fun InventoryScreen() {
    Surface { Text("Inventory — Managing contents of the refrigerator/pantry") }
}

@Composable
fun RecipesScreen() {
    Surface { Text("Recipes — Recipe recommendations based on existing ingredients") }
}

@Composable
fun ShoppingScreen() {
    Surface { Text("Shopping List — Automatically generated/manually edited shopping list") }
}
