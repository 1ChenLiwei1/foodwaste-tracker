package com.example.foodwaste.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodwaste.data.model.Recipe
import com.example.foodwaste.ui.InventoryViewModel

/* ============================================================
   食谱主页面（搜索 + 分类 + 推荐评分 + 跳转 AI）
   ============================================================ */
@Composable
fun RecipesScreen(
    vm: InventoryViewModel,
    nav: NavController
) {
    val owned = vm.foodList.collectAsState().value.map { it.name.lowercase() }
    val allRecipes = vm.allRecipes

    var search by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }

    /* ---------- 评分函数 ---------- */
    fun score(r: Recipe): Int =
        (5 - r.ingredients.count { it.lowercase() !in owned })
            .coerceIn(1, 5)

    /* ---------- 数据过滤 ---------- */
    var filtered = allRecipes.filter {
        it.name.contains(search, ignoreCase = true)
    }

    if (filter != "All") {
        filtered = filtered.filter { it.category == filter }
    }

    val sorted = filtered.sortedByDescending { score(it) }

    /* ---------- UI ---------- */
    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        /* -------- 搜索框 + AI 按钮 -------- */
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Search recipe") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { nav.navigate("ai_recipes") },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI")
                Spacer(Modifier.width(6.dp))
                Text("AI Recipes")
            }
        }

        Spacer(Modifier.height(12.dp))

        /* -------- 分类栏 -------- */
        RecipeCategoryChips(
            selected = filter,
            onSelected = { filter = it }
        )

        Spacer(Modifier.height(12.dp))

        /* -------- 列表 -------- */
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sorted) { recipe ->
                val missing = recipe.ingredients.filter { it.lowercase() !in owned }

                RecipeCard(
                    title = recipe.name,
                    subtitle =
                        if (missing.isEmpty())
                            "You can cook this ⭐"
                        else
                            "Missing: ${missing.joinToString()}",
                    onClick = {
                        nav.navigate("recipe_detail/${recipe.name}")
                    }
                )
            }
        }
    }
}

/* ============================================================
   分类 Chips（唯一定义）
   ============================================================ */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeCategoryChips(
    selected: String,
    onSelected: (String) -> Unit
) {
    val options = listOf("All", "Breakfast", "Main", "Snack", "Dessert")

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = (selected == option),
                onClick = { onSelected(option) },
                label = { Text(option) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

/* ============================================================
   食谱卡片
   ============================================================ */
@Composable
fun RecipeCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}