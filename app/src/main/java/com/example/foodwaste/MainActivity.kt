package com.example.foodwaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.*
import com.example.foodwaste.ui.InventoryViewModel
import com.example.foodwaste.ui.ShoppingViewModel
import com.example.foodwaste.ui.screens.*
import com.example.foodwaste.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

// ---------------------- 路由定义 ----------------------
sealed class Dest(val route: String) {
    data object Inventory : Dest("inventory")
    data object Recipes : Dest("recipes")
    data object Shopping : Dest("shopping")
    data object Profile : Dest("profile")
}

// ---------------------- MainActivity ----------------------
class MainActivity : ComponentActivity() {

    private val inventoryVM: InventoryViewModel by viewModels {
        val app = application as FoodWasteApp
        viewModelFactory { initializer { InventoryViewModel(app.repository) } }
    }

    private val shoppingVM: ShoppingViewModel by viewModels {
        val app = application as FoodWasteApp
        viewModelFactory { initializer { ShoppingViewModel(app.shoppingRepository) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodWasteAppUI(
                inventoryVM = inventoryVM,
                shoppingVM = shoppingVM
            )
        }
    }
}

// ---------------------- UI Scaffold ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodWasteAppUI(
    inventoryVM: InventoryViewModel,
    shoppingVM: ShoppingViewModel
) {
    MyApplicationTheme {

        val nav = rememberNavController()
        val backStack by nav.currentBackStackEntryAsState()
        val current = backStack?.destination?.route

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Food Waste Reduction Tracker") }
                )
            },

            bottomBar = {
                BottomAppBar {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // -------- 左 1：库存 --------
                        IconButton(onClick = {
                            nav.navigate(Dest.Inventory.route) {
                                launchSingleTop = true
                            }
                        }) {
                            Icon(
                                Icons.Default.List,
                                contentDescription = "Inventory",
                                tint = if (current == Dest.Inventory.route)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // -------- 左 2：食谱 --------
                        IconButton(onClick = {
                            nav.navigate(Dest.Recipes.route) {
                                launchSingleTop = true
                            }
                        }) {
                            Icon(
                                Icons.Default.Restaurant,
                                contentDescription = "Recipes",
                                tint = if (current == Dest.Recipes.route)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // -------- 中间大按钮：扫码 --------
                        FloatingActionButton(
                            onClick = { nav.navigate("scan") },
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(70.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, null, Modifier.size(32.dp))
                        }

                        // -------- 右 1：购物清单 --------
                        IconButton(onClick = {
                            nav.navigate(Dest.Shopping.route) {
                                launchSingleTop = true
                            }
                        }) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Shopping",
                                tint = if (current == Dest.Shopping.route)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // -------- 右 2：个人中心 --------
                        IconButton(onClick = {
                            nav.navigate(Dest.Profile.route) {
                                launchSingleTop = true
                            }
                        }) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = if (current == Dest.Profile.route)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        ) { inner ->

            NavHost(
                navController = nav,
                startDestination = Dest.Inventory.route,
                modifier = Modifier.padding(inner)
            ) {
                // 库存页
                composable(Dest.Inventory.route) {
                    InventoryScreen(vm = inventoryVM)
                }

                // 食谱页
                composable(Dest.Recipes.route) {
                    RecipesScreen(vm = inventoryVM, nav = nav)
                }

                // 购物清单页
                composable(Dest.Shopping.route) {
                    ShoppingScreen(vm = shoppingVM)
                }

                // Profile
                composable(Dest.Profile.route) {
                    ProfileScreen(
                        inventoryVM = inventoryVM,
                        shoppingVM = shoppingVM
                    )
                }

                // 扫码页（用你原来的 ScanScreen 逻辑）
                composable("scan") {
                    val snackbar = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()

                    Box(Modifier.fillMaxSize()) {
                        ScanScreen(
                            vm = inventoryVM,
                            onFinish = { nav.popBackStack() },
                            onItemAdded = { name ->
                                scope.launch {
                                    snackbar.showSnackbar("Added: $name")
                                }
                                nav.popBackStack()
                            }
                        )

                        SnackbarHost(
                            hostState = snackbar,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }

                // 食谱详情
                composable("recipe_detail/{name}") { entry ->
                    val name = entry.arguments?.getString("name") ?: ""
                    RecipeDetailScreen(
                        recipeName = name,
                        vm = inventoryVM,
                        onBack = { nav.popBackStack() }
                    )
                }

                // AI 菜谱页
                composable("ai_recipes") {
                    AiRecipeScreen(vm = inventoryVM, onBack = { nav.popBackStack() })
                }
            }
        }
    }
}
