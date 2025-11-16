package com.example.foodwaste.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodwaste.ui.InventoryViewModel
import com.example.foodwaste.data.model.Recipe

@Composable
fun RecipesScreen(vm: InventoryViewModel, nav: NavController) {

    val allRecipes = vm.allRecipes
    val food = vm.foodList.collectAsState().value.map { it.name.lowercase() }

    var search by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }

    // 评分函数
    fun score(recipe: Recipe): Int {
        val missing = recipe.ingredients.count { it.lowercase() !in food }
        return when (missing) {
            0 -> 5
            1 -> 4
            2 -> 3
            3 -> 2
            else -> 1
        }
    }

    // 初始列表
    var list = allRecipes

    // 搜索过滤
    if (search.isNotBlank()) {
        list = list.filter {
            it.name.contains(search, ignoreCase = true)
        }
    }

    // 分类过滤
    if (filter != "All") {
        list = list.filter { it.category == filter }
    }

    // 按评分排序（智能推荐）
    list = list.sortedByDescending { score(it) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //  AI 推荐按钮
        Button(
            onClick = { nav.navigate("ai_recipes") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("✨ AI Generate Recipes")
        }

        Spacer(Modifier.height(16.dp))

        // 搜索框
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Search recipe") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // 分类过滤栏
        RecipeFilterRow(selected = filter, onSelected = { filter = it })

        Spacer(Modifier.height(12.dp))

        // 列表
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(list) { recipe ->

                val missing = recipe.ingredients.filter { it.lowercase() !in food }

                RecipeCard(
                    title = recipe.name,
                    subtitle = if (missing.isEmpty())
                        "You can cook this now! ⭐️"
                    else
                        "Missing: ${missing.joinToString()}",
                    onClick = { nav.navigate("recipe_detail/${recipe.name}") }
                )
            }
        }
    }
}

@Composable
fun RecipeCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


